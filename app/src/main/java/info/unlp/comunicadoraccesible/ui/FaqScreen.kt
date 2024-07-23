package info.unlp.comunicadoraccesible.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import info.unlp.comunicadoraccesible.AccessibilityViewModel
import info.unlp.comunicadoraccesible.data.QuestionsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(accessibilityViewModel: AccessibilityViewModel, viewModel: QuestionsViewModel) {

    val categories = viewModel.categories
    var selectedQuestion by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(categories[1]) }
    var searchQuery by remember { mutableStateOf("") }
    val allQuestions by viewModel.questions.collectAsState()

    // Filter questions based on search query
    val filteredQuestions = if (searchQuery.isEmpty()) {
        allQuestions
    } else {
        allQuestions.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Column {

        ScrollableTabRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp * accessibilityViewModel.buttonSize),
            selectedTabIndex = categories.indexOf(selectedCategory),
        ) {

            categories.forEach { category ->

                if (category != categories[0]) {
                    Tab(
                        text = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            viewModel.changeCategory(category)
                            searchQuery = ""
                        }
                    )
                } else
                    Tab(
                        icon = {
                            Icon(
                                if (selectedCategory == "Search") Icons.Outlined.Search else Icons.Filled.Search,
                                contentDescription = "Buscar preguntas",
                                modifier = Modifier.size(24.dp)
                            )

                        },
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            searchQuery = ""
                        }
                    )
            }
        }

        if (selectedCategory == categories[0]) {
            SecondaryTabRow(selectedTabIndex = 0, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
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
                    onValueChange = { searchQuery = it },
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
                questions = filteredQuestions,
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
            .height(340.dp * accessibilityViewModel.buttonSize)
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
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier

            .heightIn(
                min = 60.dp * accessibilityViewModel.buttonSize,
                max = 90.dp * accessibilityViewModel.buttonSize
            )
            .fillMaxWidth()
            .clickable {
                onClick()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)
            )

            Text(
                modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically),
                text = question,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * accessibilityViewModel.textScale,
                //max text size is 20

            )
        }

    }
}

