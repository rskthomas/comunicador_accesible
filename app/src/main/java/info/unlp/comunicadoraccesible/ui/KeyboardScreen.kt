package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
    Row(modifier = Modifier.fillMaxSize()) {
        // 3/4 section for questions
        Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                    .heightIn(min = 250.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(8.dp),
                singleLine = false,
                maxLines = 10,
                placeholder = {
                    ScalableText(
                        text = "Escriba aquí...",
                        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                        accessibilityViewModel = accessibilityViewModel
                    )
                },

                )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            Button(
                modifier = androidx.compose.ui.Modifier.size(78.dp * accessibilityViewModel.buttonSize),
                shape = CircleShape,
                onClick = { accessibilityViewModel.speakQuestion(text.text) }
            ) {
                Icon(
                    modifier = androidx.compose.ui.Modifier.size(48.dp * accessibilityViewModel.buttonSize),
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Leer en voz alta",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ScalableText(
                "Leer",
                textStyle = MaterialTheme.typography.bodyLarge,
                accessibilityViewModel,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
