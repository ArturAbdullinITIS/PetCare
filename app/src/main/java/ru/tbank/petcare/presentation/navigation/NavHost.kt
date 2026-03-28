package ru.tbank.petcare.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.w3c.dom.Text
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.CustomFAB
import ru.tbank.petcare.presentation.common.MainScreenTitleRow
import ru.tbank.petcare.presentation.common.ScreenTitleRow
import ru.tbank.petcare.presentation.screen.mypets.MyPetsScreen
import ru.tbank.petcare.presentation.screen.publicProfiles.PublicProfilesScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberSaveable {
        mutableStateListOf<Route>(NavigationBarRoute.MyPets)
    }
    val currentRoute = backStack.lastOrNull() ?: NavigationBarRoute.MyPets

    val isBottomBar = currentRoute is NavigationBarRoute

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if(isBottomBar) {
                        MainScreenTitleRow(
                            name = currentRoute.getConfig()?.titleRes ?: R.string.missing_title,
                            icon = currentRoute.getConfig()?.icon ?: R.drawable.photo_placeholder
                        )
                    } else {
                        ScreenTitleRow(
                            name = currentRoute.getConfig()?.titleRes ?: R.string.missing_title
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if(currentRoute is NavigationBarRoute.MyPets) {
                CustomFAB(
                    onClick = {}
                )
            }
        },
        bottomBar = {
            if(isBottomBar) {
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
        NavDisplay(
            modifier = modifier.padding(innerPadding),
            backStack = backStack,
            onBack = {
                if(backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<NavigationBarRoute.MyPets> {
                    MyPetsScreen()
                }
                entry<NavigationBarRoute.Public> {
                    PublicProfilesScreen()
                }
            }
        )

    }
}