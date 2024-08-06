package info.unlp.comunicadoraccesible.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.input.TextFieldValue
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
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val questions by viewModel.questions.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        Column {
            ScrollableTabRow(
                edgePadding = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp * accessibilityViewModel.buttonSize),
                selectedTabIndex = if (currentCategory == null || searchSelected) 0 else categories.indexOf(
                    currentCategory
                ) + 1,
            ) {

                Tab(
                    modifier = Modifier.background(if (searchSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.extraSmall),
                    icon = {
                        Icon(
                            Icons.Filled.Search ,
                            contentDescription = "Buscar preguntas",
                            modifier = Modifier.size(28.dp * accessibilityViewModel.buttonSize),
                            tint = if (searchSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                        )
                    },
                    selected = searchSelected,
                    onClick = {
                        searchSelected = true
                        searchQuery = TextFieldValue("")
                        viewModel.queryQuestions(searchQuery.text)
                    }
                )

                if (categories.isNotEmpty()) categories.forEach { category ->
                    Tab(
                        text = {
                            ScalableText(
                                text = category.name,
                                textStyle = MaterialTheme.typography.bodyLarge ,
                                accessibilityViewModel = accessibilityViewModel
                            )
                        },
                        selected = currentCategory == category,
                        onClick = {
                            viewModel.changeCategory(category)
                            searchSelected = false
                            searchQuery = TextFieldValue("")
                            selectedQuestion = null
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
                            viewModel.queryQuestions(it.text)
                        },
                        label = { Text("Buscar preguntas") }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(3.4f)
                    .fillMaxWidth()
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

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                ReadTextButton(
                    accessibilityViewModel = accessibilityViewModel,
                    selectedQuestion = selectedQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = {
                        accessibilityViewModel.speakQuestion(selectedQuestion.orEmpty())
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
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

    val gridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(400.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
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
                    }
                )
            }
        }

    }
}


