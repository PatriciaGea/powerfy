package se.tattooink.powerfy.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import se.tattooink.powerfy.ui.favorites.FavoritesRoute
import se.tattooink.powerfy.ui.intro.IntroRoute
import se.tattooink.powerfy.ui.login.LoginRoute
import se.tattooink.powerfy.ui.productdetail.ProductDetailRoute
import se.tattooink.powerfy.ui.profile.ProfileRoute
import se.tattooink.powerfy.ui.home.HomeRoute
import se.tattooink.powerfy.ui.signup.SignUpRoute
import se.tattooink.powerfy.ui.splash.SplashRoute
import androidx.compose.runtime.Composable

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

        composable(PowerfyDestination.SignIn.route) {
            SignUpRoute(
                onSignUpSuccess = {
                    navController.navigate(PowerfyDestination.Home.route) {
                        popUpTo(PowerfyDestination.Intro.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(PowerfyDestination.Login.route) {
                        popUpTo(PowerfyDestination.SignIn.route) { inclusive = true }
                    }
                }
            )
        }

        composable(PowerfyDestination.Home.route) {
            HomeRoute(
                onNavigateToIntro = {
                    navController.navigate(PowerfyDestination.Intro.route) {
                        popUpTo(PowerfyDestination.Home.route) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(PowerfyDestination.Profile.route)
                },
                onProductClick = { productId ->
                    navController.navigate(PowerfyDestination.ProductDetail.createRoute(productId))
                },
                onFavoriteIconClick = {
                    navController.navigate(PowerfyDestination.Favorites.route)
                },
                onCartIconClick = {
                    navController.navigate(PowerfyDestination.Cart.route)
                }
            )
        }

        composable(PowerfyDestination.Profile.route) {
            ProfileRoute(
                onBackClick = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(PowerfyDestination.Intro.route) {
                        popUpTo(PowerfyDestination.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = PowerfyDestination.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            ProductDetailRoute(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate(PowerfyDestination.ProductDetail.createRoute(productId))
                }
            )
        }

        composable(PowerfyDestination.Favorites.route) {
            FavoritesRoute(
                onProductClick = { productId ->
                    navController.navigate(PowerfyDestination.ProductDetail.createRoute(productId))
                },
                onFavoriteIconClick = { navController.popBackStack() },
                onCartClick = {
                    navController.navigate(PowerfyDestination.Cart.route)
                },
                onNavigateToProfile = {
                    navController.navigate(PowerfyDestination.Profile.route)
                },
                onNavigateToIntro = {
                    navController.navigate(PowerfyDestination.Intro.route) {
                        popUpTo(PowerfyDestination.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(PowerfyDestination.Cart.route) {
            se.tattooink.powerfy.ui.cart.CartRoute(
                isLoggedIn = true,
                onFavoriteIconClick = {
                    navController.navigate(PowerfyDestination.Favorites.route)
                },
                onProfileClick = {
                    navController.navigate(PowerfyDestination.Profile.route)
                },
                onCheckoutClick = {}
            )
        }
    }
}