package com.kmose.workouttimer.util

import java.util.regex.Pattern

object Utils {
    private val pattern = Pattern.compile("\\D")
    fun timerValidation(time: String): String =
        time.let {
            val s = it.toNumber()
            if (s.isEmpty()) "00"
            else if (s.length > 2) {
                s.substring(1, 3).let { it2 ->
                    if (it2.toInt() < 60)
                        it2
                    else {
                        "0${it2.last()}"
                    }
                }
            } else {
                if (s.substring(0, 1).toInt() > 5)
                    "0$s"
                else
                    s
            }
        }

    fun String.toNumber(size: Int = this.length) = this.let {
        if (it.isEmpty()) "0"
        else {
            this.replace(pattern.toRegex(), "0")
                .substring(0, if (size <= this.length) size else this.length)
        }
    }

    fun String.toNumberString(size: Int = this.length) = this.let {
        if (it.isEmpty()) "0"
        else {
            this.replace(pattern.toRegex(), "0").toInt().toString().let { it2 ->
                it2.substring(
                    if (it2.length - size <= 0) 0 else it2.length - size,
                    it2.length
                )
            }
        }
    }
}