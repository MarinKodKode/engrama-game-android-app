package com.manu.kode.engrama.features.math.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.manu.kode.engrama.navigation.AppRoute

@Composable
fun SummaryMathScreen(
    score: Int,
    navController: NavHostController
) {
    val resultPhrase = when (score) {
        in 0..5   -> "¿Seguro que querías jugar matemáticas?"
        in 6..10  -> "¡Puedes hacerlo mejor!"
        in 11..20 -> "¡Nada mal! Sigue practicando."
        in 21..35 -> "¡Muy bien! Tienes buen ritmo."
        else      -> "¡Eres una máquina! 🔥"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00BCD4),
                        Color(0xFF1976D2)
                    )
                )
            )
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Trofeo
            Text(
                text = "🏆",
                fontSize = 100.sp
            )

            // Score
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$score",
                    fontSize = 80.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Puntaje",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = resultPhrase,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 36.dp)
                )
            }

            // Botón
            Button(
                onClick = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF5350)
                )
            ) {
                Text(
                    text = "Jugar de nuevo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}