package com.alexpi.texttools.extension

import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.fragment.*

fun getToolFragment(n: Int): BaseToolFragment<*> =
    when(n){
        0 -> SplitTextFragment()
        1 -> JoinTextFragment()
        2 -> RepeatTextFragment()
        3 -> ReverseTextFragment()
        4 -> TruncateTextFragment()
        5 -> TrimTextFragment()
        6 -> AddLeftPaddingFragment()
        7 -> AddRightPaddingFragment()
        8 -> AddPrefixFragment()
        9 -> AddSuffixFragment()
        10 -> RemoveEmptyLinesFragment()
        11 -> RemoveDuplicateLinesFragment()
        else -> FilterTextFragment()
    }