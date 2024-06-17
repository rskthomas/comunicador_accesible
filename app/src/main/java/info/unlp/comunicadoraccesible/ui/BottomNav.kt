package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import info.unlp.comunicadoraccesible.AccessibilityViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object FAQ : Screen("faq", "FAQ", Icons.Default.Info)
    object Teclado : Screen("teclado", "Teclado", Icons.Default.Keyboard)
    object Lenguaje : Screen("lenguaje", "Lenguaje", Icons.Default.Mic)
    object Opciones : Screen("opciones", "Opciones", Icons.Default.Settings)
}

@Composable
fun BottomNav() {
    val navController = rememberNavController()
    val accessibilityViewModel = AccessibilityViewModel()
    val questionsViewModel = QuestionsViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        accessibilityViewModel.initializeTextToSpeech( context)
    }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, accessibilityViewModel, )
        }
    ) {
        Navigation(navController, it, accessibilityViewModel,questionsViewModel)
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    accessibilityViewModel: AccessibilityViewModel
) {
    val items = listOf(
        Screen.FAQ,
        Screen.Teclado,
        Screen.Lenguaje,
        Screen.Opciones
    )
    NavigationBar(modifier = Modifier.height(65.dp * accessibilityViewModel.buttonSize)) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { ScalableText(text = item.title, textStyle = MaterialTheme.typography.bodyMedium, accessibilityViewModel)},
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, paddingValues: PaddingValues, accesibilityViewModel: AccessibilityViewModel, questionsViewModel: QuestionsViewModel ) {
    NavHost(navController, startDestination = Screen.FAQ.route, modifier = Modifier.padding(paddingValues) ) {
        composable(Screen.FAQ.route) {
            FAQScreen(accesibilityViewModel, questionsViewModel)
        }
        composable(Screen.Teclado.route) {
            KeyboardScreen(accesibilityViewModel)
        }
        composable(Screen.Lenguaje.route) {
            LenguajeScreen()
        }
        composable(Screen.Opciones.route) {
            SettingsScreen(accesibilityViewModel)
        }
    }
}



@Composable
fun LenguajeScreen() {
    // Your Lenguaje screen UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Lenguaje Screen")
    }
}



@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
