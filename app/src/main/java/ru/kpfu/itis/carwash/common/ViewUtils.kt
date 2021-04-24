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
import java.text.SimpleDateFormat
import java.time.LocalDate
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

fun EditText.dateFormatter(date: Date){
    this.setText(SimpleDateFormat("MM/dd/yyyy", Locale.forLanguageTag("ru")).format(date))
}

fun TextView.setDate(date: Date){
    val format = SimpleDateFormat("EE, dd MMMM", Locale.forLanguageTag("ru"))
    this.text = format.format(date)
}