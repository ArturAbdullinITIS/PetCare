package ru.tbank.petcare.presentation.common

import android.R.attr.name
import android.R.attr.onClick
import android.R.attr.text
import android.graphics.drawable.Icon
import android.util.Log.e
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.clickable(
                onClick = onClick
            ),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_icon),
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


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: (@Composable () -> Unit)? = null,
    text: String,
    enabled: Boolean
) {
    Button(
        modifier = modifier.height(52.dp),
        onClick = onClick,
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
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
                modifier = Modifier.size((iconSize/3).dp)
            )
        }
    }
}






