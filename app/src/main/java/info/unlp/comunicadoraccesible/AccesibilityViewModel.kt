package info.unlp.comunicadoraccesible

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Locale

class AccessibilityViewModel : ViewModel() {
    var textScale by mutableStateOf(1.0f)
        private set

    fun updateTextScale(newScale: Float) {
        textScale = newScale
    }

    private var textToSpeech: TextToSpeech? = null
    // volume functionality
    var volume by mutableStateOf(1.0f)
        private set

    fun updateVolume(newVolume: Float) {
        volume = newVolume
    }
    //button size
    var buttonSize by mutableStateOf(1.0f)
        private set

    fun updateButtonSize(newSize: Float) {
        buttonSize = newSize

    }
    fun initializeTextToSpeech(context: Context) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech?.language = Locale("es", "ES")
                    textToSpeech?.setSpeechRate(1.0f)
                }
            }
        }
    }

    fun speakQuestion(question: String) {
        textToSpeech?.speak(question, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }


}