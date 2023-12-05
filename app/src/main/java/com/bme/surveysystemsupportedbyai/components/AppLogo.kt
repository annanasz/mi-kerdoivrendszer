package com.bme.surveysystemsupportedbyai.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bme.surveysystemsupportedbyai.R

@Composable
fun AppLogo() {
    Image(
        painter = painterResource(id = R.drawable.surveasy_logo),
        contentDescription = "App logo",
        Modifier.size(240.dp, 240.dp)
    )
}