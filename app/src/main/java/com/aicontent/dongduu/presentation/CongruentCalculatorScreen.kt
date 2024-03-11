package com.aicontent.dongduu.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CongruentCalculatorScreen(
    navController: NavController,
    viewModel: CongruentViewModel,
    uiState: CongruentUiState,
    uiStateScreen: UiStateScreen<CongruentUiState>
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessScreen(
    navController: NavController,
    viewModel: CongruentViewModel,
    uiState: CongruentUiState,
    mediumPadding: Dp = 16.dp,
    bool: Boolean,
    boolState: Boolean,
) {
    var state by remember { mutableStateOf(false) }

    Scaffold(
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

                Text(
                    text = "Phương trình đồng dư một ẩn",
                    style = typography.titleLarge,
                )
                CongruentLayout(
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

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (uiState.a.isEmpty() || uiState.b.isEmpty() || uiState.b.isEmpty()) {
                                state = !state
                            } else {
                                if (viewModel.isInputTooLarge(uiState.a.toLong()) || viewModel.isInputTooLarge(
                                        uiState.b.toLong()
                                    ) || viewModel.isInputTooLarge(uiState.m.toLong())
                                ) {
                                    viewModel.clearText()
                                } else if (viewModel.isInputIsNotInteger(uiState.a.toLong()) || viewModel.isInputIsNotInteger(
                                        uiState.b.toLong()
                                    ) || viewModel.isInputIsNotInteger(uiState.m.toLong())
                                ) {
                                    viewModel.clearText()
                                } else {
                                    navController.navigate(NavigationItemDrawer.Fifth.route)
                                    viewModel.restartScreenAndNavigate()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "submit",
                            fontSize = 16.sp
                        )
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


                if (state) {
                    MinimalDialog(
                        onDismissRequest = { state = !state },
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
fun CongruentLayout(
    uiState: CongruentUiState,
    viewModel: CongruentViewModel,
    modifier: Modifier = Modifier
) {
    val mediumPadding = 16.dp

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = "Ax ≡ B(modM)",
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )
            CustomText(
                a = uiState.a,
                b = uiState.b,
                m = uiState.m
            )
            Text(
                text = "Nhập lần lượt a, b và m",
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
            OutlinedTextField(
                value = uiState.a,
                onValueChange = { viewModel.onTextAChange(it) },
                label = { Text("Enter coefficient A") },
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = uiState.b,
                onValueChange = { viewModel.onTextBChange(it) },
                label = { Text("Enter coefficient B") },
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = uiState.m,
                onValueChange = { viewModel.onTextMChange(it) },
                label = { Text("Enter  modules M") },
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }
    }
}

@Composable
fun CustomText(
    a: String,
    b: String,
    m: String
) {
    val longestValueLength = listOf(a, b, m).maxByOrNull { it.length }?.length ?: 0
    val fontSize = when {
        longestValueLength <= 6 -> 28.sp
        longestValueLength <= 9 -> 24.sp
        else -> 20.sp
    }
    Text(
        text = "$a x ≡ $b (mod $m)",
        style = typography.displaySmall.copy(fontSize = fontSize),
    )
}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit, text: String) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Hope you notice") },
        text = { Text(text = text) },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "OK")
            }
        }
    )
}
