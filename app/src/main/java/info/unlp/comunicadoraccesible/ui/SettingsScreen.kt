package info.unlp.comunicadoraccesible.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import info.unlp.comunicadoraccesible.composables.CustomSlider
import info.unlp.comunicadoraccesible.composables.ScalableText
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel

@Composable
fun SettingsScreen(viewModel: AccessibilityViewModel, navController: NavController) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ScalableText(
                        text = "Tamaño del texto",
                        textStyle = MaterialTheme.typography.bodyLarge,
                        accessibilityViewModel = viewModel
                    )
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        CustomSlider(
                            label = "Tamaño del texto",
                            viewModel = viewModel,
                            value = viewModel.textScale,
                            onValueChange = {
                                viewModel.updateTextScale(it)
                            },
                            valueRange = 1f..1.5f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ScalableText(
                        text = "Tamaño de la interfaz",
                        textStyle = MaterialTheme.typography.bodyLarge,
                        accessibilityViewModel = viewModel
                    )
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
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
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ScalableText(
                        text = "Pitch del Texto a Voz",
                        textStyle = MaterialTheme.typography.bodyLarge,
                        accessibilityViewModel = viewModel
                    )
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        CustomSlider(
                            label = "Pitch",
                            viewModel = viewModel,
                            value = viewModel.ttsPitch,
                            onValueChange = { viewModel.updatePitch(it) },
                            valueRange = -0.0f..2f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ScalableText(
                        text = "Velocidad",
                        textStyle = MaterialTheme.typography.bodyLarge,
                        accessibilityViewModel = viewModel
                    )
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        CustomSlider(
                            label = "Velocidad del Texto a Voz",
                            viewModel = viewModel,
                            value = viewModel.ttsSpeed,
                            onValueChange = { viewModel.updateSpeed(it) },
                            valueRange = 0.5f..1.5f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("editar")
            },
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 32.dp)
                .width(200.dp * viewModel.buttonSize)
                .align(Alignment.Bottom)
        ) {

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            )
            {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Questions")
                Spacer(modifier = Modifier.width(8.dp))
                ScalableText(text = "Editar preguntas", textStyle = MaterialTheme.typography.bodyMedium, viewModel)

            }
        }
    }
}


