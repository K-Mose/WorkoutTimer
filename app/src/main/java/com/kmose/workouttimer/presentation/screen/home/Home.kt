package com.kmose.workouttimer.presentation.screen.home

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kmose.workouttimer.data.TimerType
import com.kmose.workouttimer.ui.theme.Teal500
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_SIZE
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_WIDTH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

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
                OnBackPressed(modalSheetState, coroutineScope)
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

    val zero = "00"
    val hours = remember { mutableStateOf(TextFieldValue(text = zero, selection = TextRange(zero.length)))}
    val minutes = remember { mutableStateOf(TextFieldValue(text = zero, selection = TextRange(zero.length)))}
    val seconds = remember { mutableStateOf(TextFieldValue(text = zero, selection = TextRange(zero.length)))}
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
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = hours.value,
                    onValueChange = {
                        Log.d("MYTAG::",timerValidation(it.text))
                        hours.value = TextFieldValue(text = timerValidation(it.text), selection = TextRange(2))
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
//                        Log.d("MYTAG::", timerValidation(it))
//                        minutes.value = timerValidation(it)
                    },
                    onTextLayout = { textLayoutResult ->
                        textLayoutResult
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
//                        Log.d("MYTAG::", timerValidation(it))
//                        seconds.value = timerValidation(it)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnBackPressed(
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var close by remember { mutableStateOf(false) }
    // back button으로 모달 닫기
    BackHandler(enabled = modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }
    // onBackPressed
    BackHandler(enabled = !modalSheetState.isVisible) {
        if (close) {
            activity.finish()
        }
        else {
            close = true
            Toast.makeText(context, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            coroutineScope.launch(Dispatchers.IO) {
                Thread.sleep(2000)
                Log.d("MYTAG", "CLOSE FALSE")
                close = false
            }
        }
    }
}

fun timerValidation(time: String): String =
    time.let {
        val pattern = Pattern.compile("\\D")
        val s = it.replace(pattern.toRegex(), "0")
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

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    val timerType = remember { mutableStateOf(TimerType.COUNTER) }
    Time(timerType)
}