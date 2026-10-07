package com.manu.kode.engrama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.manu.kode.engrama.ui.theme.EngramaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            EngramaTheme {
                // Contenido mínimo del Checkpoint 1; la navegación con Home llega en el Checkpoint 2.
                Surface(modifier = Modifier.fillMaxSize()) {}
            }
        }
    }
}
