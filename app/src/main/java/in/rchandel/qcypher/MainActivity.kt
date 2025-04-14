package `in`.rchandel.qcypher

import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.rchandel.qcypher.navigation.AppNavigation
import `in`.rchandel.qcypher.ui.theme.QCypherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QCypherTheme {
                AppNavigation()
            }
        }
    }
}
