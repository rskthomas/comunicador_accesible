package info.unlp.comunicadoraccesible.data

import android.content.Context
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AccessibilityViewModel(private val appDao: AppDao, context: Context) : ViewModel() {

    private var textToSpeech: TextToSpeech? = null
    private lateinit var audioManager: AudioManager


    init {
        try {
            getSettings()
        }finally {
            initializeTextToSpeech(context)
        }
    }
    private val _settings = MutableStateFlow<Settings>(Settings())
    val settings: StateFlow<Settings> get() = _settings

    var textScale by mutableFloatStateOf(1.0f)
    var buttonSize by mutableFloatStateOf(1.0f)
    var ttsVolume by mutableFloatStateOf(1.0f)
    var ttsPitch by mutableFloatStateOf(1.0f)
    var ttsSpeed by mutableFloatStateOf(1.0f)



    fun updateTextScale(newScale: Float) {
        textScale = newScale
        _settings.value.textScale = newScale
    }

    private fun updateSettings(settings: Settings) {

        viewModelScope.launch {
            appDao.updateSettings(settings)
        }
    }



    private fun getSettings() {

        viewModelScope.launch {
            try {
                if (appDao.isSettingsEmpty()) appDao.insertSettings(_settings.value)

                else _settings.value = appDao.getSettings()
            }finally {
                textScale = settings.value.textScale
                buttonSize = settings.value.buttonSize
                ttsPitch = settings.value.ttsPitch
                ttsSpeed = settings.value.ttsSpeed

            }
        }
    }


    fun updateVolume(newVolume: Float) {

        val normalizedVolume = (newVolume - 1f) / 0.8f
        // Convert normalizedVolume to the scale used by the AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val newVolumeLevel = (normalizedVolume * maxVolume).toInt()

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolumeLevel, 0)
    }

    fun updatePitch(newPitch: Float) {
        ttsPitch = newPitch
        applyTTSChanges()
    }

    fun updateSpeed(newSpeed: Float) {
        ttsSpeed = newSpeed
        applyTTSChanges()
    }

    private fun applyTTSChanges() {
        textToSpeech?.setPitch(ttsPitch)
        textToSpeech?.setSpeechRate(ttsSpeed)
        textToSpeech?.setLanguage(Locale(settings.value.ttsLanguage))
    }


    fun updateButtonSize(newSize: Float) {
        buttonSize = newSize
    }

    private fun initializeTextToSpeech(context: Context) {

        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (textToSpeech == null) {
            // Initialize TextToSpeech with settings
            textToSpeech = TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    //set langauge

                    applyTTSChanges()
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
        updateSettings(settings.value)
    }


}