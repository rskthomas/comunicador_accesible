package info.unlp.comunicadoraccesible.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDao {


    //Checks
    @Query("SELECT (SELECT COUNT(*) FROM categories) == 0")
    suspend fun isCategoriesEmpty(): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM questions) == 0")
    suspend fun isQuestionsEmpty(): Boolean
    //check if settings is empty
    @Query("SELECT (SELECT COUNT(*) FROM settings) == 0")
    suspend fun isSettingsEmpty(): Boolean


    //Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Insert
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)

    @Delete
    suspend fun deleteCategories(vararg category: Category)

    @Delete
    suspend fun deleteQuestions(vararg question: Question)


    //delete all
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()




    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM settings")
    suspend fun getSettings(): Settings

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    suspend fun getQuestionsByCategory(categoryId: Long): List<Question>


    //update
    @Update
    suspend fun updateSettings(settings: Settings)
}
