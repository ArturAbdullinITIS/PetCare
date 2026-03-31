package ru.tbank.petcare.presentation.screen.login

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
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
    onNavigateToRegistration: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetCareHeader()

            Spacer(modifier = Modifier.weight(1f))

            AuthTitle(
                mainTitle = stringResource(R.string.login),
                subTitle = stringResource(R.string.welcome_back_login_title)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(48.dp))
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
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
                        onValueChange = { viewModel.processCommand(LoginCommand.InputEmail(it)) },
                    )
                }

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
                        onValueChange = { viewModel.processCommand(LoginCommand.InputPassword(it)) },
                        onIconClick = { viewModel.processCommand(LoginCommand.ChangePasswordVisibility(!state.isPasswordVisibility)) },
                        isPasswordVisible = state.isPasswordVisibility,
                    )
                }
                CustomButton(
                    text = stringResource(R.string.login),
                    onClick = { viewModel.processCommand(LoginCommand.LoginUserFromEmailAndPassword) }
                )
                CustomDivider()

                GoogleButton(
                    onClick = { viewModel.processCommand(LoginCommand.SignInWithGoogle(context)) }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
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
                        color = MaterialTheme.colorScheme.primary,
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
}
