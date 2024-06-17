package info.unlp.comunicadoraccesible.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import info.unlp.comunicadoraccesible.AccessibilityViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FAQScreen(accessibilityViewModel: AccessibilityViewModel, viewModel: QuestionsViewModel) {

    val categories = viewModel.categories
    var selectedQuestion by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(categories.first()) }
    val questions by viewModel.questions.collectAsState()

    Column {
        TabRow(selectedTabIndex = categories.indexOf(selectedCategory)) {
            categories.forEach { category ->
                Tab(
                    text = {
                        ScalableText(
                            text = category,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            accessibilityViewModel
                        )
                    },
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        viewModel.changeCategory(category)
                    }
                )
            }
        }
        Row(modifier = Modifier.fillMaxSize()) {
            // 3/4 section for questions
            Box(modifier = Modifier.weight(3f)) {
                QuestionList(
                    accessibilityViewModel = accessibilityViewModel,
                    questions = questions,
                    onSelectQuestion = { question ->
                        selectedQuestion = question
                    },
                    selectedQuestion = selectedQuestion
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
            ) {
                Button(
                    modifier = Modifier.size(78.dp * accessibilityViewModel.buttonSize),
                    shape = CircleShape,
                    enabled = selectedQuestion != null,
                    onClick = { accessibilityViewModel.speakQuestion(selectedQuestion ?: "") }
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp * accessibilityViewModel.buttonSize),
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Leer en voz alta",
                        tint = if (selectedQuestion != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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

}

@Composable
fun QuestionList(
    accessibilityViewModel: AccessibilityViewModel,
    questions: List<String>,
    onSelectQuestion: (String) -> Unit,
    selectedQuestion: String?
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(9.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp * accessibilityViewModel.buttonSize)
            .padding(8.dp)
    ) {

        items(questions.size) { index ->
            val question = questions[index]
            val isSelected = question == selectedQuestion
            QuestionItem(
                accessibilityViewModel,
                question = question,
                isSelected = isSelected,
                onClick = {
                    onSelectQuestion(question)
                })

        }
    }
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
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 80.dp * accessibilityViewModel.buttonSize,
                    max = 150.dp * accessibilityViewModel.buttonSize
                )
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Checkbox(checked = isSelected, onCheckedChange = null)
            ScalableText(
                text = question,
                textStyle = MaterialTheme.typography.bodyMedium,
                accessibilityViewModel = accessibilityViewModel,
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
            )
        }

    }
}

class QuestionsViewModel : ViewModel() {

    val categories = listOf(
        "Asuntos Académicos",
        "Becas y ayudas económicas",
        "Fechas y plazos",
        "Ingreso a la UNLP",
        "Inscripción a materias"
    )
    private val allQuestions = mapOf(
        "Asuntos Académicos" to listOf(
            "¿Cómo puedo solicitar un certificado de alumno regular?",
            "¿Cómo puedo solicitar un certificado analítico?",
            "¿Cómo puedo solicitar un certificado de materias aprobadas?",
            "¿Cómo puedo solicitar un certificado de materias cursadas?",
            "¿Cómo puedo solicitar un certificado de promedio?"
        ),
        "Becas y ayudas económicas" to listOf(
            "¿Cómo puedo solicitar una beca?",
            "¿Cómo puedo solicitar una ayuda económica?",
            "¿Cómo puedo solicitar una beca de transporte?",
            "¿Cómo puedo solicitar una beca de comedor?",
            "¿Cómo puedo solicitar una beca de material de estudio?"
        ),
        "Fechas y plazos" to listOf(
            "¿Cuándo son las fechas de inscripción a materias?",
            "¿Cuándo son las fechas de exámenes finales?",
            "¿Cuándo son las fechas de cursada?",
            "¿Cuándo son las fechas de vacaciones de invierno?",
            "¿Cuándo son las fechas de vacaciones de verano?"
        ),
        "Ingreso a la UNLP" to listOf(
            "¿Cómo puedo inscribirme a la UNLP?",
            "¿Cuáles son los requisitos para inscribirme a la UNLP?",
            "¿Cuándo son las fechas de inscripción a la UNLP?",
            "¿Cuándo son las fechas de exámenes de ingreso a la UNLP?",
            "¿Cuándo son las fechas de cursada en la UNLP?"
        ),
        "Inscripción a materias" to listOf(
            "¿Cómo puedo inscribirme a materias?",
            "¿Cuáles son los requisitos para inscribirme a materias?",
            "¿Cuándo son las fechas de inscripción a materias?",
            "¿Cuándo son las fechas de exámenes de materias?",
            "¿Cuándo son las fechas de cursada de materias?"
        ),
    )

    private val _questions = MutableStateFlow<List<String>>(emptyList())
    val questions: StateFlow<List<String>> get() = _questions


    private val _currentCategory = MutableStateFlow<String>("")

    init {
        loadQuestions("Asuntos Académicos")
    }

    fun changeCategory(category: String) {
        _currentCategory.value = category
        loadQuestions(_currentCategory.value)
    }

    private fun loadQuestions(category: String) {
        _questions.value = allQuestions[category] ?: emptyList()
    }


}
