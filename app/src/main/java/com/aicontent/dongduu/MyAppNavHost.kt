package com.aicontent.dongduu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aicontent.dongduu.presentation.CongruentCalculatorScreen
import com.aicontent.dongduu.presentation.CongruentViewModel
import com.aicontent.dongduu.presentation.SolutionStepsScreen
//import com.aicontent.dongduu.presentation.SystemCongruentCalculatorScreen

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItem.Main.route,
    viewModel: CongruentViewModel,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = NavigationItem.Main.route) {
            CongruentCalculatorScreen(
                navController = navController,
                viewModel = viewModel,
                uiState = viewModel.uiState.collectAsState().value,
                uiStateScreen = viewModel.uiStateScreen.value
            )
        }
        composable(route = NavigationItem.Second.route) {
            SolutionStepsScreen(
                navController = navController,
                viewModel = viewModel,
                uiState = viewModel.uiState.collectAsState().value,
            )
        }
    }
}


enum class Screen {
    MAIN,
    SECOND,
    THIRD
}

sealed class NavigationItem(val route: String) {
    data object Main : NavigationItem(Screen.MAIN.name)
    data object Second : NavigationItem(Screen.SECOND.name)
    data object Third : NavigationItem(Screen.THIRD.name)
}
