package com.alexpi.texttools.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

class CustomTextField : TextInputLayout {
    private val customTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (isErrorEnabled) {
                isErrorEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addOnEditTextAttachedListener {
            editText?.addTextChangedListener(customTextWatcher)
        }
    }
    fun showError(text: String? = null){
        error = text
        isErrorEnabled = true
    }
}
