package ru.kpfu.itis.carwash.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import ru.kpfu.itis.carwash.R

class MyToolbar(
    context: Context,
    attrs: AttributeSet,
) : Toolbar(context, attrs) {

    private var searchView: Boolean
    private var settings: Boolean
    private var signOut: Boolean

    init {
        inflateMenu(R.menu.nav_menu)
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        setTitleTextColor(ContextCompat.getColor(context, R.color.white))
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyToolbar,
            0,
            0
        ).apply {

            try {
                searchView = getBoolean(R.styleable.MyToolbar_showSearchView, false)
                settings = getBoolean(R.styleable.MyToolbar_showSettings, false)
                signOut = getBoolean(R.styleable.MyToolbar_showSignOut, false)

                setSettings(settings)
                setSearchView(searchView)
                setSignOut(signOut)
            } finally {
                recycle()
            }
        }
    }

    fun setSettings(isShow: Boolean) {
        menu.findItem(R.id.nav_setting).isVisible = isShow
    }

    fun setSearchView(isShow: Boolean) {
        menu.findItem(R.id.nav_search).isVisible = isShow
    }

    fun setSignOut(isShow: Boolean) {
        menu.findItem(R.id.nav_sign_out).isVisible = isShow
    }
}
