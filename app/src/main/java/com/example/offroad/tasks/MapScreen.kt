package com.example.offroad.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.offroad.R
import com.example.offroad.util.LoadingContent
import com.example.offroad.util.MapTopAppBar

@Composable
fun MapScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MapTopAppBar(
                openDrawer = openDrawer,
                onRefresh = { } // TODO: handle this
            )
        },
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {}) { // TODO: fix this
                Icon(Icons.Filled.Add, stringResource(id = R.string.center_map))
            }
        }
    ) { paddingValues ->
        MapContent(
            onRefresh = {}, // TODO: fix this
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun MapContent(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = false,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
        ) {
            Text(
                text = "Works",
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.list_item_padding),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
                style = MaterialTheme.typography.h6
            )
        }
    }
}
