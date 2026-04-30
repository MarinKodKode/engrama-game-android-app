package com.manu.kode.engrama.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun EngramaNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route
    ) {
        composable(AppRoute.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(AppRoute.ChooseOperation.route) {
            ChooseOperationScreen(navController = navController)
        }

        composable(AppRoute.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(AppRoute.Analytics.route) {
            AnalyticsScreen(navController = navController)
        }

        composable(
            route = AppRoute.ChallengeOperation.ROUTE,
            arguments = listOf(
                navArgument("sign") { type = NavType.StringType },
                navArgument("digits") { type = NavType.IntType },
                navArgument("timeLimit") { type = NavType.IntType },
                navArgument("useNegatives") { type = NavType.BoolType },
                navArgument("hapticEnabled") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val sign = backStackEntry.arguments?.getString("sign") ?: "+"
            val digits = backStackEntry.arguments?.getInt("digits") ?: 1
            val timeLimit = backStackEntry.arguments?.getInt("timeLimit") ?: 30
            val useNegatives = backStackEntry.arguments?.getBoolean("useNegatives") ?: false
            val hapticEnabled = backStackEntry.arguments?.getBoolean("hapticEnabled") ?: true

            ChallengeOperationScreen(
                sign = sign,
                digits = digits,
                timeLimit = timeLimit,
                useNegatives = useNegatives,
                hapticEnabled = hapticEnabled,
                navController = navController
            )
        }

        composable(
            route = AppRoute.SummaryMath.ROUTE,
            arguments = listOf(navArgument("score") { type = NavType.IntType })
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            SummaryMathScreen(score = score, navController = navController)
        }

        composable(AppRoute.WordChallenge.route) {
            WordChallengeScreen(navController = navController)
        }

        composable(
            route = AppRoute.SummaryLanguage.ROUTE,
            arguments = listOf(navArgument("score") { type = NavType.IntType })
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            SummaryLanguageScreen(score = score, navController = navController)
        }

        composable(AppRoute.TriviaChallenge.route) {
            TriviaChallengeScreen(navController = navController)
        }

        composable(
            route = AppRoute.SummaryTrivia.ROUTE,
            arguments = listOf(navArgument("score") { type = NavType.IntType })
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            SummaryTriviaScreen(score = score, navController = navController)
        }
    }
}