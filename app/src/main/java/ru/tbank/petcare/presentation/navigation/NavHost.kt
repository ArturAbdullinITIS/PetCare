package ru.tbank.petcare.presentation.navigation

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
import ru.tbank.petcare.presentation.screen.continueRegistration.ContinueRegistrationScreen
import ru.tbank.petcare.presentation.screen.createActivity.CreateActivityScreen
import ru.tbank.petcare.presentation.screen.editpet.EditPetScreen
import ru.tbank.petcare.presentation.screen.editprofile.EditProfileScreen
import ru.tbank.petcare.presentation.screen.login.LoginScreen
import ru.tbank.petcare.presentation.screen.mypets.MyPetsScreen
import ru.tbank.petcare.presentation.screen.petProfile.PetProfileScreen
import ru.tbank.petcare.presentation.screen.publicPetProfile.PublicPetProfileScreen
import ru.tbank.petcare.presentation.screen.publicProfiles.PublicProfilesScreen
import ru.tbank.petcare.presentation.screen.registration.RegistrationScreen
import ru.tbank.petcare.presentation.screen.settings.SettingsScreen
import ru.tbank.petcare.presentation.screen.userprofile.UserProfileScreen

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    startDestination: StartDestination
) {
    val navHostViewModel: NavHostViewModel = hiltViewModel()
    val isOnline by navHostViewModel.isOnline.collectAsState()

    val startRoute = when (startDestination) {
        StartDestination.Auth -> Route.Login
        StartDestination.Main -> NavigationBarRoute.MyPets
    }

    val backStack = rememberSaveable {
        mutableStateListOf<Route>(startRoute)
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
                    } else {
                        if (currentRoute is Route.Login || currentRoute is Route.Register) {
                            null
                        } else {
                            ScreenTitleRow(
                                name = getRouteTitle(currentRoute),
                                onClick = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
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
                    onClick = {
                        backStack.add(Route.AddPet)
                    },
                    enabled = isOnline
                )
            }
        },
        bottomBar = {
            if (isBottomBar) {
                CustomBottomNavBar(
                    currentRoute = currentRoute,
                    onSelected = { route ->
                        if (route != currentRoute) {
                            backStack.add(route)
                        }
                    }
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
                        SettingsScreen()
                    }
                    entry<NavigationBarRoute.Profile> {
                        UserProfileScreen(
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
                            },
                        )
                    }
                    entry<Route.EditProfile> {
                        EditProfileScreen(
                            onContinue = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                }
            )
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
