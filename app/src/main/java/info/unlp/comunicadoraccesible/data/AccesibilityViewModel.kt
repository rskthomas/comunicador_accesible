package info.unlp.comunicadoraccesible.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Locale

class AccessibilityViewModel : ViewModel() {


    private lateinit var audioManager: AudioManager

    var volume by mutableStateOf(1f)

    var textScale by mutableStateOf(1.0f)
        private set

    fun updateTextScale(newScale: Float) {
        textScale = newScale
    }

    private var textToSpeech: TextToSpeech? = null



    fun updateVolume(newVolume: Float) {
        volume = newVolume
        val normalizedVolume = (newVolume - 1f) / 0.8f
        // Convert normalizedVolume to the scale used by the AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val newVolumeLevel = (normalizedVolume * maxVolume).toInt()

        // Set the new volume level
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolumeLevel, 0)
    }
    //button size
    var buttonSize by mutableStateOf(1.0f)
        private set

    fun updateButtonSize(newSize: Float) {
        buttonSize = newSize

    }
    fun initializeTextToSpeech(context: Context) {
        //and also volume
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

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

    fun startVoiceInput(activity: Activity, requestCode: Int) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...")
        }
        activity.startActivityForResult(intent, requestCode)
    }

}