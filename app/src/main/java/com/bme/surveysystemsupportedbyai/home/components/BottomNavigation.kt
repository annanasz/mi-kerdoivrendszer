package com.bme.surveysystemsupportedbyai.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SendToMobile
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.bme.surveysystemsupportedbyai.core.Constants

data class BottomNavigationItem(
    val title: String,
    val text: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(navController: NavController, selectedItemIndex:Int, changeSelecetedItemIndex: (Int)->Unit) {
    val items = listOf(
        BottomNavigationItem(
            title = Constants.MY_SURVEYS_SCREEN,
            text = "My surveys",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            title = Constants.SENT_SURVEYS_SCREEN,
            text = "Sent",
            selectedIcon = Icons.Filled.SendToMobile,
            unselectedIcon = Icons.Outlined.SendToMobile,
            //badgeCount = 45
        ),
        BottomNavigationItem(
            title = Constants.FILLED_OUT_SURVEYS_SCREEN,
            text = "Filled out",
            selectedIcon = Icons.Filled.Done,
            unselectedIcon = Icons.Outlined.Done,
        ),
        BottomNavigationItem(
            title = Constants.INBOX_SURVEYS_SCREEN,
            text = "Inbox",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email,
        ),
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    changeSelecetedItemIndex(index)
                    navController.navigate(item.title) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.text)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                })
               }
    }
}
