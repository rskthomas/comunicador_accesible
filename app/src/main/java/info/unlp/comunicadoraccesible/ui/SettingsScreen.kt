package info.unlp.comunicadoraccesible.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.AccessibilityViewModel

@Composable
fun SettingsScreen(viewModel: AccessibilityViewModel) {
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .width(300.dp)
    ) {
        ScalableText(
            text = "Text scale",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        CustomSlider(
            viewModel = viewModel,
            value = viewModel.textScale,
            onValueChange = { viewModel.updateTextScale(it) },
            valueRange = 1f..1.5f,
            modifier = Modifier.fillMaxWidth()
        )

        //volume control
        ScalableText(
            text = "Volume",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        CustomSlider(
            viewModel = viewModel,
            value = viewModel.volume,
            onValueChange = { newVolume ->
                viewModel.updateVolume(newVolume)
            },
            valueRange = 1f..1.8f,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )

        //button size
        ScalableText(
            text = "Button size",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        CustomSlider(
            viewModel = viewModel,
            value = viewModel.buttonSize,
            onValueChange = { viewModel.updateButtonSize(it) },
            valueRange = 1f..1.5f,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun CustomSlider(
    viewModel: AccessibilityViewModel,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    Slider(
        value = value,
        onValueChange = onValueChange,

        valueRange = valueRange,
        modifier = modifier.clickable(enabled = true) {
           view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
        },
        colors = SliderDefaults.colors(
            thumbColor = lerp(
                MaterialTheme.colorScheme.onSecondary,
                MaterialTheme.colorScheme.secondary,
                (value - 1f) / 0.8f
            )
        )
    )
}

@Composable
fun ScalableText(
    text: String,
    textStyle: TextStyle,
    accessibilityViewModel: AccessibilityViewModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = textStyle.fontSize * accessibilityViewModel.textScale,
        modifier = modifier
    )
}