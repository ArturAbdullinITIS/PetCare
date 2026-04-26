package ru.tbank.petcare.presentation.navigation

import android.R.attr.onClick
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.ActivityType
import ru.tbank.petcare.presentation.common.CustomFAB
import ru.tbank.petcare.presentation.common.MainScreenTitleRow
import ru.tbank.petcare.presentation.common.NoInternetBanner
import ru.tbank.petcare.presentation.common.ScreenTitleRow
import ru.tbank.petcare.presentation.root.StartDestination
import ru.tbank.petcare.presentation.screen.addpet.AddPetScreen
import ru.tbank.petcare.presentation.screen.allRecentActivities.AllRecentActivitiesScreen
import ru.tbank.petcare.presentation.screen.analytics.AnalyticsScreen
import ru.tbank.petcare.presentation.screen.continueRegistration.ContinueRegistrationScreen
import ru.tbank.petcare.presentation.screen.createActivity.CreateActivityScreen
import ru.tbank.petcare.presentation.screen.editpet.EditPetScreen
import ru.tbank.petcare.presentation.screen.editprofile.EditProfileScreen
import ru.tbank.petcare.presentation.screen.login.LoginScreen
import ru.tbank.petcare.presentation.screen.minigame.MiniGameScreen
import ru.tbank.petcare.presentation.screen.mypets.MyPetsScreen
import ru.tbank.petcare.presentation.screen.onboarding.OnboardingScreen
import ru.tbank.petcare.presentation.screen.petProfile.PetProfileScreen
import ru.tbank.petcare.presentation.screen.publicPetProfile.PublicPetProfileScreen
import ru.tbank.petcare.presentation.screen.publicProfiles.PublicProfilesScreen
import ru.tbank.petcare.presentation.screen.registration.RegistrationScreen
import ru.tbank.petcare.presentation.screen.settings.SettingsScreen
import ru.tbank.petcare.presentation.screen.userprofile.UserProfileScreen

