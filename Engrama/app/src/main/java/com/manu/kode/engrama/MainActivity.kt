package com.manu.kode.engrama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.manu.kode.engrama.navigation.EngramaNavHost
import com.manu.kode.engrama.ui.theme.EngramaTheme
import com.manu.kode.engrama.ui.theme.isDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appearance by viewModel.appearance.collectAsStateWithLifecycle()
            val dark = appearance.isDark()

            // Iconos de las barras del sistema según la apariencia del tema, no la del sistema.
            DisposableEffect(dark) {
                val transparent = Color.Transparent.toArgb()
                val style = if (dark) {
                    SystemBarStyle.dark(transparent)
                } else {
                    SystemBarStyle.light(transparent, transparent)
                }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                onDispose {}
            }

            EngramaTheme(appearance = appearance) {
                // Fondo y color de contenido del tema: la ventana usa Theme.Material.Light.
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    EngramaNavHost(navController = navController)
                }
            }
        }
    }
}
