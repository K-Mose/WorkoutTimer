package com.kmose.workouttimer.presentation.screen.home

import android.annotation.SuppressLint
import android.icu.text.UnicodeSet.SpanCondition
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(  // Home에서 BottomSheet으로 바꾸기
//    timerViewmomdel: ViewModel = hiltViewModel()
) {
    val timerType = remember { mutableStateOf(TimerType.COUNTER) }
// https://proandroiddev.com/bottom-sheet-in-jetpack-compose-d7e106422606
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }, // false 시 드래그나 외부 터치로 닫기 안됨
        skipHalfExpanded = true
    )
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            Time(timerType = timerType)
        }
    ) {
        Scaffold(
            floatingActionButton = {
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .border(width = 2.dp, color = Teal500, shape = CircleShape),
                    onClick = {
                        coroutineScope.launch {
                            if (modalSheetState.isVisible)
                                modalSheetState.hide()
                            else
                                modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "timer list")
                }
            },
            content = {
            },
        )
    }
}

@Composable
fun Time(
    timerType: MutableState<TimerType>,
) {
    val isSelectedItem: (TimerType) -> Boolean = { type ->
        type == timerType.value
    }

    val time = remember { mutableStateOf("00") }
    val hours = remember { mutableStateOf(TextFieldValue("")) }
    val minutes = remember { mutableStateOf(TextFieldValue("")) }
    val seconds = remember { mutableStateOf(TextFieldValue("")) }
    var isFocused1 by remember { mutableStateOf(false) }
    var isFocused2 by remember { mutableStateOf(false) }
    var isFocused3 by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = isFocused1, key2 = isFocused2, key3 = isFocused3) {
        if (isFocused1 || isFocused2 || isFocused3) {/*
            hours.value = hours.value.apply {
                copy(selection = TextRange(0, text.length))
            }
            minutes.value = minutes.value.apply {
                copy(selection = TextRange(0, text.length))
            }
            seconds.value = seconds.value.apply {
                copy(selection = TextRange(0, text.length))
            }*/
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Clear TextField Focus
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier
                        .width(TIMER_NUMBER_WIDTH)
                        .onFocusChanged { focusState ->
//                            isFocused1 = focusState.isFocused
                            if (focusState.isFocused)
                                hours.value = hours.value.apply {
                                    copy(selection = TextRange(0, text.length))
                                }
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = hours.value,
                    onValueChange = {
//                        Log.d("MYTAG::",timerValidation(it.text))
                        hours.value = hours.value.copy(timerValidation(it.text))
                    }
                )
                Text(text = ":", fontSize = TIMER_NUMBER_SIZE)
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
                        Log.d("MYTAG::", timerValidation(it.text))
                        minutes.value = TextFieldValue(timerValidation(it.text))
                    }
                )
                Text(text = ":", fontSize = TIMER_NUMBER_SIZE)
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
                        Log.d("MYTAG::", timerValidation(it.text))
                        seconds.value = TextFieldValue(timerValidation(it.text))
                    }
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .border(width = 1.dp, color = Color.LightGray)
        )
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

fun timerValidation(time: String): String =
    time.let {
        if (it.length > 2) {
            it.substring(1, 3).let { it2 ->
                if (it2.substring(0, 1).toInt() > 5)
                    "0$it2"
                else
                    it2
            }
        } else {
            if (it.substring(0, 1).toInt() > 5)
                "0$it"
            else
                it
        }
    }

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    val timerType = remember { mutableStateOf(TimerType.COUNTER) }
    Time(timerType)
}