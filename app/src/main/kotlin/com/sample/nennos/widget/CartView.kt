package com.sample.nennos.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.sample.nennos.R
import com.sample.nennos.domain.Cart
import com.sample.nennos.ktx.arch.observeNonNull
import com.sample.nennos.ktx.asActivity
import com.sample.nennos.ktx.provideModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cart_view_widget.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider

class CartView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LayoutContainer, KodeinAware {
    private val layoutView = inflate(context, R.layout.cart_view_widget, this)
    override val containerView: View get() = layoutView

    override val kodeinContext: KodeinContext<*> = kcontext(this)

    private val _parentKodein by closestKodein(context)

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein)
        bind<FragmentActivity>() with provider { this@CartView.context.asActivity() }
        bind<CartViewModel>() with provider {
            instance<FragmentActivity>().provideModel { CartViewModel(instance(), instance()) }
        }
    }

    private val cartModel by instance<CartViewModel>()
    private val activity by instance<FragmentActivity>()

    init {
        isClickable = true
        isFocusable = true

        if (isInEditMode) {
            itemNumber.text = 10.toString()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return
        cartModel.cartObservable.observeNonNull(activity, ::updateUi)
    }

    override fun onDetachedFromWindow() {
        if (isInEditMode) return

        cartModel.cartObservable.removeObservers(activity)
        super.onDetachedFromWindow()
    }

    private fun updateUi(cart: Cart) {
        itemNumber.text = cart.items.size.toString()
    }
}
