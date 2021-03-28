package com.alexpi.texttools.fragment

import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseAddPaddingFragment
import com.alexpi.texttools.extension.padEnd

class AddRightPaddingFragment : BaseAddPaddingFragment(){
    override val addPaddingButtonTextId: Int = R.string.add_right_padding

    override fun addPadding(inputText: String, textLength: Int, paddingString: String): String = inputText.padEnd(textLength, paddingString)
}