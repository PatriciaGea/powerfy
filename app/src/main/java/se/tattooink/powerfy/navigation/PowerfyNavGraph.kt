package se.tattooink.powerfy.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import se.tattooink.powerfy.ui.cart.CartRoute
import se.tattooink.powerfy.ui.checkout.CheckoutRoute
import se.tattooink.powerfy.ui.components.TopBar
import se.tattooink.powerfy.ui.confirmation.ConfirmationRoute
import se.tattooink.powerfy.ui.favorites.FavoritesRoute
import se.tattooink.powerfy.ui.intro.IntroRoute
import se.tattooink.powerfy.ui.login.LoginRoute
import se.tattooink.powerfy.ui.productdetail.ProductDetailRoute
import se.tattooink.powerfy.ui.profile.ProfileRoute
import se.tattooink.powerfy.ui.home.HomeRoute
import se.tattooink.powerfy.ui.signup.SignUpRoute
import se.tattooink.powerfy.ui.splash.SplashRoute

private val TOP_BAR_ROUTES = setOf(
    PowerfyDestination.Home.route,
    PowerfyDestination.Favorites.route,
    PowerfyDestination.Cart.route
)

@Composable
fun PowerfyNavGraph(navController: NavHostController = rememberNavController()) {
    val appViewModel: AppViewModel = hiltViewModel()
    val isLoggedIn by appViewModel.isLoggedInFlow.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val cartItemCount by appViewModel.cartItemCount.collectAsState()

    val goToHome: () -> Unit = {
        navController.navigate(PowerfyDestination.Home.route) {
            popUpTo(PowerfyDestination.Home.route) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            if (currentRoute in TOP_BAR_ROUTES) {
                TopBar(
                    isLoggedIn = isLoggedIn,
                    cartItemCount = cartItemCount,
                    onFavoriteClick = { navController.navigate(PowerfyDestination.Favorites.route) },
                    onCartClick = { navController.navigate(PowerfyDestination.Cart.route) },
                    onProfileClick = {
                        if (isLoggedIn) {
                            navController.navigate(PowerfyDestination.Profile.route)
                        } else {
                            navController.navigate(PowerfyDestination.Intro.route) {
                                popUpTo(PowerfyDestination.Home.route) { inclusive = true }
                            }
                        }
                    },
                    onLogoClick = goToHome
                )
            }
        }
    ) { innerPadding ->
        val topBarShown = currentRoute in TOP_BAR_ROUTES
        NavHost(
            navController = navController,
            startDestination = PowerfyDestination.Splash.route,
            modifier = Modifier.padding(
                top = if (topBarShown) innerPadding.calculateTopPadding() else 0.dp,
                bottom = if (topBarShown) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {

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
                    onLoginClick = { navController.navigate(PowerfyDestination.Login.route) },
                    onSignUpClick = { navController.navigate(PowerfyDestination.SignIn.route) },
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
                    onSignUpClick = { navController.navigate(PowerfyDestination.SignIn.route) }
                )
            }

            composable(PowerfyDestination.SignIn.route) {
                SignUpRoute(
                    onSignUpSuccess = {
                        navController.navigate(PowerfyDestination.Home.route) {
                            popUpTo(PowerfyDestination.Intro.route) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() },
                    onLoginClick = {
                        navController.navigate(PowerfyDestination.Login.route) {
                            popUpTo(PowerfyDestination.SignIn.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(PowerfyDestination.Home.route) {
                HomeRoute(onProductClick = { productId ->
                    navController.navigate(PowerfyDestination.ProductDetail.createRoute(productId))
                })
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
                FavoritesRoute(onProductClick = { productId ->
                    navController.navigate(PowerfyDestination.ProductDetail.createRoute(productId))
                })
            }

            composable(PowerfyDestination.Cart.route) {
                CartRoute(onCheckoutClick = {
                    navController.navigate(PowerfyDestination.Checkout.route)
                })
            }

            composable(PowerfyDestination.Checkout.route) {
                CheckoutRoute(
                    onBackClick = { navController.popBackStack() },
                    onPaymentSuccess = {
                        navController.navigate(PowerfyDestination.Confirmation.route) {
                            popUpTo(PowerfyDestination.Home.route)
                        }
                    }
                )
            }

            composable(PowerfyDestination.Confirmation.route) {
                ConfirmationRoute(
                    onBackToHomeClick = {
                        navController.navigate(PowerfyDestination.Home.route) {
                            popUpTo(PowerfyDestination.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}