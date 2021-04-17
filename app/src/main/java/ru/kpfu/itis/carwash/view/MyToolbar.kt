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

class MyToolbar(
    context: Context,
    attrs: AttributeSet,
) : LinearLayout(context, attrs) {

    private var leftIcon: Drawable? = null
    private var right1Icon: Drawable? = null
    private var right2Icon: Drawable? = null
    private var title: String? = null
    private val binding: CustomToolbarBinding

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyToolbar,
            0,
            0
        ).apply {
            try {
                binding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.custom_toolbar,
                    this@MyToolbar,
                    true
                )
                leftIcon = getDrawable(R.styleable.MyToolbar_setLeftIcon)
                right1Icon = getDrawable(R.styleable.MyToolbar_setRight1Icon)
                right2Icon = getDrawable(R.styleable.MyToolbar_setRight2Icon)
                title = getString(R.styleable.MyToolbar_setTitle)

                title?.let { setTitle(it) }
                leftIcon?.let { setLeftIcon(it) }
                right1Icon?.let { setRight1Icon(it) }
                right2Icon?.let { setRight2Icon(it) }
            } finally {
                recycle()
            }
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
