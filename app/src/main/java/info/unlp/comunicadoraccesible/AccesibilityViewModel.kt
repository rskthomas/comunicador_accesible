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

    // Add other accessibility settings as needed

}