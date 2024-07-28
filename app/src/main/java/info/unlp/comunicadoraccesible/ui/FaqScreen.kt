package info.unlp.comunicadoraccesible.ui


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.composables.QuestionItem
import info.unlp.comunicadoraccesible.composables.ReadTextButton
import info.unlp.comunicadoraccesible.composables.ScalableText
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel
import info.unlp.comunicadoraccesible.data.Question
import info.unlp.comunicadoraccesible.data.QuestionsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(accessibilityViewModel: AccessibilityViewModel, viewModel: QuestionsViewModel) {

    val categories = viewModel.categories.collectAsState().value
    val currentCategory = viewModel.currentCategory.collectAsState().value


    var selectedQuestion by remember { mutableStateOf<String?>(null) }
    var searchSelected by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val questions by viewModel.questions.collectAsState()

    Log.d("FAQScreen", "Categories: $categories")
    Log.d("FAQScreen", "Current category: $currentCategory")
    Log.d("FAQScreen", "Questions: $questions")
    Log.d("FAQScreen", "Selected question: $selectedQuestion")
    Log.d("FAQScreen", "Search selected: $searchSelected")
    Log.d("FAQScreen", "Search query: $searchQuery")


    Column {
        Column {

            ScrollableTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp * accessibilityViewModel.buttonSize),
                selectedTabIndex = if (currentCategory == null || searchSelected) 0 else categories.indexOf(
                    currentCategory
                ) + 1,
            ) {
                //Searchtab
                Tab(
                    icon = {
                        Icon(
                            if (searchSelected) Icons.Outlined.Search else Icons.Filled.Search,
                            contentDescription = "Buscar preguntas",
                            modifier = Modifier.size(24.dp * accessibilityViewModel.buttonSize)
                        )
                    },
                    selected = searchSelected,
                    onClick = {
                        searchSelected = true
                        searchQuery = ""
                    }
                )

                if (categories.isNotEmpty()) categories.forEach { category ->
                    Tab(
                        text = {
                            ScalableText(
                                text = category.name,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                accessibilityViewModel = accessibilityViewModel
                            )
                        },
                        selected = currentCategory == category,
                        onClick = {
                            viewModel.changeCategory(category)
                            searchSelected = false
                            searchQuery = ""
                        }
                    )
                }
            }

            AnimatedVisibility(searchSelected) {
                SecondaryTabRow(
                    selectedTabIndex = 0, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 60.dp * accessibilityViewModel.buttonSize,
                                max = 80.dp * accessibilityViewModel.buttonSize
                            ),

                        singleLine = true,
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.queryQuestions(it)
                        },
                        label = { Text("Buscar preguntas") }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(2.7f)
                    .align(Alignment.CenterVertically)
            ) {
                QuestionList(
                    accessibilityViewModel = accessibilityViewModel,
                    questions = questions,
                    onSelectQuestion = { question ->
                        selectedQuestion = question
                    },
                    selectedQuestion = selectedQuestion
                )
            }

            ReadTextButton(
                accessibilityViewModel = accessibilityViewModel,
                selectedQuestion = selectedQuestion,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                onClick = {
                    accessibilityViewModel.speakQuestion(selectedQuestion.orEmpty())
                }
            )
        }
    }
}


@Composable
fun QuestionList(
    accessibilityViewModel: AccessibilityViewModel,
    questions: List<Question>,
    onSelectQuestion: (String) -> Unit,
    selectedQuestion: String?
) {

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ScalableText(
                text = "No se encontraron preguntas",
                textStyle = MaterialTheme.typography.bodyMedium,
                accessibilityViewModel = accessibilityViewModel
            )
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(9.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        modifier = Modifier
            .fillMaxSize()
            .height(340.dp * accessibilityViewModel.buttonSize)
            .padding(24.dp)
    ) {

        items(questions.size) { index ->
            val question = questions[index]
            val isSelected = question.text == selectedQuestion
            QuestionItem(
                accessibilityViewModel,
                question = question.text,
                isSelected = isSelected,
                onClick = {
                    onSelectQuestion(question.text)
                })

        }
    }
}


