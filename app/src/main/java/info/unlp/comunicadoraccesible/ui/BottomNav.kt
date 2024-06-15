package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object FAQ : Screen("faq", "FAQ", Icons.Default.Info)
    object Teclado : Screen("teclado", "Teclado", Icons.Default.Keyboard)
    object Lenguaje : Screen("lenguaje", "Lenguaje", Icons.Default.Mic)
    object Opciones : Screen("opciones", "Opciones", Icons.Default.Settings)
}

@Composable
fun BottomNav() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Navigation(navController, it)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.FAQ,
        Screen.Teclado,
        Screen.Lenguaje,
        Screen.Opciones
    )
    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
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
fun Navigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = Screen.FAQ.route, modifier = Modifier.padding(paddingValues) ) {
        composable(Screen.FAQ.route) {
            FAQScreen()
        }
        composable(Screen.Teclado.route) {
            TecladoScreen()
        }
        composable(Screen.Lenguaje.route) {
            LenguajeScreen()
        }
        composable(Screen.Opciones.route) {
            OpcionesScreen()
        }
    }
}

@Composable
fun FAQScreen() {
    // Your FAQ screen UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "FAQ Screen")
    }
}

@Composable
fun TecladoScreen() {
    // Your Teclado screen UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Teclado Screen")
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
fun OpcionesScreen() {
    // Your Opciones screen UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Opciones Screen")
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
