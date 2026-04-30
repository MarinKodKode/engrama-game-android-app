package com.manu.kode.engrama.features.math.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manu.kode.engrama.features.math.viewmodel.ChooseOperationViewModel
import com.manu.kode.engrama.navigation.AppRoute

@Composable
fun ChooseOperationScreen(
    navController: NavHostController,
    viewModel: ChooseOperationViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Text("←", fontSize = 24.sp)
            }
        }

        // Título
        Text(
            text = "Elige...",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Grid de operaciones
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OperationButton(
                    symbol = "+",
                    color = Color(0xFF66BB6A),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(
                        AppRoute.ChallengeOperation.createRoute(
                            sign = "+",
                            digits = settings.math.add.numberOfDigits,
                            timeLimit = settings.math.timeLimit,
                            useNegatives = settings.math.add.useNegativeNumbers,
                            hapticEnabled = settings.hapticOnError
                        )
                    )
                }
                OperationButton(
                    symbol = "-",
                    color = Color(0xFF42A5F5),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(
                        AppRoute.ChallengeOperation.createRoute(
                            sign = "-",
                            digits = settings.math.subtract.numberOfDigits,
                            timeLimit = settings.math.timeLimit,
                            useNegatives = settings.math.subtract.useNegativeNumbers,
                            hapticEnabled = settings.hapticOnError
                        )
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OperationButton(
                    symbol = "×",
                    color = Color(0xFFAB47BC),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(
                        AppRoute.ChallengeOperation.createRoute(
                            sign = "*",
                            digits = settings.math.multiply.numberOfDigits,
                            timeLimit = settings.math.timeLimit,
                            useNegatives = settings.math.multiply.useNegativeNumbers,
                            hapticEnabled = settings.hapticOnError
                        )
                    )
                }
                OperationButton(
                    symbol = "÷",
                    color = Color(0xFFFFCA28),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(
                        AppRoute.ChallengeOperation.createRoute(
                            sign = "/",
                            digits = settings.math.divide.numberOfDigits,
                            timeLimit = settings.math.timeLimit,
                            useNegatives = settings.math.divide.useNegativeNumbers,
                            hapticEnabled = settings.hapticOnError
                        )
                    )
                }
            }

            // Sorpresa
            Button(
                onClick = {
                    val ops = listOf("+", "-", "*", "/")
                    val randomSign = ops.random()
                    val opSettings = when (randomSign) {
                        "+" -> settings.math.add
                        "-" -> settings.math.subtract
                        "*" -> settings.math.multiply
                        else -> settings.math.divide
                    }
                    navController.navigate(
                        AppRoute.ChallengeOperation.createRoute(
                            sign = randomSign,
                            digits = opSettings.numberOfDigits,
                            timeLimit = settings.math.timeLimit,
                            useNegatives = opSettings.useNegativeNumbers,
                            hapticEnabled = settings.hapticOnError
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF5350)
                )
            ) {
                Text(
                    text = "Sorpresa...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun OperationButton(
    symbol: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = symbol,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}