package info.unlp.comunicadoraccesible.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {

    @Query("SELECT EXISTS(SELECT 1 FROM categories LIMIT 1)")
    suspend fun isCategoriesEmpty(): Boolean

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Insert
    suspend fun insertQuestion(question: Question)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    suspend fun getQuestionsByCategory(categoryId: Int): List<Question>
}