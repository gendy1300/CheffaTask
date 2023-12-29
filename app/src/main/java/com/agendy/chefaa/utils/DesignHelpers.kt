package com.agendy.chefaa.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agendy.chefaa.R
import com.agendy.chefaa.utils.theme.Purple40
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
    overflow: TextOverflow = TextOverflow.Clip
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
        lineHeight = lineHeight,
        overflow = overflow,
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


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isEnabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = Color.Gray,
    colors: androidx.compose.material.TextFieldColors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = backgroundColor,
        focusedBorderColor = Purple40
    ),
    textStyle: TextStyle = TextStyle(
        fontSize = 12.sp,

        ),

    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(70.dp),

        enabled = isEnabled,
        singleLine = singleLine,
        textStyle = textStyle,
        colors = colors,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        shape = shape,
        keyboardOptions = keyboardOptions

    )
}


@Composable
fun LabelText(text: String) {
    TextWithFont(
        text = text,
        fontSize = 10.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold
    )
}







