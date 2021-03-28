package com.alexpi.texttools.extension

fun String.insertPeriodically(insert: String, period: Int): String {
    val builder = StringBuilder(this.length + insert.length * (this.length / period) + 1)
    var index = 0
    var prefix = ""
    while (index < this.length) {
        builder.append(prefix)
        prefix = insert
        builder.append(this.substring(index, kotlin.math.min(index + period, this.length)))
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

fun String.lineByLineFilter(filter: ((String) -> Boolean)) =  this.split("\n").filter(filter).joinToString(separator = "\n")

fun String.lineByLineTransform(transform: ((String) -> CharSequence)) =  this.split("\n").joinToString(separator = "\n", transform = transform)

fun String.paragraphTransform(transform: ((String) -> CharSequence)) =  this.split("\n\n").joinToString(separator = "\n\n", transform = transform)

fun String.repeatUntilLength(length: Int): String{
    val builder: StringBuilder = StringBuilder(length)
    while (builder.length < length) {
        builder.append(this)
    }
    builder.setLength(length)
    return builder.toString()
}

fun String.padStart(length: Int, padString: String = " "): String {
    if (length < 0) throw IllegalArgumentException("Desired length $length is less than zero.")
    return if (length <= this.length) this
    else padString.repeatUntilLength(length - this.length) + this
}

fun String.padEnd(length: Int, padString: String = " "): String {
    if (length < 0) throw IllegalArgumentException("Desired length $length is less than zero.")
    return if (length <= this.length) this
    else this +  padString.repeatUntilLength(length - this.length)
}