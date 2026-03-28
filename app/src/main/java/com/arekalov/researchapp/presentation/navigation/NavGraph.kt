package com.arekalov.researchapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arekalov.researchapp.presentation.details.ProductDetailsScreen
import com.arekalov.researchapp.presentation.products.ProductsScreen
import com.arekalov.researchapp.presentation.settings.SettingsScreen
import com.arekalov.researchapp.ui.theme.ResearchappTheme

sealed class Screen(val route: String) {
    data object Products : Screen("products")
    data object Settings : Screen("settings")
    data object ProductDetails : Screen("product_details/{productId}") {
        fun createRoute(productId: Int) = "product_details/$productId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Products.route
    ) {
        composable(Screen.Products.route) {
            ProductsScreen(
                onNavigateToDetails = { productId ->
                    navController.navigate(Screen.ProductDetails.createRoute(productId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.ProductDetails.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            ProductDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "NavHost (без Hilt)")
@Composable
private fun NavGraphStubPreview() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "preview_stub"
        ) {
            composable("preview_stub") {
                Surface(Modifier.fillMaxSize()) {
                    Text(
                        text = "Превью графа навигации.\nПолный NavGraph с экранами требует Hilt — смотрите превью ProductsScreen, Settings, ProductDetails.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