@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    startDestination: StartDestination
) {
    val navHostViewModel: NavHostViewModel = hiltViewModel()
    val isOnline by navHostViewModel.isOnline.collectAsState()
    val currentUserId by navHostViewModel.currentUserId.collectAsState()

    val startRoute = when (startDestination) {
        StartDestination.Auth -> Route.Login
        StartDestination.Main -> NavigationBarRoute.MyPets
        StartDestination.Onboarding -> Route.Onboarding
    }

    val backStack = rememberSaveable {
        mutableStateListOf(startRoute)
    }
    val currentRoute = backStack.lastOrNull() ?: Route.Register

    val isBottomBar = currentRoute is NavigationBarRoute

    val topBarActions = remember {
        mutableStateOf<(@Composable () -> Unit)?>(null)
    }

    LaunchedEffect(currentRoute) {
        topBarActions.value = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isBottomBar) {
                        MainScreenTitleRow(
                            name = currentRoute.getConfig()?.titleRes ?: R.string.missing_title,
                            icon = currentRoute.getConfig()?.icon ?: R.drawable.photo_placeholder
                        )
                    } else if (currentRoute is Route.Login || currentRoute is Route.Register) {
                        null
                    } else if (currentRoute is Route.Onboarding) {
                        MainScreenTitleRow(
                            name = R.string.app_name,
                            icon = R.drawable.ic_pet_care_main
                        )
                    } else {
                        ScreenTitleRow(
                            name = getRouteTitle(currentRoute),
                            onClick = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    topBarActions.value?.invoke()
                }
            )
        },
        floatingActionButton = {
            if (currentRoute is NavigationBarRoute.MyPets) {
                CustomFAB(
                    modifier = Modifier.padding(bottom = 100.dp),
                    onClick = {
                        backStack.add(Route.AddPet)
                    },
                    enabled = isOnline
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            NavDisplay(
                modifier = Modifier.fillMaxSize(),
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                entryProvider = entryProvider {
                    entry<NavigationBarRoute.MyPets> {
                        MyPetsScreen(
                            onNavigateToProfile = { petId ->
                                backStack.add(Route.PetProfile(petId))
                            },
                            onWalkClick = {
                                backStack.add(
                                    Route.CreateActivity(
                                        type = ActivityType.WALK.value,
                                        petId = ""
                                    )
                                )
                            },
                            onGroomingClick = {
                                backStack.add(
                                    Route.CreateActivity(
                                        type = ActivityType.GROOMING.value,
                                        petId = ""
                                    )
                                )
                            },
                            onVetClick = {
                                backStack.add(
                                    Route.CreateActivity(
                                        type = ActivityType.VET.value,
                                        petId = ""
                                    )
                                )
                            }
                        )
                    }
                    entry<NavigationBarRoute.Public> {
                        PublicProfilesScreen(
                            onPetClick = { petId ->
                                backStack.add(Route.PublicPetProfile(petId))
                            },
                            setTopBarActions = { topBarActions.value = it }
                        )
                    }
                    entry<NavigationBarRoute.PetRunner> {
                        MiniGameScreen()
                    }
                    entry<Route.AddPet> {
                        AddPetScreen(
                            onAddClick = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                    entry<Route.PetProfile> { route ->
                        PetProfileScreen(
                            petId = route.petId,
                            onNavigateToEdit = {
                                backStack.add(Route.EditPet(route.petId))
                            },
                            onCreateActivityClick = { petId ->
                                backStack.add(
                                    Route.CreateActivity(
                                        petId = petId,
                                        type = ActivityType.WALK.value
                                    )
                                )
                            },
                            onNavigateToAnalytics = { petId ->
                                backStack.add(Route.Analytics(petId))
                            }
                        )
                    }
                    entry<Route.EditPet> { route ->
                        EditPetScreen(
                            petId = route.petId,
                            onEditClick = {
                                backStack.removeLastOrNull()
                            },
                            onDeleteClick = {
                                backStack.clear()
                                backStack.add(NavigationBarRoute.MyPets)
                            }
                        )
                    }
                    entry<Route.Login> { route ->
                        LoginScreen(
                            onLoginSuccess = {
                                backStack.clear()
                                backStack.add(NavigationBarRoute.MyPets)
                            },
                            onNavigateToRegistration = {
                                backStack.add(Route.Register)
                            },
                            onNavigateToOnboarding = {
                                backStack.clear()
                                backStack.add(Route.Onboarding)
                            }
                        )
                    }
                    entry<Route.Register> { route ->
                        RegistrationScreen(
                            onNavigateToLogin = {
                                backStack.add(Route.Login)
                            },
                            onEmailRegisterSuccess = {
                                backStack.add(Route.Continue)
                            },
                            onGoogleRegisterSuccess = {
                                backStack.clear()
                                backStack.add(NavigationBarRoute.MyPets)
                            },
                            onNavigateToOnboarding = {
                                backStack.clear()
                                backStack.add(Route.Onboarding)
                            }
                        )
                    }
                    entry<Route.CreateActivity> { route ->
                        CreateActivityScreen(
                            petId = route.petId,
                            type = route.type,
                            onAddClick = {
                                backStack.add(Route.AddPet)
                            },
                            onSaveActivityClick = {
                                backStack.removeLastOrNull()
                            },
                            instanceId = route.instanceId
                        )
                    }

                    entry<Route.PublicPetProfile> { route ->
                        PublicPetProfileScreen(
                            petId = route.petId
                        )
                    }
                    entry<Route.Continue> {
                        ContinueRegistrationScreen(
                            onContinue = {
                                backStack.add(NavigationBarRoute.MyPets)
                            }
                        )
                    }
                    entry<Route.Settings> {
                        SettingsScreen(
                            onDeleteClick = {
                                backStack.clear()
                                backStack.add(Route.Login)
                            }
                        )
                    }
                    entry<NavigationBarRoute.Profile> {
                        if (currentUserId != null) {
                            UserProfileScreen(
                                userId = currentUserId ?: "",
                                onLogoutSuccess = {
                                    backStack.clear()
                                    backStack.add(Route.Login)
                                },
                                onSettingsClick = {
                                    backStack.add(Route.Settings)
                                },
                                setTopBarActions = { topBarActions.value = it },
                                onEditIconClick = {
                                    backStack.add(Route.EditProfile)
                                }
                            )
                        }
                    }
                    entry<Route.EditProfile> {
                        if (currentUserId != null) {
                            EditProfileScreen(
                                onContinue = {
                                    backStack.removeLastOrNull()
                                },
                                userId = currentUserId ?: ""
                            )
                        }
                    }
                    entry<Route.Analytics> { route ->
                        AnalyticsScreen(
                            petId = route.petId,
                            onNavigateToRecent = { petId ->
                                backStack.add(Route.AllRecent(petId))
                            }
                        )
                    }
                    entry<Route.AllRecent> { route ->
                        AllRecentActivitiesScreen(
                            petId = route.petId
                        )
                    }
                    entry<Route.Onboarding> {
                        OnboardingScreen(
                            setTopBarActions = {
                                topBarActions.value = it
                            },
                            onFinished = {
                                backStack.clear()
                                backStack.add(Route.Register)
                            }
                        )
                    }
                }
            )

            if (isBottomBar) {
                CustomBottomNavBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    currentRoute = currentRoute,
                    onSelected = { route ->
                        if (route != currentRoute) {
                            backStack.add(route)
                        }
                    }
                )
            }

            if (!isOnline) {
                NoInternetBanner(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
