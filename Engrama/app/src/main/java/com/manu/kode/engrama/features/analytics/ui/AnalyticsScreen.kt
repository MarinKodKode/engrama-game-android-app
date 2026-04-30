package com.manu.kode.engrama.features.analytics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.manu.kode.engrama.data.model.GameSession
import com.manu.kode.engrama.data.model.GameType
import com.manu.kode.engrama.features.analytics.viewmodel.AnalyticsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(
    navController: NavHostController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessions.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Text("←", fontSize = 24.sp)
            }
            Text(
                text = "Tu historial",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filtros
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            label = "Todo",
                            isSelected = selectedFilter == null,
                            onClick = { viewModel.setFilter(null) }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Matemáticas",
                            isSelected = selectedFilter == GameType.MATH,
                            onClick = { viewModel.setFilter(GameType.MATH) }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Lenguaje",
                            isSelected = selectedFilter == GameType.LANGUAGE,
                            onClick = { viewModel.setFilter(GameType.LANGUAGE) }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Trivia",
                            isSelected = selectedFilter == GameType.TRIVIA,
                            onClick = { viewModel.setFilter(GameType.TRIVIA) }
                        )
                    }
                }
            }

            // Stats
            if (viewModel.filteredSessions.isNotEmpty()) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatBubble(
                            value = "${viewModel.totalSessions}",
                            label = "Partidas",
                            color = accentColor(selectedFilter),
                            modifier = Modifier.weight(1f)
                        )
                        StatBubble(
                            value = "${viewModel.bestScore}",
                            label = "Mejor score",
                            color = Color(0xFFFF9800),
                            modifier = Modifier.weight(1f)
                        )
                        StatBubble(
                            value = "${viewModel.averageScore}",
                            label = "Promedio",
                            color = Color(0xFF9C27B0),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Título historial
            item {
                Text(
                    text = "Historial",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Lista
            if (viewModel.filteredSessions.isEmpty()) {
                item { EmptyHistorialView() }
            } else {
                items(viewModel.filteredSessions) { session ->
                    SessionRowView(session = session)
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF9C27B0)
            else Color.Gray.copy(alpha = 0.12f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

@Composable
fun StatBubble(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
fun SessionRowView(session: GameSession) {
    val dateFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale("es"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Ícono
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = rowColor(session).copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = rowIcon(session),
                        fontSize = 22.sp
                    )
                }
            }

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = rowTitle(session),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = rowSubtitle(session),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = dateFormat.format(session.date),
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }

            // Score
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${session.score}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = scoreColor(session)
                )
                Text(
                    text = "pts",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyHistorialView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "🎮", fontSize = 56.sp)
        Text(
            text = "¡Aún no has jugado!",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Completa un reto y aquí\nverás tu historial.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

fun rowTitle(session: GameSession): String = when (session.gameType) {
    GameType.MATH -> when (session.operation) {
        "+" -> "Suma"
        "-" -> "Resta"
        "*" -> "Multiplicación"
        "/" -> "División"
        else -> "Matemáticas"
    }
    GameType.LANGUAGE -> "Tipo de Palabra"
    GameType.TRIVIA -> "Cultura General"
}

fun rowSubtitle(session: GameSession): String = when (session.gameType) {
    GameType.MATH -> "${session.numberOfDigits} dígito(s) · ${session.timeLimit}s"
    GameType.LANGUAGE -> "Tipo de palabra · ${session.timeLimit}s"
    GameType.TRIVIA -> "Trivia · ${session.timeLimit}s"
}

fun rowIcon(session: GameSession): String = when (session.gameType) {
    GameType.MATH -> when (session.operation) {
        "+" -> "➕"
        "-" -> "➖"
        "*" -> "✖️"
        "/" -> "➗"
        else -> "🧮"
    }
    GameType.LANGUAGE -> "📝"
    GameType.TRIVIA -> "🧠"
}

fun rowColor(session: GameSession): Color = when (session.gameType) {
    GameType.MATH -> when (session.operation) {
        "+" -> Color(0xFF00BCD4)
        "-" -> Color(0xFF795548)
        "*" -> Color(0xFF4CAF50)
        "/" -> Color(0xFF2196F3)
        else -> Color.Gray
    }
    GameType.LANGUAGE -> Color(0xFF9C27B0)
    GameType.TRIVIA -> Color(0xFFFF9800)
}

fun scoreColor(session: GameSession): Color {
    val max = when (session.gameType) {
        GameType.MATH -> 50
        GameType.LANGUAGE -> 30
        GameType.TRIVIA -> 20
    }
    val ratio = session.score.toFloat() / max.toFloat()
    return when {
        ratio < 0.25f -> Color(0xFFEF5350)
        ratio < 0.5f  -> Color(0xFFFF9800)
        ratio < 0.75f -> Color(0xFFFFEB3B)
        else          -> Color(0xFF2196F3)
    }
}

fun accentColor(filter: GameType?): Color = when (filter) {
    GameType.MATH     -> Color(0xFF00BCD4)
    GameType.LANGUAGE -> Color(0xFF9C27B0)
    GameType.TRIVIA   -> Color(0xFFFF9800)
    null              -> Color(0xFF00BCD4)
}