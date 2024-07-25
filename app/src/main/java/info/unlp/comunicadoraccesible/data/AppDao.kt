package info.unlp.comunicadoraccesible.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {


    @Query("SELECT EXISTS(SELECT 1 FROM categories LIMIT 1)")
    suspend fun isCategoriesEmpty(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM questions LIMIT 1)")
    suspend fun isQuestionsEmpty(): Boolean

    //Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Insert
    suspend fun insertQuestion(question: Question)

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

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    suspend fun getQuestionsByCategory(categoryId: Long): List<Question>

}