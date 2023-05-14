package com.kmose.workouttimer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: TimerType = TimerType.COUNTER,
    val time: Int,
    val preTimer: Int,
    val intervalRounds: Int = 0,
    val restTimes: Int = 0
)
