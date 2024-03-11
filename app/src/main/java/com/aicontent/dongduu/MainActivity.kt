package com.aicontent.dongduu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aicontent.dongduu.presentation.CongruentCalculatorScreen
import com.aicontent.dongduu.presentation.CongruentViewModel
//import com.aicontent.dongduu.presentation.LinearEquationInputScreen
import com.aicontent.dongduu.presentation.MainScreen
import com.aicontent.dongduu.presentation.SettingScreen
import com.aicontent.dongduu.presentation.SolutionStepsScreen
import com.aicontent.dongduu.presentation.SolutionStepsSystemScreen
import com.aicontent.dongduu.presentation.SystemCongruentCalculatorScreen
import com.aicontent.dongduu.presentation.SystemCongruentCalculatorViewModel
//import com.aicontent.dongduu.presentation.SystemCongruentCalculatorScreen
import com.aicontent.dongduu.ui.theme.DongDuuTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: CongruentViewModel = viewModel()
            val viewModelSystem: SystemCongruentCalculatorViewModel = viewModel()

            DongDuuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    MyAppNavHost(
//                        navController = rememberNavController(),
//                        viewModel = viewModel,
//                    )
                    MainActivity(
                        navHostController = rememberNavController(),
                        viewModel = viewModel,
                        viewModelSystem = viewModelSystem
                    )
                }
            }
        }
    }
}

data class NavigationDrawerItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainActivity(
    viewModel: CongruentViewModel,
    viewModelSystem: SystemCongruentCalculatorViewModel,
    navHostController: NavHostController // Add this parameter)
) {
    val items = listOf(

        NavigationDrawerItem(
            title = "All",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = NavigationItemDrawer.Main.route
        ),
        NavigationDrawerItem(
            title = "System Congruent",
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.Star,
            route = NavigationItemDrawer.Second.route
        ),
        NavigationDrawerItem(
            title = "Congruent",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            route = NavigationItemDrawer.Third.route
        ),
        NavigationDrawerItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = NavigationItemDrawer.Four.route
        ),
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            navHostController.navigate(item.route) // Now you can use the navController here
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            content = {
                Spacer(modifier = Modifier.padding(16.dp))
                MyAppNavHostDrawer(
                    navController = navHostController,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize(),
                    viewModelSystem = viewModelSystem,
                )
            }
        )
    }
}

@Composable
fun MyAppNavHostDrawer(
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItemDrawer.Main.route,
    viewModel: CongruentViewModel,
    viewModelSystem: SystemCongruentCalculatorViewModel,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = NavigationItemDrawer.Main.route) {
            MainScreen()
        }

        composable(route = NavigationItemDrawer.Second.route) {
            SystemCongruentCalculatorScreen(
                navController,
                viewModelSystem,
                viewModelSystem.uiState.collectAsState().value,
                uiStateScreen = viewModelSystem.uiStateScreen.value
            )
        }

        composable(route = NavigationItemDrawer.Third.route) {
            CongruentCalculatorScreen(
                navController = navController,
                viewModel = viewModel,
                uiState = viewModel.uiState.collectAsState().value,
                uiStateScreen = viewModel.uiStateScreen.value
            )
        }

        composable(route = NavigationItemDrawer.Four.route) {
            SettingScreen()
        }

        composable(route = NavigationItemDrawer.Fifth.route) {
            SolutionStepsScreen(
                navController = navController,
                viewModel = viewModel,
                uiState = viewModel.uiState.collectAsState().value,
            )
        }

        composable(route = NavigationItemDrawer.Six.route) {
            SolutionStepsSystemScreen(
                uiState = viewModelSystem.uiState.collectAsState().value,
                navController
            )
        }
    }
}

enum class ScreenDrawer {
    MAIN,
    SECOND,
    THIRD,
    FOUR,
    FIFTH,
    SIX
}

sealed class NavigationItemDrawer(val route: String) {
    data object Main : NavigationItemDrawer(ScreenDrawer.MAIN.name)
    data object Second : NavigationItemDrawer(ScreenDrawer.SECOND.name)
    data object Third : NavigationItemDrawer(ScreenDrawer.THIRD.name)
    data object Four : NavigationItemDrawer(ScreenDrawer.FOUR.name)
    data object Fifth : NavigationItemDrawer(ScreenDrawer.FIFTH.name)
    data object Six : NavigationItemDrawer(ScreenDrawer.SIX.name)
}
