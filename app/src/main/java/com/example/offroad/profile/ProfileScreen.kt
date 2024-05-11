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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                imageUrl = "",
                fullName = "",
                email = "",
                birthdayDate = "",
                phoneNumber = "",
                subscribed = true
            )
        )
    }

    LaunchedEffect(profile) {
        profile?.let {
            localPui = ProfileUpdateInput(
                imageUrl = it.imageUrl,
                fullName = it.fullName,
                email = it.email,
                birthdayDate = it.birthdayDate,
                phoneNumber = it.phoneNumber,
                subscribed = it.subscribed
            )
        }
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
                        placeholder(R.drawable.profile_picture_placeholder)
                        error(R.drawable.profile_picture_placeholder)
                        transformations(CircleCropTransformation())
                    }

                ),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 4.dp)
            )

            // About Me Heading
            Text(
                text = "About Me",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.W900),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
            )

            // Full Name Input
            ProfileTextField(
                label = "Full Name",
                value = localPui.fullName,
                onValueChange = { localPui = localPui.copy(fullName = it) }
            )

            // Birthday Picker
            ProfileTextField(
                label = "Birthday",
                value = localPui.birthdayDate,
                onValueChange = { localPui = localPui.copy(fullName = it) },
                trailingIcon = Icons.Default.DateRange,
                trailingIconDesc = "Date Picker"
            )

            // Email Input
            ProfileTextField(
                label = "E-mail",
                value = localPui.email,
                onValueChange = { localPui = localPui.copy(email = it) }
            )


            // Phone Input
            ProfileTextField(
                label = "Phone",
                value = localPui.phoneNumber,
                onValueChange = { localPui = localPui.copy(phoneNumber = it) }
            )

            // Newsletter Subscription Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(all = 0.dp)
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = localPui.subscribed,
                    onCheckedChange = { localPui = localPui.copy(subscribed = it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = R.color.colorPurple)
                    ),
                    modifier = Modifier.padding(all = 0.dp)
                )
                Text(
                    text = "I want to subscribe to the newsletter.",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                )
            }

            // Save Changes Button
            Button(
                onClick = { updateProfile(localPui) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.colorPurple),
                    contentColor = colorResource(id = R.color.colorPrimary)
                )
            ) {
                Text("Save Changes")
            }
        }
    }
}


@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (newVal: String) -> Unit,
) {
    Text(text = label, style = MaterialTheme.typography.body1)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp, color = LocalTextStyle.current.color),
        cursorBrush = SolidColor(Color.Black),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        label,
                        style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                    )
                }
                innerTextField()  // This is where the actual TextField will be displayed
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (newVal: String) -> Unit,
    trailingIcon: ImageVector,
    trailingIconDesc: String
) {
    Text(text = label, style = MaterialTheme.typography.body1)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp, color = LocalTextStyle.current.color),
        cursorBrush = SolidColor(Color.Black),
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        label,
                        style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                    )
                }
                innerTextField()  // This is where the actual TextField will be displayed
                IconButton(
                    onClick = { /* Define what happens when icon is clicked, e.g., open date picker */ },
                    modifier = Modifier.size(18.dp)  // Adjust the size of the icon if necessary
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = trailingIconDesc,
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp)
    )
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
