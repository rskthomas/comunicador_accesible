package info.unlp.comunicadoraccesible.ui

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import info.unlp.comunicadoraccesible.AccessibilityViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(accessibilityViewModel: AccessibilityViewModel) {

    val viewModel = QuestionsViewModel()
    val categories = viewModel.categories
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedQuestion by remember { mutableStateOf<String?>(null) }

    Column {
        TabRow(selectedTabIndex = categories.indexOf(selectedCategory)) {
            categories.forEach{ category ->
                Tab(
                    text = { ScalableText(text = category, textStyle = MaterialTheme.typography.bodyMedium, accessibilityViewModel)},
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        viewModel.changeCategory(category) }
                )
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            // 3/4 section for questions
            Box(modifier = Modifier.weight(3f)) {
                Column {
                    QuestionList(
                        accessibilityViewModel = accessibilityViewModel,
                        questionsViewModel = viewModel,
                        onSelectQuestion = { question ->
                            selectedQuestion = question
                        },
                        selectedQuestion = selectedQuestion
                    )
                }
            }

            val context = LocalContext.current
            Button(
                onClick = { viewModel.textToSpeech(context, selectedQuestion?: "") },
                modifier = Modifier.weight(1f)
            ) {
                ScalableText("Read Aloud", textStyle = MaterialTheme.typography.bodyMedium, accessibilityViewModel)
            }
        }

    }
}
@Composable
fun QuestionList(accessibilityViewModel: AccessibilityViewModel, questionsViewModel :QuestionsViewModel , onSelectQuestion: (String) -> Unit, selectedQuestion: String?) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        items (questionsViewModel.questions.size) { index ->
            val question = questionsViewModel.questions[index]
            val isSelected = question == selectedQuestion
            QuestionItem(accessibilityViewModel, question = question, isSelected = isSelected, onClick = { onSelectQuestion(question) })
        }
    }
}

@Composable
fun QuestionItem(accessibilityViewModel: AccessibilityViewModel, question: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .background(if (isSelected) Color.LightGray else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        ScalableText(
            text = question,
            textStyle = MaterialTheme.typography.bodyMedium,
            accessibilityViewModel = accessibilityViewModel,
            modifier = Modifier.padding(16.dp)
        )
        Checkbox(checked = isSelected, onCheckedChange = null)
    }
}
class QuestionsViewModel : ViewModel() {
    val categories = listOf("Category 1", "Category 2", "Category 3")
    private val allQuestions = mapOf(
        "Category 1" to listOf("Question 1", "Question 2", "Question 3", "Question 4"),
        "Category 2" to listOf("Question 5", "Question 6", "Question 7", "Question 8"),
        "Category 3" to listOf("Question 7", "Question 8", "Question 9", "Question 10")
    )
    var currentCategory by mutableStateOf("Category 1")


    private var textToSpeech:TextToSpeech? = null
    val questions: List<String>
        get() = allQuestions[currentCategory] ?: emptyList()

    fun changeCategory(category: String) {
        currentCategory = category
    }
    fun textToSpeech(context: Context, question: String){

        textToSpeech = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.let { txtToSpeech ->
                    txtToSpeech.language = Locale("es", "ES")
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        question,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }
    }
}
