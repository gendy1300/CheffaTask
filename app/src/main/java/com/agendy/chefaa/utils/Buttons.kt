package com.agendy.chefaa.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.agendy.chefaa.R


@Composable
fun CloseIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.close_icon),
        contentDescription = null,
        modifier = modifier
            .size(32.dp)
            .clickable {
                onClick()
            }
    )
}


@Composable
fun BackIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.back_icon),
        contentDescription = null,
        modifier = modifier
            .size(32.dp)
            .clickable {
                onClick()
            },

    )
}

@Composable
fun AppButton(
    text: String,
    height: Dp = 44.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    color: Color = Color.Black,
    padding: PaddingValues = PaddingValues(0.dp),
    widthPresent: Float = 1f,
    onClick: () -> Unit
) {

    val modifier = Modifier
        .height(height)
        .background(shape = shape, color = color)
        .padding(paddingValues = padding)
        .fillMaxWidth(widthPresent)


    Button(
        modifier = modifier, onClick = {
            onClick()
        }, colors = ButtonDefaults.buttonColors(containerColor = color)

    ) {
        TextWithFont(
            text = text, color = Color.White, fontWeight = FontWeight.Medium
        )
    }
}


