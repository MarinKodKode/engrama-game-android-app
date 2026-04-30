package com.manu.kode.engrama.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manu.kode.engrama.data.model.color
import com.manu.kode.engrama.data.model.emoji
import com.manu.kode.engrama.features.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    var showNameDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(settings.profile.name) }

    val timeOptions = listOf(30, 45, 60, 75, 90)
    val difficultyOptions = listOf("easy" to "Fácil", "medium" to "Medio", "hard" to "Difícil")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text("←", fontSize = 24.sp)
                }
                Text(
                    text = "Configuración",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = viewModel.currentDivision.color().copy(alpha = 0.15f)
                ),
                onClick = { showNameDialog = true }
            ) {
                Box {
                    // Planeta fondo
                    Text(
                        text = viewModel.currentDivision.emoji(),
                        fontSize = 80.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 20.dp, y = (-10).dp)
                    )

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 32.sp)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = settings.profile.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = viewModel.currentDivision.displayName,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Barra de progreso
                viewModel.currentDivision.next?.let { next ->
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp, end = 16.dp, bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Hacia ${next.displayName}",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "${viewModel.pointsToNext} pts restantes",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                        LinearProgressIndicator(
                            progress = { viewModel.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = viewModel.currentDivision.color(),
                            trackColor = Color.Gray.copy(alpha = 0.15f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${viewModel.totalPoints} pts totales",
                                fontSize = 12.sp,
                                color = Color.Gray.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${next.threshold} pts",
                                fontSize = 12.sp,
                                color = Color.Gray.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        // General
        item {
            SettingsSectionCard(title = "General") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vibrar al equivocarse", fontSize = 16.sp)
                    Switch(
                        checked = settings.hapticOnError,
                        onCheckedChange = { viewModel.updateHaptic(it) }
                    )
                }
            }
        }

        // Matemáticas
        item {
            SettingsSectionCard(title = "Reto Matemáticas") {
                Text("Duración", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                TimeSelector(
                    options = timeOptions,
                    selected = settings.math.timeLimit,
                    onSelect = { viewModel.updateMathTimeLimit(it) }
                )
            }
        }

        // Español
        item {
            SettingsSectionCard(title = "Reto Español") {
                Text("Duración", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                TimeSelector(
                    options = timeOptions,
                    selected = settings.spanish.timeLimit,
                    onSelect = { viewModel.updateSpanishTimeLimit(it) }
                )
            }
        }

        // Trivia
        item {
            SettingsSectionCard(title = "Cultura General") {
                Text("Duración", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                TimeSelector(
                    options = timeOptions,
                    selected = settings.trivia.timeLimit,
                    onSelect = { viewModel.updateTriviaTimeLimit(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Dificultad", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    difficultyOptions.forEach { (key, label) ->
                        FilterButton(
                            label = label,
                            isSelected = settings.trivia.difficulty == key,
                            onClick = { viewModel.updateTriviaDifficulty(key) }
                        )
                    }
                }
            }
        }
    }

    // Dialog nombre
    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Tu nombre") },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nombre o nick") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateName(nameInput)
                    showNameDialog = false
                }) { Text("Listo") }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SettingsSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun TimeSelector(
    options: List<Int>,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { seconds ->
            FilterButton(
                label = "${seconds}s",
                isSelected = selected == seconds,
                onClick = { onSelect(seconds) }
            )
        }
    }
}

@Composable
fun FilterButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF9C27B0)
            else Color.Gray.copy(alpha = 0.12f)
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}