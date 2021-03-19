package com.alexpi.texttools.extension

fun String.insertPeriodically(insert: String, period: Int): String {
    val builder = StringBuilder(
        this.length + insert.length * (this.length / period) + 1
    )
    var index = 0
    var prefix = ""
    while (index < this.length) {
        // Don't put the insert in the very first iteration.
        // This is easier than appending it *after* each substring
        builder.append(prefix)
        prefix = insert
        builder.append(
            this.substring(
                index,
                kotlin.math.min(index + period, this.length)
            )
        )
        index += period
    }
    return builder.toString()
}

fun String.takeAndAddSuffix(n: Int, suffix: String): String =  this.take(n - suffix.length) + suffix

fun String.takeLastAndAddPrefix(n: Int, prefix: String): String = prefix + this.takeLast(n - prefix.length)

fun String.trim(trimStart: Boolean, trimEnd: Boolean) =
    if(trimStart && trimEnd) this.trim()
    else if(trimStart && !trimEnd) this.trimStart()
    else if(trimEnd && !trimStart) trimEnd() else this