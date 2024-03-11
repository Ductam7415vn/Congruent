package com.aicontent.dongduu.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aicontent.dongduu.NavigationItemDrawer
import com.aicontent.dongduu.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SystemCongruentCalculatorScreen(
    navController: NavController,
    viewModel: SystemCongruentCalculatorViewModel,
    uiState: SystemCongruentUiState,
    uiStateScreen: UiStateScreen<SystemCongruentUiState>
) {
    val bool by viewModel.bool.collectAsState()
    val boolState by viewModel.boolState.collectAsState()

    var state by remember { mutableStateOf(false) }

    when (uiStateScreen) {
        is UiStateScreen.Loading -> {
            LoadingScreen()
        }

        is UiStateScreen.Error -> {
            ErrorScreen(errorMessage = uiStateScreen.message) {
                state = !state
            }
        }

        is UiStateScreen.Success -> {
            SuccessScreen(
                uiState = uiState,
                viewModel = viewModel,
                navController = navController,
                bool = bool,
                boolState = boolState,
                state = state,
                isTextNull = {
                    state = !state
                }
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun ErrorScreen(errorMessage: String, onDismiss: () -> Unit) {
    MinimalDialog(
        { onDismiss() },
        errorMessage,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SuccessScreen(
    navController: NavController,
    viewModel: SystemCongruentCalculatorViewModel,
    uiState: SystemCongruentUiState,
    mediumPadding: Dp = 16.dp,
    isTextNull: () -> Unit,
    bool: Boolean,
    boolState: Boolean,
    state: Boolean
) {
    var stateIsEmpty by remember {
        mutableStateOf(false)
    }
    var stateIHate by remember {
        mutableStateOf(false)
    }
    var stateFaile by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
        },
        content = {
            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding()
                    .padding(mediumPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(mediumPadding))
                Spacer(modifier = Modifier.height(mediumPadding))
                Spacer(modifier = Modifier.height(mediumPadding))

                Text(
                    text = "Hệ phương trình đồng dư một ẩn",
                    style = MaterialTheme.typography.titleLarge,
                )
                SystemCongruentLayout(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(mediumPadding)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(mediumPadding),
                    verticalArrangement = Arrangement.spacedBy(mediumPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (uiState.equations.size == 1) {
                                    stateIHate = !stateIHate
                                } else {
                                    if (uiState.equations.any { equation ->
                                            equation.b.isEmpty() ||
                                                    equation.m.isEmpty()
                                        }) {
                                        stateIsEmpty = !stateIsEmpty
                                        viewModel.clearText()
                                    } else {
                                        val isAnyInputTooLargeInEquations =
                                            uiState.equations.any { equation ->
                                                viewModel.isInputTooLarge(equation.b.toLong()) ||
                                                        viewModel.isInputTooLarge(equation.m.toLong())
                                            }
                                        if (isAnyInputTooLargeInEquations) {
                                            viewModel.clearText()
                                        } else {
                                            val isAnyInputNotIntegerInEquations =
                                                uiState.equations.any { equation ->
                                                    viewModel.isInputIsNotInteger(equation.b.toLong()) ||
                                                            viewModel.isInputIsNotInteger(equation.m.toLong())
                                                }
                                            if (isAnyInputNotIntegerInEquations) {
                                                viewModel.clearText()
                                            } else {
                                                if (viewModel.checkConditionIllegal()) {
                                                    viewModel.chineseRemainderTheorem()
                                                    navController.navigate(NavigationItemDrawer.Six.route)
                                                }else{
                                                    stateFaile = !stateFaile
                                                }
                                            }
                                        }
                                    }
                                }
//                                if (viewModel.handleOnClick()) {
//                                    navController.navigate(NavigationItemDrawer.Six.route)
//                                } else {
//
                            }
                        ) {
                            Text(
                                text = "submit",
                                fontSize = 16.sp
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.clearText()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Clear",
                            fontSize = 16.sp
                        )
                    }
                }
                if (stateFaile) {
                    MinimalDialog(
                        onDismissRequest = {
                            stateFaile = !stateFaile
                        },
                        "@ell, I can't calculate this system for you, so ri"
                    )
                }
                if (stateIHate) {
                    MinimalDialog(
                        onDismissRequest = {
//                            viewModel.handleStateIHateIt()
                            stateIHate = !stateIHate
                            navController.navigate(NavigationItemDrawer.Third.route)
                        },
                        "Dude!!! why people cứ make it complicated thế nhở, sống đơn giản cho đời thanh thản đi nào các cậu =))"
                    )
                }
                if (stateIsEmpty) {
                    MinimalDialog(
                        onDismissRequest = {
                            stateIsEmpty = !stateIsEmpty
                        },
                        "Hey, you've got some empty text fields to fill in, dude"
                    )
                }
                if (state) {
                    MinimalDialog(
                        onDismissRequest = { isTextNull() },
                        "Hmmmm bro! Are you confirm all the text field (''/)"
                    )
                }
                if (bool) {
                    MinimalDialog(
                        onDismissRequest = {
                            viewModel.checkState()
                        },
                        "YOOO BROOOO your math problem don't require you to enter a lot of number like that right :V",
                    )
                }
                if (boolState) {
                    MinimalDialog(
                        onDismissRequest = { viewModel.checkBoolState() },
                        "Hmmmm bro! Are you troll me now, you are learning 'Toán chuyên đề' and you teacher don't tell you we just can use integer for calculation",
                    )
                }
            }
        }
    )
}

@Composable
fun SystemCongruentLayout(
    uiState: SystemCongruentUiState,
    viewModel: SystemCongruentCalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val mediumPadding = 8.dp
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(mediumPadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(mediumPadding))


            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = "Ax ≡ B(modM)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(mediumPadding))

            Text(
                text = "Nhập lần lượt b và m",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(mediumPadding))

            uiState.equations.forEachIndexed { index, equation ->
                LinearCongruenceEquationRow(
                    equation = equation,
                    onEquationDelete = {
                        viewModel.deleteEquation(equation)
                    },
                    index,
                    viewModel
                )
            }
            Spacer(modifier = Modifier.height(mediumPadding))

            Button(
                onClick = {
                    viewModel.addEquation(LinearCongruenceEquation())
                },
//                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add another congruent")
            }
            Spacer(modifier = Modifier.height(mediumPadding))

        }
    }
}

