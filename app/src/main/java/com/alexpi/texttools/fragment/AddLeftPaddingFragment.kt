package com.alexpi.texttools.fragment

import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseAddPaddingFragment
import com.alexpi.texttools.extension.padStart
class AddLeftPaddingFragment : BaseAddPaddingFragment(){
    override val addPaddingButtonTextId: Int = R.string.add_left_padding

    override fun addPadding(inputText: String, textLength: Int, paddingString: String): String = inputText.padStart(textLength, paddingString)
}