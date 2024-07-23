package info.unlp.comunicadoraccesible.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
            text = "Tamaño del texto",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        CustomSlider(
            label = "Tamaño del texto",
            viewModel = viewModel,
            value = viewModel.textScale,
            onValueChange = { viewModel.updateTextScale(it) },
            valueRange = 1f..1.5f,
            modifier = Modifier.fillMaxWidth()
        )

        //volume control
        ScalableText(
            text = "Volumen de la aplicación",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        CustomSlider(
            label = "Volumen",
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
            text = "Tamaño de la interfaz",
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = viewModel
        )
        CustomSlider(
            label = "Tamaño de la Interfaz",
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
    label: String,
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
        steps = 3,
        valueRange = valueRange,
        modifier = modifier
            .clickable() {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            }
            .semantics {
                contentDescription = "$label slider"
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
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Text(
        textAlign = textAlign ?: TextAlign.Start,
        text = text,
        fontSize = textStyle.fontSize * accessibilityViewModel.textScale,
        modifier = modifier
    )
}

@Composable
fun ReadTextButton(
    selectedQuestion: String?,
    modifier: Modifier = Modifier,
    accessibilityViewModel: AccessibilityViewModel,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.size(78.dp * accessibilityViewModel.buttonSize),
            shape = CircleShape,
            enabled = selectedQuestion != null,
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(78.dp * accessibilityViewModel.buttonSize),
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Leer en voz alta",
                tint = if (selectedQuestion != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ScalableText(
            "Leer",
            textStyle = MaterialTheme.typography.titleLarge,
            accessibilityViewModel,
        )
    }
}

@Composable
fun VoiceToTextButton(
    modifier: Modifier = Modifier,
    accessibilityViewModel: AccessibilityViewModel,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(78.dp * accessibilityViewModel.buttonSize)
        ) {
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = "Escuchar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
