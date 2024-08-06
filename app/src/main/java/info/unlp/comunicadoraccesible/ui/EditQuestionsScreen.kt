package info.unlp.comunicadoraccesible.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import info.unlp.comunicadoraccesible.composables.ScalableText
import info.unlp.comunicadoraccesible.data.AccessibilityViewModel
import info.unlp.comunicadoraccesible.data.QuestionsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionsScreen(
    accessibilityViewModel: AccessibilityViewModel,
    viewModel: QuestionsViewModel,
    navController: NavController
) {

    val categories = viewModel.categories.collectAsState().value
    val currentCategory = viewModel.currentCategory.collectAsState().value

    var selectedQuestion by remember { mutableStateOf<String?>(null) }
    var searchSelected by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val questions by viewModel.questions.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var onConfirm by remember { mutableStateOf<() -> Unit>({}) }

    var onConfirmEdit by remember { mutableStateOf<(String) -> Unit>({}) }

    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


    if (showDialog) {
        CustomDialog(
            title = title,
            text = text,
            onConfirm = {
                onConfirm()
                selectedQuestion = null
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    if (showEditDialog) {
        EditDialog(
            title = title,
            description = description,
            placeholder = text,
            onConfirm = { newText ->
                onConfirmEdit(newText)
                showEditDialog = false
            },
            onDismiss = {
                showEditDialog = false
            }
        )
    }

    Column {
        Column {
            ScrollableTabRow(
                edgePadding = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp * accessibilityViewModel.buttonSize),
                selectedTabIndex = if (currentCategory == null || searchSelected) 1 else categories.indexOf(
                    currentCategory
                ) + 2,
            ) {
                //back arrow tab
                Tab(
                    modifier = Modifier.background(
                        if (searchSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.shapes.extraSmall
                    ),
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a las opciones",
                            modifier = Modifier.size(24.dp * accessibilityViewModel.buttonSize)
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate("opciones")
                    }
                )

                Tab(
                    icon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Buscar preguntas",
                            modifier = Modifier.size(28.dp * accessibilityViewModel.buttonSize),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    selected = searchSelected,
                    onClick = {
                        searchSelected = true
                        searchQuery = ""
                        selectedQuestion = null
                        viewModel.queryQuestions("")
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
                            selectedQuestion = null
                            searchQuery = ""
                        }
                    )
                }
            }

            AnimatedVisibility(searchSelected) {
                SecondaryTabRow(
                    selectedTabIndex = 0, modifier = Modifier
                        .fillMaxWidth()
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(3.5f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(4.dp)
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Column {
                    ScalableButton(
                        icon = Icons.Outlined.Folder,
                        enabled = !searchSelected,
                        text = "Eliminar Categoría",
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        accessibilityViewModel = accessibilityViewModel,
                        onClick = {
                            selectedQuestion = null
                            title = "Eliminar Categoría"
                            text =
                                "¿Está seguro que desea eliminar la categoría: " + currentCategory?.name + "?. Se eliminarán todas las preguntas asociadas."
                            onConfirm = {
                                currentCategory?.let {
                                    viewModel.deleteCategory(it)
                                }
                            }
                            showDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    ScalableButton(
                        icon = Icons.Outlined.Folder,
                        enabled = !searchSelected,
                        text = "Editar Categoría",
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        accessibilityViewModel = accessibilityViewModel,
                        onClick = {

                            selectedQuestion = null
                            title = "Editar Categoría"
                            text = currentCategory?.name.orEmpty()
                            onConfirmEdit = { newText ->
                                currentCategory?.let {
                                    viewModel.updateCategory(it, newText)
                                }
                            }

                            showEditDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    ScalableButton(
                        icon = Icons.Outlined.Folder,
                        text = "Agregar Categoría",
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        accessibilityViewModel = accessibilityViewModel,
                        onClick = {
                            selectedQuestion = null
                            title = "Agregar Categoría"
                            text = ""
                            onConfirmEdit = { newText ->
                                viewModel.addCategory(newText)
                            }
                            showEditDialog = true
                        }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {

                    ScalableButton(

                        icon = Icons.Outlined.QuestionMark,
                        text = "Agregar Pregunta",
                        accessibilityViewModel = accessibilityViewModel,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        onClick = {
                            title = "Agregar Pregunta"
                            description =
                                "¿Quiere agregar una pregunta a la categoría ${currentCategory?.name}?"
                            text = ""
                            onConfirmEdit = { newText ->
                                currentCategory?.let {
                                    viewModel.addQuestion(newText, it)
                                }
                            }
                            showEditDialog = true
                            selectedQuestion = null
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    ScalableButton(
                        icon = Icons.Outlined.QuestionMark,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        text = "Editar Pregunta",
                        accessibilityViewModel = accessibilityViewModel,
                        enabled = selectedQuestion != null,
                        onClick = {
                            title = "Editar Pregunta"
                            text = selectedQuestion.orEmpty()
                            description = selectedQuestion.orEmpty()
                            onConfirmEdit = { newText ->
                                viewModel.updateQuestion(selectedQuestion.orEmpty(), newText)
                            }
                            showEditDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ScalableButton(
                        icon = Icons.Outlined.QuestionMark,
                        text = "Eliminar Pregunta",
                        accessibilityViewModel = accessibilityViewModel,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        enabled = selectedQuestion != null,
                        onClick = {
                            title = "Eliminar Pregunta"
                            text =
                                "¿Está seguro que desea eliminar la pregunta: $selectedQuestion?"
                            onConfirm = {
                                viewModel.deleteQuestion(selectedQuestion.orEmpty())
                            }
                            showDialog = true
                        }
                    )

                }


            }
        }
    }

}

@Composable
fun CustomDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditDialog(
    title: String,
    description: String,
    placeholder: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(placeholder) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                Text(description)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = { onConfirm(text) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ScalableButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    text: String,
    enabled: Boolean = true,
    accessibilityViewModel: AccessibilityViewModel,
    onClick: () -> Unit
) {
    Button(
        colors = colors,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .width(180.dp * accessibilityViewModel.buttonSize)
            .height(56.dp * accessibilityViewModel.buttonSize),
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp * accessibilityViewModel.buttonSize)
        )
        Spacer(modifier = Modifier.width(8.dp))
        ScalableText(
            text = text,
            textStyle = MaterialTheme.typography.bodyLarge,
            accessibilityViewModel = accessibilityViewModel
        )
    }
}
