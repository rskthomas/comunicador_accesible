package info.unlp.comunicadoraccesible

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.compose.AppTheme
import info.unlp.comunicadoraccesible.databinding.ActivityMainBinding
import info.unlp.comunicadoraccesible.ui.BottomNav

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set material design theme

        supportActionBar?.hide()
        setContent {
            AppTheme {
                BottomNav()
            }
        }
    }
}