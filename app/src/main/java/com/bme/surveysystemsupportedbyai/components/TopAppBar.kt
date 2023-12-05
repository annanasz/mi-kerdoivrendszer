package com.bme.surveysystemsupportedbyai.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.bme.surveysystemsupportedbyai.core.Constants.SIGN_OUT_ITEM
import com.bme.surveysystemsupportedbyai.R
import com.bme.surveysystemsupportedbyai.core.Constants.titleFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    signOut: () -> Unit
) {
    var openMenu by remember { mutableStateOf(false) }
    TopAppBar (
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = titleFontFamily
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            openMenu = !openMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            }
        },
        actions = {
            DropdownMenu(
                expanded = openMenu,
                onDismissRequest = {
                    openMenu = !openMenu
                }
            ) {
                DropdownMenuItem(
                    text = { Text(
                        text = SIGN_OUT_ITEM
                    ) },
                    onClick = {
                        signOut()
                        openMenu = !openMenu
                    }
                )
            }
        }
    )
}