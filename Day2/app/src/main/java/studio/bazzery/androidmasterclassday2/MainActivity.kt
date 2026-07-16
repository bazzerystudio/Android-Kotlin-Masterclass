package studio.bazzery.androidmasterclassday2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import studio.bazzery.androidmasterclassday2.ui.screens.ConsoleSimulatorScreen
import studio.bazzery.androidmasterclassday2.ui.screens.KotlinSandboxScreen
import studio.bazzery.androidmasterclassday2.ui.screens.RpsGameScreen
import studio.bazzery.androidmasterclassday2.ui.theme.AndroidMasterClassDay2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMasterClassDay2Theme {
                MainAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Android Master Class - Day 2") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("เป่ายิ้งฉุบ (RPS)") },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Play RPS Game") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("คอนโซล (Console)") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Console Simulator") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("สนามเด็กเล่น (Sandbox)") },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Kotlin Sandbox") }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTab) {
                0 -> RpsGameScreen()
                1 -> ConsoleSimulatorScreen()
                2 -> KotlinSandboxScreen()
            }
        }
    }
}