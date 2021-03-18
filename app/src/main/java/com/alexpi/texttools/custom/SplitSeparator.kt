package com.alexpi.texttools

import kotlin.text.Regex

sealed class SplitSeparator
data class SymbolSeparator(val symbol: String): SplitSeparator()
data class RegexSeparator(val regex: Regex): SplitSeparator()
data class LengthSeparator(val length: Int): SplitSeparator()