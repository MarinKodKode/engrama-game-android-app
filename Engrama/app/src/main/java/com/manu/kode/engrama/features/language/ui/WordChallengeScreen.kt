package com.manu.kode.engrama.features.language.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.manu.kode.engrama.features.language.viewmodel.WordChallengeViewModel
import com.manu.kode.engrama.features.math.ui.TimerRingView
import com.manu.kode.engrama.navigation.AppRoute

@Composable
fun WordChallengeScreen(
    navController: NavHostController,
    viewModel: WordChallengeViewModel = hiltViewModel()
) {
    val currentWord by viewModel.currentWord.collectAsState()
    val score by viewModel.score.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val wrongSelection by viewModel.wrongSelection.collectAsState()
    val bounceCorrect by viewModel.bounceCorrect.collectAsState()
    val activeCategories by viewModel.activeCategories.collectAsState()

    LaunchedEffect(Unit) { viewModel.init() }

    LaunchedEffect(gameOver) {
        if (gameOver) {
            navController.navigate(
                AppRoute.SummaryLanguage.createRoute(viewModel.score.value)
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

            // Palabra
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¿Qué tipo de palabra es?",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = currentWord?.word ?: "",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.88f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Grid categorías
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(activeCategories) { category ->
                    val state = buttonState(
                        category = category,
                        selectedCategory = selectedCategory,
                        wrongSelection = wrongSelection,
                        correctCategory = currentWord?.category,
                        bounceCorrect = bounceCorrect
                    )

                    val scale by animateFloatAsState(
                        targetValue = when {
                            bounceCorrect && category == currentWord?.category -> 1.08f
                            selectedCategory == category -> 1.04f
                            else -> 1.0f
                        },
                        label = "scale"
                    )

                    Button(
                        onClick = { viewModel.selectCategory(category) },
                        enabled = !wrongSelection,
                        modifier = Modifier
                            .height(80.dp)
                            .scale(scale),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = state.color,
                            disabledContainerColor = state.color
                        )
                    ) {
                        Text(
                            text = categoryLabel(category),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar
            Button(
                onClick = { viewModel.onConfirm() },
                enabled = selectedCategory != null && !wrongSelection,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

data class ButtonState(val color: Color)

fun buttonState(
    category: String,
    selectedCategory: String?,
    wrongSelection: Boolean,
    correctCategory: String?,
    bounceCorrect: Boolean
): ButtonState {
    return when {
        wrongSelection && category == correctCategory -> ButtonState(Color(0xFF4CAF50))
        wrongSelection && category == selectedCategory -> ButtonState(Color(0xFFEF5350))
        selectedCategory == category -> ButtonState(categoryColor(category))
        else -> ButtonState(categoryColor(category).copy(alpha = 0.75f))
    }
}

fun categoryColor(category: String): Color = when (category) {
    "verbo"       -> Color(0xFF4CAF50)
    "sustantivo"  -> Color(0xFF2196F3)
    "adjetivo"    -> Color(0xFF9C27B0)
    "adverbio"    -> Color(0xFFFF9800)
    "articulo"    -> Color(0xFFE57373)
    "pronombre"   -> Color(0xFF00BCD4)
    "preposicion" -> Color(0xFF7E57C2)
    "conjuncion"  -> Color(0xFF26A69A)
    else          -> Color.Gray
}

fun categoryLabel(category: String): String = when (category) {
    "verbo"       -> "Verbo"
    "sustantivo"  -> "Sustantivo"
    "adjetivo"    -> "Adjetivo"
    "adverbio"    -> "Adverbio"
    "articulo"    -> "Artículo"
    "pronombre"   -> "Pronombre"
    "preposicion" -> "Preposición"
    "conjuncion"  -> "Conjunción"
    else          -> category.replaceFirstChar { it.uppercase() }
}