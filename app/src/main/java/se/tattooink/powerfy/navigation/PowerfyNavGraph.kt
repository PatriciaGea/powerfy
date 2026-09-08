package se.tattooink.powerfy.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.tattooink.powerfy.ui.intro.IntroRoute
import se.tattooink.powerfy.ui.login.LoginRoute
import se.tattooink.powerfy.ui.splash.SplashRoute

@Composable
fun PowerfyNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = PowerfyDestination.Splash.route) {

        composable(PowerfyDestination.Splash.route) {
            SplashRoute(
                onNavigateToIntro = {
                    navController.navigate(PowerfyDestination.Intro.route) {
                        popUpTo(PowerfyDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(PowerfyDestination.Home.route) {
                        popUpTo(PowerfyDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(PowerfyDestination.Intro.route) {
            IntroRoute(
                onLoginClick = {
                    navController.navigate(PowerfyDestination.Login.route)
                },
                onSignUpClick = {
                    navController.navigate(PowerfyDestination.SignIn.route)
                },
                onGuestLoginSuccess = {
                    navController.navigate(PowerfyDestination.Home.route) {
                        popUpTo(PowerfyDestination.Intro.route) { inclusive = true }
                    }
                }
            )
        }

        composable(PowerfyDestination.Login.route) {
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(PowerfyDestination.Home.route) {
                        popUpTo(PowerfyDestination.Intro.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(PowerfyDestination.SignIn.route)
                }
            )
        }

        composable(PowerfyDestination.SignIn.route) { Text("Sign In screen — próxima etapa") }
        composable(PowerfyDestination.Home.route) { Text("Home screen — próxima etapa") }
    }
}