package ru.tbank.petcare.presentation.navigation

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import ru.tbank.petcare.R

@Parcelize
sealed interface Route : Parcelable {
    @Parcelize
    data class PetProfile(val petId: String) : Route

    @Parcelize
    data class PublicPetProfile(val petId: String) : Route

    @Parcelize
    data object AddPet : Route

    @Parcelize
    data class EditPet(val petId: String) : Route

    @Parcelize
    data object Login : Route

    @Parcelize
    data object Register : Route

    @Parcelize
    data class CreateActivity(
        val petId: String?,
        val type: String? = null,
        val instanceId: String = java.util.UUID.randomUUID().toString()
    ) : Route
    data object Continue : Route

    @Parcelize
    data object Settings : Route

    @Parcelize
    data object EditProfile : Route

    @Parcelize
    data class Analytics(
        val petId: String
    ) : Route

    @Parcelize
    data class AllRecent(
        val petId: String
    ) : Route

    @Parcelize
    data object Onboarding : Route
}


@Parcelize
sealed interface NavigationBarRoute : Route {
    @Parcelize
    data object MyPets : NavigationBarRoute

    @Parcelize
    data object Public : NavigationBarRoute

    @Parcelize
    data object PetRunner : NavigationBarRoute

    @Parcelize
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

@Suppress("CyclomaticComplexMethod")
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
        is Route.CreateActivity -> R.string.create_activity
        is Route.PublicPetProfile -> R.string.public_pet_profile
        Route.Continue -> R.string.continue_registration
        Route.Settings -> R.string.settings
        Route.EditProfile -> R.string.edit_profile
        is Route.Analytics -> R.string.analytics
        is Route.AllRecent -> R.string.recent_activities
        Route.Onboarding -> R.string.app_name
    }
}
