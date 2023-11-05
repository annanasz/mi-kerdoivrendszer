package com.bme.surveysystemsupportedbyai.filloutwithspeech

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillOutWithSpeechScreen(
    viewModel: FillOutWithSpeechViewModel = hiltViewModel(), navigateBack: () -> Unit
) {
    val survey by viewModel.survey
    val context = LocalContext.current
    val currentQuestion = viewModel.getCurrentQuestion()
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

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = if (survey.id.isNotEmpty()) "Fill out ${survey.title}" else "")
        }, navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
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
                if (survey.id.isEmpty()) LoadingAnimation()
                else Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (viewModel.fillOutState.value) {
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
                        text = viewModel.textState,
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
                                // Display the current question
                                Row {
                                    if (viewModel.currentQuestionIndex.intValue >= 0 && viewModel.currentQuestionIndex.intValue < survey.questions.size) {
                                        Text(
                                            text = "${viewModel.currentQuestionIndex.intValue + 1}. ${currentQuestion.text}",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(
                                                vertical = 0.dp, horizontal = 16.dp
                                            )
                                        )

                                        if (currentQuestion.isRequired) {
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

                                when (currentQuestion.type) {
                                    "multiple_choice" -> {
                                        currentQuestion.options.forEach { option ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Render the multiple-choice options
                                                RadioButton(
                                                    selected = false,
                                                    onClick = { },
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(text = option)
                                            }
                                        }
                                    }

                                    "checkbox" -> {
                                        currentQuestion.options.forEach { option ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = false,
                                                    onCheckedChange = {},
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(text = option)
                                            }
                                        }
                                    }

                                    "short_answer" -> {
                                        OutlinedTextField(
                                            value = "Response here",
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

                if (viewModel.fillOutState.value != FillOutWithSpeechViewModel.FillOutState.Sent) {
                    Button(
                        onClick = { viewModel.onButtonClick() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(viewModel.buttonState)
                    }
                }


            }

        } else {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    })
}

@Composable
fun LoadingAnimation() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(180.dp) // Adjust the size as needed
            .padding(24.dp)
    )
}

@Composable
fun ShowingIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector, // Adjust the icon as needed
        contentDescription = "Microphone Icon", tint = Color.Black, // Adjust the color as needed
        modifier = Modifier
            .size(200.dp) // Adjust the size as needed
            .padding(4.dp)

    )
}


