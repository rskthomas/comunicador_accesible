package info.unlp.comunicadoraccesible.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)