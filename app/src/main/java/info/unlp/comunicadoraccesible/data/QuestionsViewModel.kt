package info.unlp.comunicadoraccesible.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer


val SAMPLE_CATEGORIES = listOf(
    "Asuntos Académicos",
    "Becas y ayudas económicas",
    "Fechas y plazos",
    "Ingreso a la UNLP",
    "Inscripción a materias",
    "Otros",
)
val SAMPLE_QUESTIONS = mapOf(
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


class QuestionsViewModel(private val appDao: AppDao) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _all_questions = MutableStateFlow<List<Question>>(emptyList())

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> get() = _questions

    private val _currentCategory = MutableStateFlow<Category?>(null)
    val currentCategory: StateFlow<Category?> get() = _currentCategory

    init {
        viewModelScope.launch {
            dropTables()
            if (appDao.isCategoriesEmpty()) {
                preloadDB()
            } else {
                retrieveData()
            }
        }
    }


    private suspend fun dropTables() {

        appDao.deleteAllCategories()
        appDao.deleteAllQuestions()

    }

    private fun preloadDB() {
        viewModelScope.launch {
            try {
                SAMPLE_CATEGORIES.forEach { categoryName ->
                    val category = Category(name = categoryName)
                    val categoryId = appDao.insertCategory(category)

                    SAMPLE_QUESTIONS[categoryName]?.forEach { questionText ->
                        val question = Question(text = questionText, categoryId = categoryId)
                        appDao.insertQuestion(question)
                    }
                }
            } catch (e: Exception) {
                Log.e("PreloadDB", "Error preloading database", e)
            } finally {
                retrieveData()
            }
        }
    }

    fun addCategory(category: String) {
        viewModelScope.launch {
            val newCategory = Category(name = category)
            appDao.insertCategory(newCategory)
            retrieveData()
        }
    }
    fun addQuestion(question: String, category: Category) {
        viewModelScope.launch {
            val newQuestion = Question(text = question, categoryId = category.id)
            appDao.insertQuestion(newQuestion)
            retrieveData()
        }
    }

    private fun retrieveData() {
        viewModelScope.launch {
            _categories.value = appDao.getAllCategories()
            _questions.value = appDao.getAllQuestions()
            _all_questions.value = _questions.value
            _currentCategory.value = _categories.value.firstOrNull()
            _currentCategory.value?.let { changeCategory(it) }
        }
    }

    fun deleteQuestion(question: String) {

        val _question = _questions.value.find { it.text == question } ?: return
        viewModelScope.launch {
            appDao.deleteQuestions(_question)
            retrieveData()
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            appDao.deleteCategory(category)
            retrieveData()
        }
    }
    fun updateQuestion(oldQuestion: String, newQuestion: String) {
        viewModelScope.launch {
            val _question = _questions.value.find { it.text == oldQuestion } ?: return@launch
            appDao.updateQuestion(_question.copy(text = newQuestion))
            retrieveData()
        }
    }

    fun updateCategory(oldCategory: Category, newCategory: String) {
        viewModelScope.launch {
            appDao.updateCategory(oldCategory.copy(name = newCategory))
            retrieveData()
        }
    }


    fun changeCategory(category: Category) {
        _currentCategory.value = category
        getQuestionsForCategory(category)
    }

    private fun getQuestionsForCategory(category: Category) {
        viewModelScope.launch {
            _questions.value = appDao.getQuestionsByCategory(category.id)
        }
    }

    //normalize the strings so the search is case insensitive and accent marks insensitive
    private fun normalizeString(input: String): String {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }

    fun queryQuestions(query: String) {
        val normalizedQuery = normalizeString(query)
        viewModelScope.launch {
            _questions.value = _all_questions.value.filter {
                normalizeString(it.text).contains(normalizedQuery, ignoreCase = true)
            }
        }
    }
}