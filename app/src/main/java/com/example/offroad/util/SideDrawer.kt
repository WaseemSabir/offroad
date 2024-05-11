package com.example.offroad.util

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.offroad.R
import com.example.offroad.Destinations
import com.example.offroad.NavigationActions
import com.google.accompanist.appcompattheme.AppCompatTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: NavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToTasks = { navigationActions.navigateToMap() },
                navigateToStatistics = { navigationActions.navigateToStatistics() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    navigateToTasks: () -> Unit,
    navigateToStatistics: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        DrawerHeader()
        DrawerButton(
            imageVector = Icons.Filled.Map,
            label = stringResource(id = R.string.map_title),
            isSelected = currentRoute == Destinations.MAP_ROUTE,
            action = {
                navigateToTasks()
                closeDrawer()
            }
        )
        DrawerButton(
            imageVector = Icons.Filled.Person,
            label = stringResource(id = R.string.profile_title),
            isSelected = currentRoute == Destinations.PROFILE_ROUTE,
            action = {
                navigateToStatistics()
                closeDrawer()
            }
        )
    }
}

@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(primaryDarkColor)
            .padding(
                vertical = 16.dp,
                horizontal = dimensionResource(id = R.dimen.horizontal_margin)
            )
    ) {
        Text(
            text = stringResource(id = R.string.navigation_view_header_title),
            color = MaterialTheme.colors.surface
        )
    }
}

@Composable
private fun DrawerButton(
    imageVector: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }

    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null, // decorative
                tint = tintColor
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = tintColor
            )
        }
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
    AppCompatTheme {
        Surface {
            AppDrawer(
                currentRoute = Destinations.MAP_ROUTE,
                navigateToTasks = {},
                navigateToStatistics = {},
                closeDrawer = {}
            )
        }
    }
}
