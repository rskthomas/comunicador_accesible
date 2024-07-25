package info.unlp.comunicadoraccesible.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> get() = _questions


    private val _currentCategory = MutableStateFlow<Category?>(null)
    val currentCategory: StateFlow<Category?> get() = _currentCategory

    init {

        preloadDB()
        retrieveData()

    }

    private fun deleteDB() {
        viewModelScope.launch {
            appDao.deleteAllCategories()
            appDao.deleteAllQuestions()
            Log.d("QuestionsViewModel", "Database deleted")
        }
    }

    private fun preloadDB() {
        deleteDB()
        viewModelScope.launch {
            try {
                SAMPLE_CATEGORIES.forEach { categoryName ->
                    val category = Category(name = categoryName)
                    val categoryId = appDao.insertCategory(category) // Get the category id

                    Log.d("PreloadDB", "Inserted category $categoryName with id $categoryId")

                    SAMPLE_QUESTIONS[categoryName]?.forEach { questionText ->
                        val question = Question(text = questionText, categoryId = categoryId)
                        appDao.insertQuestion(question)
                        Log.d("PreloadDB", "Inserted question $questionText")
                    }
                }
            } catch (e: Exception) {
                Log.e("PreloadDB", "Error preloading database", e)
            } finally {
                retrieveData()
            }
        }

    }

    private fun retrieveData() {
        viewModelScope.launch {
            // Load categories from the db and update the state
            _categories.value = appDao.getAllCategories()
            _currentCategory.value = _categories.value.firstOrNull()
            changeCategory(_currentCategory.value ?: return@launch)
            _questions.value = appDao.getAllQuestions()
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

    fun queryQuestions(query: String) {
        viewModelScope.launch {
            _questions.value =
                appDao.getAllQuestions().filter { it.text.contains(query, ignoreCase = true) }
        }
    }
}

