package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.AccessibilityViewModel

@Composable
fun SettingsScreen(viewModel: AccessibilityViewModel ) {
    Column(modifier = Modifier.padding(16.dp)) {
        ScalableText(
            text = "Text scale",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        Slider(
            value = viewModel.textScale,
            onValueChange = { viewModel.updateTextScale(it) },
            valueRange = 0.9f..1.5f,
            modifier = Modifier.fillMaxWidth()
        )

        //volume control
        ScalableText(
            text = "Volume",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        Slider(
            value = viewModel.volume,
            onValueChange = { viewModel.updateVolume(it) },
            valueRange = 0.0f..1.0f,
            modifier = Modifier.fillMaxWidth()
        )
        //button size
        ScalableText(
            text = "Button size",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        Slider(
            value = viewModel.buttonSize,
            onValueChange = { viewModel.updateButtonSize(it) },
            valueRange = 0.5f..1.5f,
            modifier = Modifier.fillMaxWidth()
        )



    }
}
@Composable
fun ScalableText(
    text: String,
    textStyle: TextStyle,
    accessibilityViewModel: AccessibilityViewModel,
    modifier: Modifier = Modifier
) {
    Text(text = text, fontSize = textStyle.fontSize * accessibilityViewModel.textScale, modifier = modifier)
}