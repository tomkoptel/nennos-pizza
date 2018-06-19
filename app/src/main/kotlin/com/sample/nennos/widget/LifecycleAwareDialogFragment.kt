package com.sample.nennos.widget

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.nennos.ktx.arch.ActionLiveData
import com.sample.nennos.ktx.arch.observeNonNull
import kotlinx.android.parcel.Parcelize

/**
 * The convenient way of launching the dialog that persists across [FragmentActivity] configuration changes.
 * The API supports the builder pattern that allows to configure the variation of the outer look of the dialog.
 * All events streamlined through [ActionLiveData] instance and allows to handle events even when the dialog recreated. In other words, allows to escape
 * a situation when recreated dialog and client that uses it looses connecting callbacks.
 *
 * ```
 * dialog(Arguments(
 *  id = "some-dialog",
 *  title = "Some title",
 *  message = "Some description"
 * )).show(fragmentManager, null)
 *
 * dialogEvents("some-dialog").observe(lifecycleOwner, Observer<LifecycleDialogEvent>{
 *   when(it) {
 *       DialogEvent.OnPositiveBtnClick -> {}
 *       DialogEvent.OnNegativeBtnClick -> {}
 *       DialogEvent.OnNeutralBtnClick -> {}
 *       DialogEvent.OnOnItemClick -> {}
 *       DialogEvent.OnCancel -> {}
 *       DialogEvent.OnDismiss -> {}
 *       DialogEvent.OnShow -> {}
 *   }
 * })
 * ```
 */
class LifecycleAwareDialogFragment : DialogFragment() {
    private val args by lazy(LazyThreadSafetyMode.NONE) {
        val bundle = arguments ?: Bundle.EMPTY
        bundle.getParcelable<Arguments>(ARGUMENTS_KEY)!!
    }
    private lateinit var model: LifecycleAwareDialogViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val store = (activity as FragmentActivity).viewModelStore
        model = ViewModelProvider(store, ViewModelProvider.NewInstanceFactory()).get(args.tag, LifecycleAwareDialogViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity as FragmentActivity)
                .apply {
                    args.title?.let { setTitle(it) }
                    args.titleResId?.let { setTitle(it) }

                    args.message?.let { setMessage(it) }
                    args.messageResId?.let { setMessage(it) }

                    args.positiveBtnResId?.let {
                        setPositiveButton(it) { dialog, _ ->
                            model.events.value = DialogEvent.OnPositiveBtnClick(dialog as Dialog)
                        }
                    }
                    args.positiveBtn?.let {
                        setPositiveButton(it) { dialog, _ ->
                            model.events.value = DialogEvent.OnPositiveBtnClick(dialog as Dialog)
                        }
                    }
                    args.negativeBtnResId?.let {
                        setNegativeButton(it) { dialog, _ ->
                            model.events.value = DialogEvent.OnNegativeBtnClick(dialog as Dialog)
                        }
                    }
                    args.negativeBtn?.let {
                        setNegativeButton(it) { dialog, _ ->
                            model.events.value = DialogEvent.OnNegativeBtnClick(dialog as Dialog)
                        }
                    }
                    args.neutralBtnResId?.let {
                        setNeutralButton(it) { dialog, _ ->
                            model.events.value = DialogEvent.OnNeutralBtnClick(dialog as Dialog)
                        }
                    }
                    args.neutralBtn?.let {
                        setNeutralButton(it) { dialog, _ ->
                            model.events.value = DialogEvent.OnNeutralBtnClick(dialog as Dialog)
                        }
                    }
                    if (args.cancellable) {
                        setCancelable(args.cancellable)
                    }
                    val items = args.items
                    if (items != null) {
                        val keys = items.keys.toList()
                        val labels = items.values.toTypedArray()
                        setItems(labels) { dialog, which ->
                            val key = keys[which]
                            val label = items.getValue(key)
                            model.events.value = DialogEvent.OnItemClick(dialog as Dialog, key to label)
                        }
                    }
                    args.customLayout?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            setView(it)
                        } else {
                            LayoutInflater.from(activity).inflate(it, null).let { customView ->
                                setView(customView)
                            }
                        }
                    }
                }
                .create()
                .apply {
                    setOnShowListener {
                        model.events.value = DialogEvent.OnShow(dialog as Dialog)
                    }
                    setCanceledOnTouchOutside(args.cancellableOnOutside)
                }
    }

    override fun onCancel(dialog: DialogInterface) {
        model.events.value = DialogEvent.OnCancel(dialog as Dialog)
    }
}

