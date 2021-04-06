package com.alexpi.texttools.custom

import kotlin.text.Regex

sealed class FilterPattern
data class TextFilter(val text: String): FilterPattern()
data class RegexFilter(val regex: Regex): FilterPattern()