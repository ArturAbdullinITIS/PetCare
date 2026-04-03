package ru.tbank.petcare.presentation.screen.registration

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.common.AuthTitle
import ru.tbank.petcare.presentation.common.CustomButton
import ru.tbank.petcare.presentation.common.CustomDivider
import ru.tbank.petcare.presentation.common.EmailTextField
import ru.tbank.petcare.presentation.common.GoogleButton
import ru.tbank.petcare.presentation.common.PasswordTextField
import ru.tbank.petcare.presentation.common.PetCareHeader

@Composable
fun RegistrationScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    RegistrationContent(
        onNavigateToLogin = onNavigateToLogin,
        onRegisterSuccess = onRegisterSuccess
    )
}

@Composable
fun RegistrationContent(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        PetCareHeader()

        Spacer(modifier = Modifier.height(40.dp))

        AuthTitle(
            mainTitle = stringResource(R.string.register),
            subTitle = stringResource(R.string.join_register_title)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(32.dp))
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
                    onValueChange = { viewModel.processCommand(RegistrationCommand.InputEmail(it)) },
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
                    placeholder = stringResource(R.string.password),
                    onValueChange = { viewModel.processCommand(RegistrationCommand.InputPassword(it)) },
                    onIconClick = {
                        viewModel.processCommand(
                            RegistrationCommand.ChangePasswordVisibility(!state.isPasswordVisibility)
                        )
                    },
                    isPasswordVisible = state.isPasswordVisibility,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.repeat_password).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 16.dp)
                )
                PasswordTextField(
                    value = state.repeatPassword,
                    passwordError = state.repeatPasswordError,
                    placeholder = stringResource(R.string.repeat_password),
                    onValueChange = { viewModel.processCommand(RegistrationCommand.InputRepeatPassword(it)) },
                    onIconClick = {
                        viewModel.processCommand(
                            RegistrationCommand.ChangeRepeatPasswordVisibility(!state.isRepeatPasswordVisibility)
                        )
                    },
                    isPasswordVisible = state.isRepeatPasswordVisibility,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.processCommand(RegistrationCommand.RegisterUserFromEmailAndPassword)
                },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.arrow_forward_icon_description)
                    )
                },
                text = stringResource(R.string.register),
                enabled = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            CustomDivider()
            Spacer(modifier = Modifier.height(16.dp))
            GoogleButton(
                onClick = { viewModel.processCommand(RegistrationCommand.SignInWithGoogle(context)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.already_have_account),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.login),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        onNavigateToLogin()
                    }
                )
            }
        }
    }
}
