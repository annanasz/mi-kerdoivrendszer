package com.bme.surveysystemsupportedbyai.ui.filloutwithspeech

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.components.ApiKeyAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillOutWithSpeechScreen(
    viewModel: FillOutWithSpeechViewModel = hiltViewModel(), navigateBack: () -> Unit
) {
    var openMenu by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var permission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permission = granted
    }

    LaunchedEffect(viewModel) {
        viewModel.getOpenAIApiKey()
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ), title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.survey.id.isNotEmpty()) "Fill out ${uiState.survey.title}" else "",
                        fontFamily = Constants.titleFontFamily
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
            }, navigationIcon = {
                IconButton(onClick = { navigateBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },actions = {
                DropdownMenu(
                    expanded = openMenu,
                    onDismissRequest = {
                        openMenu = !openMenu
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text(
                            text = "Edit API key"
                        ) },
                        onClick = {
                            viewModel.apiKeyEdit()
                            openMenu = !openMenu
                        }
                    )
                }
            })
    }, content = { padding ->
        if (permission) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.survey.id.isEmpty()) LoadingAnimation()
                else Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState.fillOutState) {
                        FillOutWithSpeechViewModel.FillOutState.Thinking -> LoadingAnimation()
                        FillOutWithSpeechViewModel.FillOutState.UserSpeaking -> ShowingIcon(
                            Icons.Default.Mic
                        )

                        FillOutWithSpeechViewModel.FillOutState.Start -> ShowingIcon(imageVector = Icons.Default.WavingHand)
                        FillOutWithSpeechViewModel.FillOutState.Stop -> ShowingIcon(imageVector = Icons.Default.Pause)
                        FillOutWithSpeechViewModel.FillOutState.Sent -> ShowingIcon(imageVector = Icons.Default.Done)
                        FillOutWithSpeechViewModel.FillOutState.Initial -> ShowingIcon(
                            imageVector = Icons.Default.Android
                        )

                        else -> {
                            ShowingIcon(imageVector = Icons.Default.VolumeUp)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = uiState.textState,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(0.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight()
                    ) {
                        item {
                            Column(modifier = Modifier.fillMaxWidth(), content = {
                                Row {
                                    if (uiState.currentQuestionIndex >= 0 && uiState.currentQuestionIndex < uiState.survey.questions.size) {
                                        Text(
                                            text = "${uiState.currentQuestionIndex + 1}. ${uiState.currentQuestion.text}",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(
                                                vertical = 0.dp, horizontal = 16.dp
                                            )
                                        )

                                        if (uiState.currentQuestion.isRequired) {
                                            Text(
                                                text = " *",
                                                color = Color.Red,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))

                                when (uiState.currentQuestion.type) {
                                    "multiple_choice" -> {
                                        uiState.currentQuestion.options.forEach { option ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = uiState.answer?.response?.contains(
                                                        option
                                                    ) == true ,
                                                    onClick = { },
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(text = option)
                                            }
                                        }
                                    }

                                    "checkbox" -> {
                                        uiState.currentQuestion.options.forEach { option ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = uiState.answer?.response?.contains(
                                                        option
                                                    ) == true ,
                                                    onCheckedChange = {},
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(text = option)
                                            }
                                        }
                                    }

                                    "short_answer" -> {
                                        OutlinedTextField(
                                            value = uiState.answer?.response?.firstOrNull()?.toString() ?: "Response here",
                                            onValueChange = { /* Nothing to do here as it's read-only */ },
                                            enabled = false,
                                            singleLine = false,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                        )
                                    }
                                }

                            })
                        }
                    }
                }

                if (uiState.fillOutState != FillOutWithSpeechViewModel.FillOutState.Sent) {
                    Button(
                        onClick = { viewModel.onButtonClick() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(uiState.buttonState)
                    }
                }


            }
            if (uiState.apiKeyNeeded || uiState.apiKeyEdit)
                if(viewModel.showDialog.value)
                    ApiKeyAlertDialog(
                        onOkClick = { viewModel.onOkClick() },
                        onCancelClick = { if( uiState.apiKeyNeeded) navigateBack() else viewModel.showDialog.value = false},
                        onApiKeyChanged = { viewModel.updateApiKey(it) },
                        apiKey = viewModel.apiKey,
                        isApiKey = uiState.apiKeyEdit,
                        isLoading = viewModel.dialogLoading,
                        deleteApiKey = {viewModel.deleteApiKey()},
                        apiKeyText = viewModel.dialogText
                    )

        } else {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    })


}

@Composable
fun LoadingAnimation() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(180.dp)
            .padding(24.dp)
    )
}

@Composable
fun ShowingIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = "Microphone Icon", tint = Color.Black,
        modifier = Modifier
            .size(200.dp) // Adjust the size as needed
            .padding(4.dp)

    )
}


