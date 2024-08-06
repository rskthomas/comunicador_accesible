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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.composables.ReadTextButton
import info.unlp.comunicadoraccesible.composables.ScalableText
import info.unlp.comunicadoraccesible.composables.VoiceToTextButton
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel


@Composable
fun KeyboardScreen(accessibilityViewModel: AccessibilityViewModel) {

    var text by remember { mutableStateOf(TextFieldValue("")) }
    val voiceLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val results =
                activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            text = TextFieldValue(results?.get(0) ?: "")
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center
                ),
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp * accessibilityViewModel.textScale)
                    .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                singleLine = false,
                maxLines = 10,
                label = {
                    ScalableText(
                        text = "Escriba su consulta aquí",
                        textStyle = MaterialTheme.typography.titleMedium,
                        accessibilityViewModel = accessibilityViewModel
                    )
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ReadTextButton(
                accessibilityViewModel = accessibilityViewModel,
                selectedQuestion = text.text,
                onClick = {
                    accessibilityViewModel.speakQuestion(text.text)
                }
            )

            Spacer(modifier = Modifier.width(100.dp))

            VoiceToTextButton(
                accessibilityViewModel = accessibilityViewModel,
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...")
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                    }
                    voiceLauncher.launch(intent)
                }
            )
        }
    }
}