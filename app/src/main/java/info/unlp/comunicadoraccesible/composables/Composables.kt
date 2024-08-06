package info.unlp.comunicadoraccesible.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel


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
            enabled = !selectedQuestion.isNullOrEmpty(),
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(78.dp * accessibilityViewModel.buttonSize),
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Leer en voz alta",
                tint = if (!selectedQuestion.isNullOrEmpty()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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
    text: String = "Escuchar",
    accessibilityViewModel: AccessibilityViewModel,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(
            modifier = Modifier
                .semantics { contentDescription = text }
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                .size(68.dp * accessibilityViewModel.buttonSize),
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(38.dp * accessibilityViewModel.buttonSize),
                imageVector = Icons.Filled.Mic,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ScalableText(
            text,
            textStyle = MaterialTheme.typography.titleMedium,
            accessibilityViewModel,
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
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ),
        value = value,
        onValueChange = onValueChange,
        steps = 3,
        valueRange = valueRange,
        modifier = modifier
            .clickable {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            }
            .semantics {
                contentDescription = "$label slider"
            }
            .height(48.dp * viewModel.buttonSize),
    )
}

@Composable
fun QuestionItem(
    accessibilityViewModel: AccessibilityViewModel,
    question: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Card(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .heightIn(
                min = 60.dp * accessibilityViewModel.buttonSize,
                max = 200.dp * accessibilityViewModel.buttonSize
            )
            .width(300.dp)
            .clickable {
                onClick()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            .semantics { contentDescription = "Question item: $question" }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterStart),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterVertically),
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = question,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * accessibilityViewModel.textScale,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
