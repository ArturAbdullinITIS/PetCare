package ru.tbank.petcare.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import ru.tbank.petcare.R

@Serializable
sealed interface Route {
    @Serializable
    data class PetProfile(val petId: String) : Route

    @Serializable
    data class PublicPetProfile(val petId: String) : Route

    @Serializable
    data object AddPet : Route

    @Serializable
    data class EditPet(val petId: String) : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object Continue : Route
}

@Serializable
sealed interface NavigationBarRoute : Route {
    @Serializable
    data object MyPets : NavigationBarRoute

    @Serializable
    data object Public : NavigationBarRoute

    @Serializable
    data object PetRunner : NavigationBarRoute

    @Serializable
    data object Profile : NavigationBarRoute
}

data class RouteConfig(
    @DrawableRes val icon: Int,
    @StringRes val titleRes: Int,
    val route: Route
)
val bottomBarRoutesList = listOf(
    RouteConfig(
        icon = R.drawable.ic_pet_care_main,
        titleRes = R.string.my_pets_screen_title,
        route = NavigationBarRoute.MyPets
    ),
    RouteConfig(
        icon = R.drawable.ic_public,
        titleRes = R.string.public_profiles_title,
        route = NavigationBarRoute.Public
    ),
    RouteConfig(
        icon = R.drawable.ic_pet_runner,
        titleRes = R.string.pet_runner_title,
        route = NavigationBarRoute.PetRunner
    ),
    RouteConfig(
        icon = R.drawable.ic_profile,
        titleRes = R.string.profile_title,
        route = NavigationBarRoute.Profile
    )
)

fun NavigationBarRoute.getConfig(): RouteConfig? {
    return bottomBarRoutesList.find { it.route == this }
}

fun getRouteTitle(route: Route): Int {
    return when (route) {
        is NavigationBarRoute.MyPets -> R.string.my_pets_screen_title
        is NavigationBarRoute.Public -> R.string.public_profiles_title
        is NavigationBarRoute.PetRunner -> R.string.pet_runner_title
        is NavigationBarRoute.Profile -> R.string.profile_title
        is Route.PetProfile -> R.string.pet_profile_title
        is Route.AddPet -> R.string.add_pet_screen_title
        is Route.EditPet -> R.string.edit_pet_screen_title
        Route.Login -> R.string.login
        Route.Register -> R.string.register
        is Route.PublicPetProfile -> R.string.public_pet_profile
        Route.Continue -> R.string.continue_registration
    }
}
