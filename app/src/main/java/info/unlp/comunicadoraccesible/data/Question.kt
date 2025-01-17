package info.unlp.comunicadoraccesible.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Question(
    val text: String,
    val categoryId: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
