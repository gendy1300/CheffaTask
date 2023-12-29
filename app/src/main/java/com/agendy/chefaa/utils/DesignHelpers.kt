package com.agendy.chefaa.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agendy.chefaa.R
import com.agendy.chefaa.utils.theme.poppinsFonts
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun Margin(height: Dp = 0.dp, width: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
    )
}


@Composable
fun AppDivider(isVertical: Boolean = false, color: Color = Color.Gray) {

    Divider(thickness = 1.dp, color = color)

}


@Composable
fun TextWithFont(
    modifier: Modifier = Modifier,
    text: String,
    fontFamily: FontFamily = poppinsFonts,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 13.sp,
    style: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    color: Color = Color.Black,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )

}


@Composable
fun LoadingComponent() {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        LoadingAnimation()
    }
}


@Composable
fun ErrorComponent(
    exception: Exception,
    popStatus: MutableState<Boolean>,
) {

    if (popStatus.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .clickable {
                    popStatus.value = false
                },
            Alignment.Center,
        ) {
            Card(
                modifier = Modifier
                    .width(100.dp)
                    .height(400.dp)
            ) {
                TextWithFont(
                    text = exception.message.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        )
                        .wrapContentSize()
                )
            }

        }
    }

}


@Composable
fun LoadingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_mint))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    isEnabled: Boolean = true,
    label: @Composable() (() -> Unit)? = null,
    placeholder: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Gray,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,

        ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    textStyle: TextStyle = TextStyle(
        fontSize = 12.sp,

        ),
    backgroundColor: Color = Color.Gray,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp)
) {

    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(
                shape = shape,
                color = backgroundColor
            )
            .height(44.dp),
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        enabled = isEnabled,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions
    ) { innerTextField ->

        TextFieldDefaults.TextFieldDecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = singleLine,
            enabled = isEnabled,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 13.dp),
            label = label,
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            colors = colors,

            )
    }
}







