package ru.tbank.petcare.presentation.screen.login

import android.R.attr.enabled
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.AuthTitle
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.common.CustomDivider
import ru.tbank.petcare.presentation.common.EmailTextField
import ru.tbank.petcare.presentation.common.GoogleButton
import ru.tbank.petcare.presentation.common.PasswordTextField
import ru.tbank.petcare.presentation.common.PetCareHeader

@Composable
fun LoginScreen(
    onNavigateToRegistration: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    LoginContent(
        onNavigateToRegistration = onNavigateToRegistration,
        onLoginSuccess = onLoginSuccess,
        onNavigateToOnboarding = onNavigateToOnboarding
    )
}
private const val HALF_WEIGHT = 0.5f

@Composable
fun LoginContent(
    onNavigateToRegistration: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
            viewModel.processCommand(LoginCommand.ResetState)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { events ->
            when (events) {
                is LoginEvents.ShowOnboarding -> {
                    onNavigateToOnboarding()
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PetCareHeader(
            onHelpClick = {
                viewModel.processCommand(LoginCommand.CheckOnboarding)
            }
        )

        Spacer(modifier = Modifier.weight(HALF_WEIGHT))

        AuthTitle(
            mainTitle = stringResource(R.string.login),
            subTitle = stringResource(R.string.welcome_back_login_title)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(vertical = 16.dp, horizontal = 16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.email).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 16.dp)
                )
                EmailTextField(
                    value = state.email,
                    emailError = state.emailError,
                    onValueChange = { viewModel.processCommand(LoginCommand.InputEmail(it)) },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.password).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 16.dp)
                )
                PasswordTextField(
                    value = state.password,
                    passwordError = state.passwordError,
                    onValueChange = { viewModel.processCommand(LoginCommand.InputPassword(it)) },
                    onIconClick = {
                        viewModel.processCommand(
                            LoginCommand.ChangePasswordVisibility(!state.isPasswordVisibility)
                        )
                    },
                    isPasswordVisible = state.isPasswordVisibility,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.processCommand(LoginCommand.LoginUserFromEmailAndPassword)
                },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.arrow_forward_icon_description)
                    )
                },
                text = stringResource(R.string.login),
                enabled = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomDivider()

            Spacer(modifier = Modifier.height(16.dp))

            GoogleButton(
                isLoading = state.isLoading,
                onClick = { viewModel.processCommand(LoginCommand.SignInWithGoogle(context)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dont_have_account),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.register),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        onNavigateToRegistration()
                    }
                )
            }
        }
    }
}
