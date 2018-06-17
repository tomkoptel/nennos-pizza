package com.sample.nennos.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.sample.nennos.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.pay_button_widget.*

class PackedButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LayoutContainer {
    private val layoutView = ConstraintLayout.inflate(context, R.layout.pay_button_widget, this)
    override val containerView: View get() = layoutView

    private val templateResId: Int

    init {
        isClickable = true
        isFocusable = true

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PackedButton, 0, 0)
        templateResId = typedArray.getResourceId(R.styleable.PackedButton_template, 0)
        val drawableStart = typedArray.getDrawable(R.styleable.PackedButton_drawableStart)
        typedArray.recycle()

        buyText.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
    }

    fun setPrice(formattedPrice: String) {
        if (templateResId == 0) {
            buyText.text = formattedPrice
        } else {
            buyText.text = HtmlCompat.fromHtml(
                    context.resources.getString(templateResId, formattedPrice),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }
    }
}