package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()) {
        // 3/4 section for questions
        Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
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
        ReadTextButton(
            accessibilityViewModel = accessibilityViewModel,
            selectedQuestion = text.text,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            onClick = {
                accessibilityViewModel.speakQuestion(text.text)
            }
        )

    }
}
