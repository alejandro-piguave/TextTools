package com.alexpi.texttools.fragment


import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseAddAffixFragment
class AddSuffixFragment : BaseAddAffixFragment(){
    override val affixEditTextHintId: Int
        get() = R.string.suffix_text
    override val addAffixButtonTextId: Int
        get() = R.string.add_suffix

    override fun addAffix(inputText: String, affix: String): String = inputText + affix

}