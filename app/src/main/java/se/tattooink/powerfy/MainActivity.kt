package se.tattooink.powerfy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import se.tattooink.powerfy.navigation.PowerfyNavGraph
import se.tattooink.powerfy.ui.theme.PowerfyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PowerfyTheme {
                PowerfyNavGraph()
            }
        }
    }
}
