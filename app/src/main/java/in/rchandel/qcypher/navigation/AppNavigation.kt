package `in`.rchandel.qcypher.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.rchandel.qcypher.ui.screens.result.ScanResultScreen
import `in`.rchandel.qcypher.ui.screens.scanner.ScannerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SCANNER) {
        composable(Routes.SCANNER) {
            ScannerScreen(onScanSuccess = {scannedText ->
                navController.navigate("${Routes.RESULT}/$scannedText")
            })
        }

        composable(
            route = "${Routes.RESULT}/{content}",
                arguments = listOf(navArgument("content") {type = NavType.StringType})
            ) {navBackStackEntry ->
            val content = navBackStackEntry.arguments?.getString("content") ?: ""
            ScanResultScreen(scannedText = content)
        }
    }

}