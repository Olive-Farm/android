package com.farmer.home.util

internal val String.Companion.EMPTY: String get() = ""

internal fun Int.toCommaString(): String =
    this.toString().reversed().chunked(3).joinToString(",").reversed()