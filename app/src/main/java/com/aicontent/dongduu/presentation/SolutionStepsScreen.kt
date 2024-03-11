package com.aicontent.dongduu.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aicontent.dongduu.R


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SolutionStepsScreen(
    navController: NavHostController,
    viewModel: CongruentViewModel,
    uiState: CongruentUiState
) {
    val mediumPadding = 16.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Main Screen")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.resetToInitialValues()
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Add"
                        )
                    }
                }
            )
        }
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = mediumPadding)
                )

                if (viewModel.checkCondition()) {
                    Text(
                        text = "Các Bước thực hiện:",
                        fontSize = 20.sp,
                        style = typography.displaySmall,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Start)
                    )
                    SolutionStepCard("Xét Điều kiện: \n${uiState.initialA} != 0 và UCLN của ${uiState.initialA},${uiState.initialM} là ước của ${uiState.initialB}")
                    if (uiState.k > 0) {
                        Column {
                            SolutionStepCard("(${uiState.initialA},${uiState.initialM}) = ${uiState.d} và ${uiState.d} là ước của ${uiState.initialB} \n-> Phương trình có ${uiState.d} no ")
                            SolutionStepCard("Ta đưa về dạng ${uiState.a}x ≡ ${uiState.b}(mod ${uiState.m}) \n-> ${uiState.a}x ≡ ${uiState.b}(mod ${uiState.m}) ")
                            SolutionStepCard("Vậy phương trình có các nghiệm là:")
                            for (i in 0 until uiState.d) {
                                Text(
                                    text = "x ≡ ${uiState.x + i * uiState.m.toInt()}mod(${uiState.initialM.toInt()})",
                                    fontSize = 20.sp,
                                    style = typography.displaySmall,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    } else {
                        SolutionStepCard("(${uiState.initialA},${uiState.initialM}) = 1 ->Phương trình có no duy nhất")
                        if (viewModel.isFactor(uiState.b.toLong(), uiState.a.toLong())) {
                            SolutionStepCard("${uiState.initialA} là Ước của ${uiState.initialB} nên x ≡ ${uiState.initialB}/${uiState.initialA}")
                        } else SolutionStepCard("${uiState.initialA} không là ước của ${uiState.initialB} nên luôn tồn tại một số k sao cho\n1 <= k <= ${uiState.initialA}-1")
                        SolutionStepCard("-> x ≡  (${uiState.b} + k.${uiState.m})/${uiState.a} ≡  (${uiState.b} + ${viewModel.kali}.${uiState.m})/${uiState.a}")
                        SolutionStepCard("Vậy phương trinh có nghiệm là")
                        Text(
                            text = "x ≡ ${uiState.x}mod(${uiState.initialM.toInt()})",
                            fontSize = 20.sp,
                            style = typography.displaySmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Start)
                        )
                    }

                } else {
                    Text(
                        text = "Phương trình vô nghiệm",
                        fontSize = 24.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
//                    viewModel.restartScreen()
                    viewModel.resetToInitialValues()
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
}

@Composable
fun SolutionStepCard(step: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = step,
                fontSize = 18.sp,
                style = typography.displaySmall
            )
        }
    }
}