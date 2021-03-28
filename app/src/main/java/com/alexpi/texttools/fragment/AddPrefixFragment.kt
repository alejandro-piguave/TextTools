package com.alexpi.texttools.fragment

import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseAddAffixFragment

class AddPrefixFragment : BaseAddAffixFragment(){
    override val affixEditTextHintId: Int
        get() = R.string.prefix_text
    override val addAffixButtonTextId: Int
        get() = R.string.add_prefix

    override fun addAffix(inputText: String, affix: String): String = affix + inputText

}