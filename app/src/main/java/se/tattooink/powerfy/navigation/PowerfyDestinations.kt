package se.tattooink.powerfy.navigation

sealed class PowerfyDestination(val route: String) {
    data object Splash : PowerfyDestination("splash")
    data object Intro : PowerfyDestination("intro")
    data object Login : PowerfyDestination("login")
    data object SignIn : PowerfyDestination("sign_in")
    data object Home : PowerfyDestination("home")
    data object ProductDetail : PowerfyDestination("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    data object Favorites : PowerfyDestination("favorites")
    data object Cart : PowerfyDestination("cart")
    data object Checkout : PowerfyDestination("checkout")
    data object Payment : PowerfyDestination("payment")
    data object Confirmation : PowerfyDestination("confirmation")
}
