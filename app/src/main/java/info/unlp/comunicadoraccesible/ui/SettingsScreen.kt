package info.unlp.comunicadoraccesible.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel
import info.unlp.comunicadoraccesible.composables.CustomSlider
import info.unlp.comunicadoraccesible.composables.ScalableText

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

