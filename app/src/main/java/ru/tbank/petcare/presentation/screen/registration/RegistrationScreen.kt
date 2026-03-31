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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
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
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                mainTitle = stringResource(R.string.register),
                subTitle = stringResource(R.string.join_register_title)
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
                        onValueChange = { viewModel.processCommand(RegistrationCommand.InputEmail(it)) },
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
                        onValueChange = { viewModel.processCommand(RegistrationCommand.InputPassword(it)) },
                        onIconClick = { viewModel.processCommand(RegistrationCommand.ChangePasswordVisibility(!state.isPasswordVisibility)) },
                        isPasswordVisible = state.isPasswordVisibility,
                    )
                }
                CustomButton(
                    text = stringResource(R.string.register),
                    onClick = { viewModel.processCommand(RegistrationCommand.RegisterUserFromEmailAndPassword) }
                )
                CustomDivider()
                GoogleButton(
                    onClick = { viewModel.processCommand(RegistrationCommand.SignInWithGoogle(context)) }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
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
}
