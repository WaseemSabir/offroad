package com.example.offroad.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.offroad.R
import com.example.offroad.data.Profile
import com.example.offroad.util.LoadingContent
import com.example.offroad.util.ProfileTopAppBar
import com.google.accompanist.appcompattheme.AppCompatTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation

@Composable
fun ProfileScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ProfileTopAppBar(openDrawer) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            viewModel.loadProfile()
        }

        ProfileContent(
            loading = uiState.isLoading,
            profile = uiState.profile,
            updateProfile = viewModel::updateProfile,
            onRefresh = viewModel::loadProfile,
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ProfileContent(
    loading: Boolean,
    profile: Profile?,
    updateProfile: (pui: ProfileUpdateInput) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val commonModifier = modifier
        .fillMaxWidth()
        .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
    var localPui by remember {
        mutableStateOf(
            ProfileUpdateInput(
                imageUrl = profile?.imageUrl ?: "",
                fullName = profile?.fullName ?: "",
                email = profile?.email ?: "",
                birthdayDate = profile?.birthdayDate ?: "",
                phoneNumber = profile?.phoneNumber ?: "",
                subscribed = profile?.subscribed ?: true
            )
        )
    }

    LoadingContent(
        loading = loading,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        Column(
            commonModifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = rememberImagePainter(
                    data = localPui.imageUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = "Profile Image",
                modifier = Modifier.size(120.dp)
            )

            // About Me Heading
            Text(
                text = "About Me",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )

            // Full Name Input
            OutlinedTextField(
                value = localPui.fullName,
                onValueChange = { localPui = localPui.copy(fullName = it) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Birthday Picker
            OutlinedTextField(
                value = localPui.birthdayDate,
                onValueChange = { localPui = localPui.copy(birthdayDate = it) },
                label = { Text("Birthday") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pick Date",
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Email Input
            OutlinedTextField(
                value = localPui.email,
                onValueChange = { localPui = localPui.copy(email = it) },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )

            // Phone Input
            OutlinedTextField(
                value = localPui.phoneNumber,
                onValueChange = { localPui = localPui.copy(phoneNumber = it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // Newsletter Subscription Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Checkbox(
                    checked = localPui.subscribed,
                    onCheckedChange = { localPui = localPui.copy(subscribed = it) }
                )
                Text(
                    text = "I want to subscribe to the newsletter",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Save Changes Button
            Button(
                onClick = { updateProfile(localPui) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Preview
@Composable
fun ProfileContentPreview() {
    AppCompatTheme {
        Surface {
            ProfileContent(
                loading = false,
                profile = Profile(
                    imageUrl = "https://fastly.picsum.photos/id/10/200/300.jpg",
                    fullName = "Waseem Sabir",
                    email = "waseemsabir99@gmail.com",
                    birthdayDate = "2000-07-10",
                    phoneNumber = "+923151156034",
                    subscribed = true
                ),
                updateProfile = {},
                onRefresh = {}
            )
        }
    }
}

@Preview
@Composable
fun ProfileContentEmptyPreview() {
    AppCompatTheme {
        Surface {
            ProfileContent(
                loading = false,
                profile = null,
                updateProfile = {},
                onRefresh = {}
            )
        }
    }
}