@Composable
fun LinearCongruenceEquationRow(
    equation: LinearCongruenceEquation,
    onEquationDelete: () -> Unit,
    index: Int,
    viewModel: SystemCongruentCalculatorViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "x ≡",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(0.9f)
                .padding(4.dp)
        )
        LinearCongruenceInputField(
            value = equation.b,
            onValueChange = { newValue ->
                viewModel.onTextBChange(b = newValue, index)
            },
            label = "B",
            modifier = Modifier
                .weight(2f)
                .padding(4.dp),
            imeAction = ImeAction.Next
        )
        Text(
            text = "(mod",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1.1f)
                .padding(4.dp)
        )
        LinearCongruenceInputField(
            value = equation.m,
            onValueChange = { newValue ->
                viewModel.onTextMChange(m = newValue, index)
            },
            label = "M",
            modifier = Modifier
                .weight(2f)
                .padding(4.dp),
            imeAction = ImeAction.Done
        )
        Text(
            text = ")",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(0.5f)
                .padding(4.dp)
        )
        IconButton(
            onClick = onEquationDelete,
            modifier = Modifier
                .weight(0.5f)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Equation"
            )
        }
    }

}

@Composable
fun LinearCongruenceInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
    )
}

@Composable
fun SolutionStepsSystemScreen(
    uiState: SystemCongruentUiState,
    navController: NavController
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomText(a = "", b = uiState.b, m = uiState.m)
        }

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Back", fontSize = 16.sp)
        }
    }
}
