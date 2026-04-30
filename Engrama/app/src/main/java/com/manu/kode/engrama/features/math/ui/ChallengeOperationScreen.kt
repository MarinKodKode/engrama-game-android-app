package com.manu.kode.engrama.features.math.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manu.kode.engrama.features.math.viewmodel.ChallengeOperationViewModel
import com.manu.kode.engrama.navigation.AppRoute
import kotlinx.coroutines.delay

@Composable
fun ChallengeOperationScreen(
    sign: String,
    digits: Int,
    timeLimit: Int,
    useNegatives: Boolean,
    hapticEnabled: Boolean,
    navController: NavHostController,
    viewModel: ChallengeOperationViewModel = hiltViewModel()
) {
    val numberA by viewModel.numberA.collectAsState()
    val numberB by viewModel.numberB.collectAsState()
    val result by viewModel.result.collectAsState()
    val score by viewModel.score.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()
    val showCountdown by viewModel.showCountdown.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(sign, digits, timeLimit, useNegatives, hapticEnabled)
        viewModel.startCountdown()
    }

    LaunchedEffect(gameOver) {
        if (gameOver) {
            navController.navigate(
                AppRoute.SummaryMath.createRoute(viewModel.score.value)
            ) {
                popUpTo(AppRoute.ChooseOperation.route)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFF))
            .statusBarsPadding()
    ) {
        if (showCountdown) {
            CountdownView()
        } else {
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

                // Operación
                val displayA = if (numberA < 0) "($numberA)" else "$numberA"
                val displayB = if (numberB < 0) "($numberB)" else "$numberB"
                val displaySign = when (sign) {
                    "*" -> "×"
                    "/" -> "÷"
                    else -> sign
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$displayA $displaySign $displayB",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.88f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Input
                OutlinedTextField(
                    value = result,
                    onValueChange = { viewModel.updateResult(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.width(180.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFF2196F3),
                        focusedContainerColor = Color(0xFFE3F2FD),
                        unfocusedContainerColor = Color(0xFFE3F2FD)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones ± y ✓
                Row(
                    modifier = Modifier.width(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.toggleSign() },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.6f)
                        )
                    ) {
                        Text("±", fontSize = 28.sp, color = Color.White)
                    }

                    Button(
                        onClick = { viewModel.onTapButton() },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("✓", fontSize = 28.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CountdownView() {
    val texts = listOf("3", "2", "1", "GO!")
    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in texts.indices) {
            index = i
            delay(700)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texts.getOrElse(index) { "GO!" },
            fontSize = 80.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFEF5350)
        )
    }
}

@Composable
fun TimerRingView(progress: Float, timeLeft: Int) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "timer"
    )

    val color = when {
        progress > 0.5f -> Color(0xFF4CAF50)
        progress > 0.25f -> Color(0xFFFF9800)
        else -> Color(0xFFEF5350)
    }

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(56.dp),
            color = color,
            strokeWidth = 4.dp,
            trackColor = Color.Gray.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "$timeLeft",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}