sealed class DialogEvent {
    abstract val dialog: Dialog

    /**
     * Gets emitted when user clicks on positive button.
     */
    class OnPositiveBtnClick(override val dialog: Dialog) : DialogEvent()

    /**
     * Gets emitted when user clicks on negative button.
     */
    class OnNegativeBtnClick(override val dialog: Dialog) : DialogEvent()

    /**
     * Gets emitted when user clicks on neutral button.
     */
    class OnNeutralBtnClick(override val dialog: Dialog) : DialogEvent()

    /**
     * Gets emitted when user clicks on item in the list dialog.
     */
    class OnItemClick(override val dialog: Dialog, val item: Pair<Key, Label>) : DialogEvent()

    /**
     * Gets emitted when user cancels dialog by either clicking back or touching outside the dialog area.
     */
    class OnCancel(override val dialog: Dialog) : DialogEvent()

    /**
     * Gets emitted when the dialog is shown.
     */
    class OnShow(override val dialog: Dialog) : DialogEvent()
}

private const val ARGUMENTS_KEY = "custom_args"
private const val DEFAULT_TAG = "singleton_dialog"

@Parcelize
class Arguments(
        /**
         * Unique tag of dialog used to create an association between dialog and callbacks.
         */
        val tag: String = DEFAULT_TAG,
        /**
         * Set the dialog title as [String].
         */
        val title: String? = null,
        /**
         * Set the dialog title as string resource. E.g. R.string.dialog_title
         */
        @StringRes
        val titleResId: Int? = null,

        /**
         * Set the dialog message as [String].
         */
        val message: String? = null,
        /**
         * Set the dialog message as string resource. E.g. R.string.dialog_message
         */
        @StringRes
        val messageResId: Int? = null,

        /**
         * Enables positive button with the wording.
         */
        val positiveBtn: String? = null,
        /**
         * Enables positive button with the wording extracted from string resource.
         */
        @StringRes
        val positiveBtnResId: Int? = null,

        /**
         * Enables negative button with the wording.
         */
        val negativeBtn: String? = null,
        /**
         * Enables negative button with the wording extracted from string resource.
         */
        @StringRes
        val negativeBtnResId: Int? = null,

        /**
         * Enables neutral button with the wording.
         */
        val neutralBtn: String? = null,
        /**
         * Enables neutral button with the wording extracted from string resource.
         */
        @StringRes
        val neutralBtnResId: Int? = null,

        /**
         * Marks dialog as cancellable.
         */
        val cancellable: Boolean = true,
        /**
         * Enables/disables option of cancelling dialog outside the boundaries.
         */
        val cancellableOnOutside: Boolean = true,

        /**
         * Allows to configure dialog with a set of items and display it as single choice dialog.
         */
        val items: Map<String, String>? = null,

        /**
         * Allows to override dialog layout.
         */
        @LayoutRes
        val customLayout: Int? = null
) : Parcelable

// Syntactic sugar to explicitly describe item as 'key' in the list dialog.
typealias Key = CharSequence

// Syntactic sugar to explicitly describe item as 'value' in the list dialog.
typealias Label = CharSequence

/**
 * Bridging model that allows to streamline events from [LifecycleAwareDialogFragment].
 */
class LifecycleAwareDialogViewModel : ViewModel() {
    val events: MutableLiveData<DialogEvent> = ActionLiveData()
}

/**
 * Shortcut API to start observing dialog events.
 */
fun FragmentActivity.observeDialogEvents(tag: String = DEFAULT_TAG, observer: (DialogEvent) -> Unit) {
    onDialogEvents(tag).observeNonNull(this, observer)
}

/**
 * Syntactic sugar API that allows client start listening for event emissions from the dialog.
 */
fun FragmentActivity.onDialogEvents(tag: String = DEFAULT_TAG): LiveData<DialogEvent> =
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory())
                .get(tag, LifecycleAwareDialogViewModel::class.java).events

/**
 * Syntactic sugar API that creates an instance of [LifecycleAwareDialogFragment] on the basis of builder pattern.
 */
fun dialog(args: Arguments): DialogFragment =
        LifecycleAwareDialogFragment().apply {
            arguments = Bundle().apply { putParcelable(ARGUMENTS_KEY, args) }
        }