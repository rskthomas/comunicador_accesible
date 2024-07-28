package info.unlp.comunicadoraccesible.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//a room entity for the settings
@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var ttsSpeed: Float = 1.0f,
    var ttsPitch: Float = 1.0f,
    var ttsLanguage: String = "es",
    var ttsVoice: String = "es-ES",
    var buttonSize: Float = 1.0f,
    var textScale: Float = 1.0f

)
