package com.manu.kode.engrama.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manu.kode.engrama.R
import com.manu.kode.engrama.navigation.AppRoute

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(AppRoute.Analytics.route) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_analytics),
                    contentDescription = "Analytics",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }
            IconButton(onClick = { navController.navigate(AppRoute.Settings.route) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = "Settings",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Logo + nombre
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Engrama",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Sección Lenguaje
            HomeSectionView(title = "Lenguaje") {
                HomeMenuRow(
                    icon = "Abc",
                    label = "Reto tipo de palabra",
                    subtitle = "Identifica verbos, adjetivos y más",
                    color = Color(0xFF9C27B0)
                ) {
                    navController.navigate(AppRoute.WordChallenge.route)
                }
            }

            // Sección Matemáticas
            HomeSectionView(title = "Matemáticas") {
                HomeMenuRow(
                    icon = "±",
                    label = "Reto cálculo mental",
                    subtitle = "Suma, resta, multiplica y divide",
                    color = Color(0xFF00BCD4)
                ) {
                    navController.navigate(AppRoute.ChooseOperation.route)
                }
            }

            // Sección Trivia
            HomeSectionView(title = "Trivia") {
                HomeMenuRow(
                    icon = "?",
                    label = "Reto de cultura general",
                    subtitle = "Química, historia, geografía y más",
                    color = Color(0xFFFF9800)
                ) {
                    navController.navigate(AppRoute.TriviaChallenge.route)
                }
            }
        }
    }
}

@Composable
fun HomeSectionView(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
        content()
    }
}

@Composable
fun HomeMenuRow(
    icon: String,
    label: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ícono
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = icon,
                            fontSize = 22.sp,
                            color = color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Texto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Chevron
            Icon(
                painter = painterResource(android.R.drawable.ic_media_next),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}