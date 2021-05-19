package ru.kpfu.itis.carwash.common

import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import ru.kpfu.itis.carwash.R
import java.text.SimpleDateFormat
import java.util.*

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan,
            startIndexOfLink,
            startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun EditText.dateFormatter(date: Date) {
    this.setText(SimpleDateFormat("MM/dd/yyyy", Locale.forLanguageTag(resources.getString(R.string.language_tag))).format(date))
}

fun TextView.setDate(date: Date, id: Int? = null) {
    val format = SimpleDateFormat("EE, dd MMMM", Locale.forLanguageTag(resources.getString(R.string.language_tag)))
    if (id == null) {
        this.text = format.format(date)
    } else {
        this.text = resources.getString(id, format.format(date))
    }
}

fun EditText.isValidEmail(email: String) {
    val matchResult = Regex("^[A-Za-z0-9._%+-]+@([A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,6}\$").find(email)
    when {
        email.isEmpty() -> this.error = resources.getString(R.string.field_is_empty)
        matchResult == null -> this.error = resources.getString(R.string.email_no_correct)
        else -> this.error = null
    }
}

fun EditText.isValidPassword(password: String, passwordRepeat: String = "") {
    when {
        password.isEmpty() -> this.error = resources.getString(R.string.field_is_empty)
        password.length < 6 -> this.error = resources.getString(R.string.password_no_correct)
        passwordRepeat.isNotEmpty() && passwordRepeat != password -> this.error = resources.getString(R.string.password_repeat__no_correct)
        else -> this.error = null
    }
}

fun EditText.isChosenCity(city: String) {
    when {
        city.isEmpty() -> this.error = resources.getString(R.string.search_city_is_empty)
        else -> this.error = null
    }
}
