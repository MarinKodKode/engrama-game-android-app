package com.manu.kode.engrama.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.navigation.NavHostController
import com.manu.kode.engrama.R
import com.manu.kode.engrama.navigation.Analytics
import com.manu.kode.engrama.navigation.ChooseOperation
import com.manu.kode.engrama.navigation.Settings
import com.manu.kode.engrama.navigation.TriviaChallenge
import com.manu.kode.engrama.navigation.WordChallenge
import com.manu.kode.engrama.ui.theme.Dimens
import com.manu.kode.engrama.ui.theme.EngramaTheme

/**
 * iOS `HomeView`. El padding de la barra de navegación lo aplica MainActivity;
 * aquí solo se aplica el de la barra de estado.
 */
@Composable
fun HomeScreen(navController: NavHostController) {
    val colors = EngramaTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.homeTopBarHorizontal)
                .padding(top = Dimens.homeTopBarTop),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.home_analytics),
                contentDescription = "Analytics",
                modifier = Modifier
                    .clickable(role = Role.Button) { navController.navigate(Analytics) }
                    .padding(start = Dimens.homeTopBarImageInset)
                    .size(Dimens.homeTopBarImage)
            )
            Image(
                painter = painterResource(R.drawable.home_settings),
                contentDescription = "Settings",
                modifier = Modifier
                    .clickable(role = Role.Button) { navController.navigate(Settings) }
                    .padding(end = Dimens.homeTopBarImageInset)
                    .size(Dimens.homeTopBarImage)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.homeContentHorizontal)
                .padding(top = Dimens.homeContentTop),
            verticalArrangement = Arrangement.spacedBy(Dimens.homeSectionSpacing)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.homeHeaderBottom),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.homeLogoTitleSpacing)
            ) {
                Image(
                    painter = painterResource(R.drawable.launch_icon),
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.homeLogo)
                )
                Text(
                    text = "Engrama",
                    style = MaterialTheme.typography.displaySmall,
                    color = colors.blue
                )
            }

            SectionMenu(title = "Lenguaje") {
                MenuRow(
                    icon = R.drawable.ic_home_language,
                    label = "Reto tipo de palabra",
                    subtitle = "Identifica verbos, adjetivos y más",
                    color = colors.purple
                ) { navController.navigate(WordChallenge) }
            }

            SectionMenu(title = "Matemáticas") {
                MenuRow(
                    icon = R.drawable.ic_home_math,
                    label = "Reto cálculo mental",
                    subtitle = "Suma, resta, multiplica y divide",
                    color = colors.cyan
                ) { navController.navigate(ChooseOperation) }
            }

            SectionMenu(title = "Trivia") {
                MenuRow(
                    icon = R.drawable.ic_home_trivia,
                    label = "Reto de cultura general",
                    subtitle = "Química, historia, geografía y más",
                    color = colors.orange
                ) { navController.navigate(TriviaChallenge) }
            }

            // Flashcards aún no portado: fila deshabilitada.
            SectionMenu(title = "Estudio") {
                MenuRow(
                    icon = R.drawable.ic_home_study,
                    label = "Tarjetas de estudio",
                    subtitle = "Crea mazos, practica y repasa con SM-2",
                    color = colors.green,
                    enabled = false
                ) {}
            }

            // Perfil de Aprendizaje aún no portado: fila deshabilitada.
            SectionMenu(title = "Perfil de Aprendizaje") {
                MenuRow(
                    icon = R.drawable.ic_home_learning_profile,
                    label = "Test CHAEA y VAK",
                    subtitle = "Descubre tu estilo de aprendizaje",
                    color = colors.indigo,
                    enabled = false
                ) {}
            }
        }
    }
}
