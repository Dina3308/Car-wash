package ru.kpfu.itis.carwash.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.CustomToolbarBinding

class MyToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomToolbarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.custom_toolbar,
        this@MyToolbar,
        true
    )

    init {
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyToolbar)

            val leftIcon = typedArray.getDrawable(R.styleable.MyToolbar_setLeftIcon)
            leftIcon?.let { setLeftIcon(it) }

            val right1Icon = typedArray.getDrawable(R.styleable.MyToolbar_setRight1Icon)
            right1Icon?.let { setRight1Icon(it) }

            val right2Icon = typedArray.getDrawable(R.styleable.MyToolbar_setRight2Icon)
            right2Icon?.let { setRight2Icon(it) }

            val title = typedArray.getString(R.styleable.MyToolbar_setTitle)
            title?.let { setTitle(it) }

            typedArray.recycle()
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setLeftIcon(icon: Drawable) {
        binding.leftIcon.run {
            setImageDrawable(icon)
            (layoutParams as LayoutParams).marginStart = 8
        }
    }

    fun setRight1Icon(icon: Drawable) {
        binding.right1Icon.setImageDrawable(icon)
    }

    fun setRight2Icon(icon: Drawable) {
        binding.right2Icon.setImageDrawable(icon)
    }

    fun leftIconClickListener(itemClick: (View) -> Unit) {
        binding.leftIcon.setOnClickListener {
            it?.also(itemClick)
        }
    }

    fun right1IconClickListener(itemClick: (View) -> Unit) {
        binding.right1Icon.setOnClickListener {
            it?.also(itemClick)
        }
    }

    fun right2IconClickListener(itemClick: (View) -> Unit) {
        binding.right2Icon.setOnClickListener {
            it?.also(itemClick)
        }
    }
}
