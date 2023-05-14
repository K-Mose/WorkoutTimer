package com.kmose.workouttimer.navigation.destination

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kmose.workouttimer.util.Constants.TIMER_ID
import com.kmose.workouttimer.util.Constants.TIMER_SCREEN

fun NavGraphBuilder.timerComposable() {
    composable(
        route = TIMER_SCREEN,
        arguments = listOf(navArgument(TIMER_ID) {
            type = NavType.IntType
        })
    ) { navBackstackEntry ->
        val id = navBackstackEntry.arguments?.getInt(TIMER_ID)
        LaunchedEffect(key1 = id) {
            // retrieve saved timer data
        }

    }
}