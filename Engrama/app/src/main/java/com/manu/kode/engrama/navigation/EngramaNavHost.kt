package com.manu.kode.engrama.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.manu.kode.engrama.features.analytics.ui.AnalyticsScreen
import com.manu.kode.engrama.features.home.HomeScreen
import com.manu.kode.engrama.features.language.ui.SummaryLanguageScreen
import com.manu.kode.engrama.features.language.ui.WordChallengeScreen
import com.manu.kode.engrama.features.math.ui.ChallengeOperationScreen
import com.manu.kode.engrama.features.math.ui.ChooseOperationScreen
import com.manu.kode.engrama.features.math.ui.SummaryMathScreen
import com.manu.kode.engrama.features.settings.ui.SettingsScreen
import com.manu.kode.engrama.features.trivia.ui.SummaryTriviaScreen
import com.manu.kode.engrama.features.trivia.ui.TriviaChallengeScreen

@Composable
fun EngramaNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(navController = navController)
        }

        composable<ChooseOperation> {
            ChooseOperationScreen(navController = navController)
        }

        composable<Settings> {
            SettingsScreen(navController = navController)
        }

        composable<Analytics> {
            AnalyticsScreen(navController = navController)
        }

        composable<ChallengeOperation> { backStackEntry ->
            val route = backStackEntry.toRoute<ChallengeOperation>()
            ChallengeOperationScreen(
                sign = route.sign,
                digits = route.digits,
                timeLimit = route.timeLimit,
                useNegatives = route.useNegatives,
                hapticEnabled = route.hapticEnabled,
                navController = navController
            )
        }

        composable<SummaryMath> { backStackEntry ->
            val route = backStackEntry.toRoute<SummaryMath>()
            SummaryMathScreen(score = route.score, navController = navController)
        }

        composable<WordChallenge> {
            WordChallengeScreen(navController = navController)
        }

        composable<SummaryLanguage> { backStackEntry ->
            val route = backStackEntry.toRoute<SummaryLanguage>()
            SummaryLanguageScreen(score = route.score, navController = navController)
        }

        composable<TriviaChallenge> {
            TriviaChallengeScreen(navController = navController)
        }

        composable<SummaryTrivia> { backStackEntry ->
            val route = backStackEntry.toRoute<SummaryTrivia>()
            SummaryTriviaScreen(score = route.score, navController = navController)
        }
    }
}
