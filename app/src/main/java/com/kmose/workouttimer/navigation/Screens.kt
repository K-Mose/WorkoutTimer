package com.kmose.workouttimer.navigation

import androidx.navigation.NavHostController

class Screens(navController: NavHostController) {
    val home: (Unit) -> Unit =  {
        navController.navigate(route = "")
    }
    val list: (Unit) -> Unit = {

    }
    val timer: (Int) -> Unit = {

    }
}