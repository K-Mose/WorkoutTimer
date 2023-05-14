package com.kmose.workouttimer.presentation.screen.home

import android.annotation.SuppressLint
import android.icu.text.UnicodeSet.SpanCondition
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.kmose.workouttimer.data.TimerType
import com.kmose.workouttimer.ui.theme.Teal500
import com.kmose.workouttimer.ui.theme.Teal700
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_SIZE
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_WIDTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.selects.select
import java.util.concurrent.TimeUnit

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
//    timerViewmomdel: ViewModel = hiltViewModel()
) {
    val timerType = remember { mutableStateOf(TimerType.COUNTER)}

    Scaffold(
        floatingActionButton = {
            IconButton(
                modifier = Modifier
                    .size(50.dp)
                    .border(width = 2.dp, color = Teal500, shape = CircleShape),
                onClick = { /*TODO*/ },
            ) {
                Icon(imageVector = Icons.Default.List, contentDescription = "timer list")
            }
        },
        content = {
            Time(timerType)
        },
    )
}

@Composable
fun Time(
    timerType: MutableState<TimerType>,
) {
    val isSelectedItem: (TimerType) -> Boolean =  { type ->
        type == timerType.value
    }

    val time = remember { mutableStateOf("00") }
    val hours = remember { mutableStateOf(TextFieldValue("00")) }
    val minutes = remember { mutableStateOf(TextFieldValue("00")) }
    val seconds = remember { mutableStateOf(TextFieldValue("00")) }
    var isFocused1 by remember { mutableStateOf(false) }
    var isFocused2 by remember { mutableStateOf(false) }
    var isFocused3 by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = isFocused1, key2 = isFocused2, key3 = isFocused3) {
        if (isFocused1 || isFocused2 || isFocused3) {

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimerType.values().forEach { type ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = isSelectedItem(type),
                            role = Role.RadioButton,
                            onClick = { timerType.value = type }
                        )
                    ) {
                        RadioButton(
                            selected = isSelectedItem(type),
                            onClick = null
                        )
                        Text(text = type.name)
                    }
                }
            }
            // Time
            Row(
                modifier =  Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier
                        .width(TIMER_NUMBER_WIDTH)
                        .onFocusChanged { focusState ->
                            isFocused1 = focusState.isFocused
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = hours.value,
                    onValueChange = {
                        hours.value = it
                    }
                )
                Text(text = ":", fontSize = TIMER_NUMBER_SIZE,)
                BasicTextField(
                    modifier = Modifier
                        .width(TIMER_NUMBER_WIDTH)
                        .onFocusChanged { focusState ->
                            isFocused2 = focusState.isFocused
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = minutes.value,
                    onValueChange = {
                        minutes.value = it
                    }
                )
                Text(text = ":", fontSize = TIMER_NUMBER_SIZE,)
                BasicTextField(
                    modifier = Modifier
                        .width(TIMER_NUMBER_WIDTH)
                        .onFocusChanged { focusState ->
                            isFocused3 = focusState.isFocused
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = seconds.value,
                    onValueChange = {
                        seconds.value = it
                    }
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .border(width = 1.dp, color = Color.LightGray))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {},
                content = {
                    Text(text = "Start")
                }
            )
        }
    }
}

fun timerValidation(time: String) {

}

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    val timerType = remember { mutableStateOf(TimerType.COUNTER)}
    Time(timerType)
}