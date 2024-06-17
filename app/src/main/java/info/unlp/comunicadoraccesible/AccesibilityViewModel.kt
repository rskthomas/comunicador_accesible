package info.unlp.comunicadoraccesible

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AccessibilityViewModel : ViewModel() {
    var textScale by mutableStateOf(1.0f)
        private set

    fun updateTextScale(newScale: Float) {
        textScale = newScale
    }

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

}