package com.kmose.workouttimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kmose.workouttimer.navigation.destination.homeComposable
import com.kmose.workouttimer.navigation.destination.listComposable
import com.kmose.workouttimer.navigation.destination.timerComposable
import com.kmose.workouttimer.util.Constants.HOME_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    val screens = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN
    ) {
        homeComposable()
        listComposable()
        timerComposable()
    }
}