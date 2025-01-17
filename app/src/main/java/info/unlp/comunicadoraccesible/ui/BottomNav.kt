package info.unlp.comunicadoraccesible.ui

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import info.unlp.comunicadoraccesible.composables.ScalableText
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel
import info.unlp.comunicadoraccesible.data.AppDao
import info.unlp.comunicadoraccesible.data.QuestionsViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object FAQ : Screen("faq", "FAQ", Icons.Default.Info)
    object Teclado : Screen("teclado", "Teclado", Icons.Default.Keyboard)
    object Lenguaje : Screen("lenguaje", "Lenguaje", Icons.Default.CameraAlt)
    object Opciones : Screen("opciones", "Opciones", Icons.Default.Settings)
    object EditQuestions : Screen("editar", "Editar preguntas", Icons.Default.Settings)
}

@Composable
fun BottomNav(appDao: AppDao) {

    val context = LocalContext.current
    val navController = rememberNavController()
    val accessibilityViewModel = AccessibilityViewModel(appDao, context)
    val questionsViewModel = QuestionsViewModel(appDao)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, accessibilityViewModel)
        }
    ) {
        Navigation(navController, it, accessibilityViewModel, questionsViewModel)
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
    NavigationBar(modifier = Modifier.height(75.dp * accessibilityViewModel.buttonSize)) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = {
                    ScalableText(
                        text = item.title,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        accessibilityViewModel
                    )
                },
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
fun Navigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    accesibilityViewModel: AccessibilityViewModel,
    questionsViewModel: QuestionsViewModel
) {

    NavHost(
        navController,
        startDestination = Screen.FAQ.route,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        composable(
            Screen.FAQ.route,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() },
        ) {
            FAQScreen(accesibilityViewModel, questionsViewModel)
        }
        composable(
            Screen.Teclado.route,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() },
        ) {
            KeyboardScreen(accesibilityViewModel)
        }
        composable(
            Screen.Lenguaje.route,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() },
        ) {
            LenguajeScreen()
        }
        composable(
            Screen.Opciones.route,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() },
        ) {
            SettingsScreen(accesibilityViewModel, navController)
        }
        composable(
            Screen.EditQuestions.route,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() },
        ) {
            EditQuestionsScreen(accesibilityViewModel, questionsViewModel, navController)
        }
    }
}


@Composable
fun LenguajeScreen() {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Construction,
            contentDescription = "Página en construcción",
            modifier = Modifier.size(100.dp)
        )
    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
