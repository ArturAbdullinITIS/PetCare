package ru.tbank.petcare.presentation.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.presentation.mapper.getIconStatusUI
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

@Composable
fun MainScreenTitleRow(
    @StringRes name: Int,
    @DrawableRes icon: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(R.string.screen_icon),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(name),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ScreenTitleRow(
    @StringRes name: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_icon),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(name),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun CustomFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(),
        contentColor = MaterialTheme.colorScheme.surface,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 4.dp,
            hoveredElevation = 6.dp,
            pressedElevation = 8.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_pet_button)
        )
    }
}

@Suppress("LongParameterList")
@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: (@Composable () -> Unit)? = null,
    text: String? = null,
    enabled: Boolean,
    bg: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    fg: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Button(
        modifier = modifier.height(52.dp),
        onClick = onClick,
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = fg
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        enabled = enabled
    ) {
        if (text != null) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        content?.invoke()
    }
}

@Preview
@Composable
fun CustomButtonPreview() {
    PetCareTheme {
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = "Custom Button",
            enabled = true
        )
    }
}

@Composable
fun IconStatusUI(
    status: IconStatus,
    modifier: Modifier = Modifier,
    iconSize: Int
) {
    val model = getIconStatusUI(status) ?: return

    Box(
        modifier = modifier
            .size(iconSize.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(model.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = model.imageVector,
                contentDescription = stringResource(R.string.icon_status),
                tint = model.iconTint,
                modifier = Modifier.size((iconSize / 2).dp)
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
fun ConfirmDeleteDialog(
    title: String,
    text: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

@Suppress("LongParameterList")
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    maxLines: Int,
    minLines: Int = 1,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyBoardOption: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LabelText(
            text = label
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Normal
                )
            },
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            maxLines = maxLines,
            singleLine = singleLine,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            keyboardOptions = keyBoardOption,
            minLines = minLines
        )
    }
}

@Composable
fun LabelText(
    text: String
) {
    Text(
        text = text.uppercase(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        letterSpacing = 1.1.sp
    )
}
