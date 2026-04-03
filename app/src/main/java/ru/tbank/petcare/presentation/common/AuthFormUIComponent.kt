package ru.tbank.petcare.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tbank.petcare.R
import ru.tbank.petcare.presentation.ui.theme.PlusJakartaSans

@Composable
fun EmailTextField(
    value: String,
    emailError: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(32.dp),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.register_placeholder_email),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        },
        isError = emailError.isNotBlank(),
        supportingText = {
            if (emailError.isNotBlank()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@Composable
fun PasswordTextField(
    value: String,
    passwordError: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit,
    isPasswordVisible: Boolean,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(32.dp),
        singleLine = true,
        isError = passwordError.isNotBlank(),
        supportingText = {
            if (passwordError.isNotBlank()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onIconClick)

            if (isPasswordVisible) {
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = stringResource(R.string.eye_on_icon_description),
                )
            } else {
                Icon(
                    modifier = modifier,
                    painter = painterResource(R.drawable.ic_eye_off),
                    contentDescription = stringResource(R.string.eye_off),
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

// @Composable
// fun CustomButton(
//    text: String,
//    onClick: () -> Unit,
// ) {
//    Button(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp),
//        onClick = onClick,
//        shape = RoundedCornerShape(32.dp),
//        colors = ButtonDefaults.buttonColors(
//            contentColor = MaterialTheme.colorScheme.surface,
//            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
//        ),
//    ) {
//        Text(
//            text = text,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Icon(
//            imageVector = Icons.Filled.ArrowForward,
//            contentDescription = stringResource(R.string.arrow_forward_icon_description),
//        )
//    }
// }

@Composable
fun GoogleButton(
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = stringResource(R.string.google_icon),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.google_button),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun CustomDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        )
        Text(
            text = stringResource(R.string.or_divider),
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        )
        Divider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        )
    }
}

@Composable
fun AuthTitle(
    mainTitle: String,
    subTitle: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = mainTitle,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
        )
    }
}

@Composable
fun PetCareHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_pet_care_main),
            contentDescription = stringResource(R.string.pet_care),
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.pet_care),
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 24.sp,
        )
    }
}
