package com.manu.kode.engrama.features.trivia.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manu.kode.engrama.features.math.ui.TimerRingView
import com.manu.kode.engrama.features.trivia.viewmodel.TriviaButtonState
import com.manu.kode.engrama.features.trivia.viewmodel.TriviaViewModel
import com.manu.kode.engrama.navigation.AppRoute

@Composable
fun TriviaChallengeScreen(
    navController: NavHostController,
    viewModel: TriviaViewModel = hiltViewModel()
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val score by viewModel.score.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val wrongSelection by viewModel.wrongSelection.collectAsState()
    val bounceCorrect by viewModel.bounceCorrect.collectAsState()

    LaunchedEffect(Unit) { viewModel.init() }

    LaunchedEffect(gameOver) {
        if (gameOver) {
            navController.navigate(
                AppRoute.SummaryTrivia.createRoute(viewModel.score.value)
            ) {
                popUpTo(AppRoute.Home.route)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Score: $score",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF9C27B0)
                )
                TimerRingView(
                    progress = viewModel.progress,
                    timeLeft = timeLeft
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pregunta
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                currentQuestion?.let { question ->
                    // Badge categoría
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFF9800).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = question.category.replaceFirstChar { it.uppercase() },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            )
                        )
                    }

                    Text(
                        text = question.question,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.88f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Opciones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion?.options?.forEach { option ->
                    val state = viewModel.buttonState(option)
                    val scale by animateFloatAsState(
                        targetValue = when {
                            bounceCorrect && option == currentQuestion?.answer -> 1.06f
                            selectedOption == option -> 1.04f
                            else -> 1.0f
                        },
                        label = "scale"
                    )

                    Button(
                        onClick = { viewModel.onSelect(option) },
                        enabled = !wrongSelection && selectedOption == null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 60.dp)
                            .scale(scale),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (state) {
                                TriviaButtonState.CORRECT  -> Color(0xFF4CAF50)
                                TriviaButtonState.WRONG    -> Color(0xFFEF5350)
                                TriviaButtonState.SELECTED -> Color(0xFF1976D2)
                                TriviaButtonState.NORMAL   -> Color(0xFF2196F3).copy(alpha = 0.75f)
                            },
                            disabledContainerColor = when (state) {
                                TriviaButtonState.CORRECT  -> Color(0xFF4CAF50)
                                TriviaButtonState.WRONG    -> Color(0xFFEF5350)
                                TriviaButtonState.SELECTED -> Color(0xFF1976D2)
                                TriviaButtonState.NORMAL   -> Color(0xFF2196F3).copy(alpha = 0.75f)
                            }
                        )
                    ) {
                        Text(
                            text = option,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}