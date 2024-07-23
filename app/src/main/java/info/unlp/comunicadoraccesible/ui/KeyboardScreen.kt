package info.unlp.comunicadoraccesible.ui

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.AccessibilityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyboardScreen(accessibilityViewModel: AccessibilityViewModel) {

    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        // 3/4 section for questions
        Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(42.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                    .heightIn(min = 250.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(8.dp),
                singleLine = false,
                maxLines = 10,
                supportingText = {
                    ScalableText(
                        text = "Escriba su pregunta",
                        textStyle = MaterialTheme.typography.titleLarge,
                        accessibilityViewModel = accessibilityViewModel
                    )
                }
            )
        }

        // Define the ActivityResultLauncher
        val voiceLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val results = activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                text = TextFieldValue(results?.get(0) ?: "")
            }
        }


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            ReadTextButton(
                accessibilityViewModel = accessibilityViewModel,
                selectedQuestion = text.text,
                onClick = {
                    accessibilityViewModel.speakQuestion(text.text)
                }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add space between buttons

            VoiceToTextButton(
                accessibilityViewModel = accessibilityViewModel,
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...")
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                    }
                    voiceLauncher.launch(intent)
                }
            )
        }

    }
}


