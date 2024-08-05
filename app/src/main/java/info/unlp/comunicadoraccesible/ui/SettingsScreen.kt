package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import info.unlp.comunicadoraccesible.composables.ScalableText
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: AccessibilityViewModel, navController: NavController) {
    var checkBoxSetting by remember { mutableStateOf(false) }
    var textValue by remember { mutableFloatStateOf(1f) }
    var uiValue by remember { mutableFloatStateOf(1f) }
    var ttsPitch by remember { mutableFloatStateOf(1f) }
    var ttsSpeed by remember { mutableFloatStateOf(1f) }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = { Text("Opciones") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingCheckbox("Modo Alto Contraste", checkBoxSetting) {
                checkBoxSetting = it
            }

            HorizontalDivider()
            ScalableText(
                text ="Interfaz",
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                accessibilityViewModel = viewModel
            )
            SettingSlider("Tamaño de letra", viewModel.textScale, valueRange = 1.0f .. 1.4f) { viewModel.updateTextScale(it) }
            SettingSlider("Tamaño de la interfaz", viewModel.buttonSize, valueRange = 1.0f .. 1.4f) { viewModel.buttonSize = it }

            HorizontalDivider()
            ScalableText(
                "Texto a voz",
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                accessibilityViewModel = viewModel
            )

            SettingSlider("Tono de voz", viewModel.ttsPitch, valueRange = 0.5f..1.5f) {
                viewModel.updatePitch(it)
            }
            SettingSlider("Velocidad", viewModel.ttsSpeed, valueRange = 0.5f .. 1.5f ) { viewModel.updateSpeed(it) }

            HorizontalDivider()
            ScalableText(
                "Preguntas y categorías",
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                accessibilityViewModel = viewModel
            )

            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate("editar") },
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Questions")
                Spacer(modifier = Modifier.width(8.dp))
                ScalableText(
                    text = "Editar preguntas",
                    textStyle = MaterialTheme.typography.bodyLarge,
                    viewModel
                )

            }
        }


    }
}


@Composable
fun SettingCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(label)
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.padding(vertical = 8.dp),
            valueRange = valueRange
        )
    }
}

