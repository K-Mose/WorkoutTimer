package com.kmose.workouttimer.presentation.screen.home

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.kmose.workouttimer.util.Utils.timerValidation
import com.kmose.workouttimer.util.Utils.toNumberString
import com.kmose.workouttimer.util.dimens.CONTENT_PADDING_MEDIUM
import com.kmose.workouttimer.util.dimens.CONTENT_PADDING_XLARGE
import com.kmose.workouttimer.util.dimens.CORNER_RADIOUS_MEDIUM
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_SIZE
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_WIDTH
import com.kmose.workouttimer.util.dimens.TIMER_NUMBER_WIDTH_MEDIUM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    var isOverlay by remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = CORNER_RADIOUS_MEDIUM,
            topEnd = CORNER_RADIOUS_MEDIUM
        ),
        sheetContent = {
            Time(timerType = timerType)
        }
    ) {
        Box {
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

            // 기존 framelayout 역할
            if (isOverlay)
                Surface(
                    color = Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxSize()
                        .clickable {
                            isOverlay = false
                        }
                ) {

                }
        }
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
    var preTime = remember { mutableStateOf(TextFieldValue(text = zero.toNumberString(1), selection = TextRange(zero.length)))}

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
//                .weight(8f)
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
                        .width(TIMER_NUMBER_WIDTH),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = hours.value,
                    onValueChange = {
                        Log.d("MYTAG::",timerValidation(it.text))
                        hours.value = TextFieldValue(
                            text = timerValidation(it.text),
                            selection = TextRange(2)
                        )
                    }
                )
                Text(text = ":", fontSize = TIMER_NUMBER_SIZE)
                BasicTextField(
                    modifier = Modifier
                        .width(TIMER_NUMBER_WIDTH),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = minutes.value,
                    onValueChange = {
                        minutes.value = TextFieldValue(
                            text = timerValidation(it.text),
                            selection = TextRange(2)
                        )
                    },
                    onTextLayout = { textLayoutResult ->
                        textLayoutResult
                    }
                )
                Text(text = ":", fontSize = TIMER_NUMBER_SIZE)
                BasicTextField(
                    modifier = Modifier
                        .width(TIMER_NUMBER_WIDTH),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TIMER_NUMBER_SIZE,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    value = seconds.value,
                    onValueChange = {
                        seconds.value = TextFieldValue(
                            text = timerValidation(it.text),
                            selection = TextRange(2)
                        )
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = CONTENT_PADDING_MEDIUM, horizontal = CONTENT_PADDING_XLARGE
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Pre-Time :")
            BasicTextField(
                modifier = Modifier
                    .padding(start = CONTENT_PADDING_MEDIUM)
                    .width(TIMER_NUMBER_WIDTH_MEDIUM),
                textStyle = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Right),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                value = preTime.value,
                onValueChange = {
                    preTime.value = TextFieldValue(
                        text = it.text.toNumberString(2),
                        selection = TextRange(it.text.length+1)
                    )
                },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    text = "sec",
                    style = MaterialTheme.typography.caption
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
//                .weight(1f)
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {},
                content = {
                    Text(text = "SAVE")
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

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    val timerType = remember { mutableStateOf(TimerType.COUNTER) }
    Time(timerType)
}