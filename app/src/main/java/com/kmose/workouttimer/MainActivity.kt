package com.kmose.workouttimer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.kmose.workouttimer.navigation.SetupNavigation
import com.kmose.workouttimer.ui.theme.WorkoutTimerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    var close = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTimerTheme {
                val navController = rememberNavController()
                SetupNavigation(navController = navController)
            }
        }
    }

    override fun onBackPressed() {
        if (close) {
            super.onBackPressed()
            return
        }
        else {
            close = true
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO) {
                Thread.sleep(2000)
                Log.d("MYTAG", "CLOSE FALSE")
                close = false
            }
        }
    }
}