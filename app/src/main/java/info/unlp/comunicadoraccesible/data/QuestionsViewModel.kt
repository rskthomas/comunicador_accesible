package info.unlp.comunicadoraccesible.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestionsViewModel(private val appDao: AppDao) : ViewModel() {

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> get() = _categories

    private val _questions = MutableStateFlow<List<String>>(emptyList())
    val questions: StateFlow<List<String>> get() = _questions

    private val _currentCategory = MutableStateFlow<String>("Search")

    init {
        preloadDatabase()
        loadCategories()
    }

    private fun preloadDatabase(){
        viewModelScope.launch {
            if (appDao.isCategoriesEmpty()) {
                val categories = listOf(
                    "Buscar",
                    "Asuntos Académicos",
                    "Becas y ayudas económicas",
                    "Fechas y plazos",
                    "Ingreso a la UNLP",
                    "Inscripción a materias",
                    "Otros",
                    "Contacto"
                )
                categories.forEach {
                    appDao.insertCategory(Category(name = it))
                }

            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromDb = appDao.getAllCategories()
            _categories.value = categoriesFromDb.map { it.name }
            if (categoriesFromDb.isNotEmpty()) {
                changeCategory(categoriesFromDb.first().name)
            }
        }
    }

    fun changeCategory(category: String) {
        _currentCategory.value = category
        loadQuestionsForCategory(category)
    }

    private fun loadQuestionsForCategory(categoryName: String) {
        viewModelScope.launch {
            val category = appDao.getAllCategories().find { it: Category ->
                it.name == categoryName
            }
            category?.let { it: Category ->
                val questionsFromDb = appDao.getQuestionsByCategory(it.id)
                _questions.value = questionsFromDb.map { question: Question -> question.text }
            }
        }
    }

    fun queryQuestions(query: String): List<String> {
        return _questions.value.filter { it.contains(query, ignoreCase = true) }
    }
}

class QuestionsViewModel2 : ViewModel() {

    val categories = listOf(
        "Buscar",
        "Asuntos Académicos",
        "Becas y ayudas económicas",
        "Ingreso a la UNLP",
        "Inscripción a materias",
        "Fechas y plazos",
        " Otros",
        "Contacto"
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
