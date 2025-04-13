package `in`.rchandel.qcypher.navigation

import android.net.Uri
import android.util.Log
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
import `in`.rchandel.qcypher.utils.toJson
import `in`.rchandel.qcypher.utils.toQRResult

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SCANNER) {
        composable(Routes.SCANNER) {
            ScannerScreen(onScanSuccess = {qrResult ->
                Log.d("THE_DEBUG", "scan result called")
                navController.navigate("${Routes.RESULT}/${Uri.encode(qrResult.toJson())}")
            })
        }

        composable(
            route = "${Routes.RESULT}/{qrJson}",
                arguments = listOf(navArgument("qrJson") {type = NavType.StringType})
            ) {navBackStackEntry ->
            val qrJson = navBackStackEntry.arguments?.getString("qrJson") ?: ""
            val qrResult = qrJson.toQRResult()
            ScanResultScreen(navController = navController, qrResult = qrResult)
        }
    }

}