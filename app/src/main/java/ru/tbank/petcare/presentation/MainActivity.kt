package ru.tbank.petcare.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ru.tbank.petcare.presentation.navigation.NavHost
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseFirestore.setLoggingEnabled(true)
        setContent {
            PetCareTheme {
                NavHost()
            }
        }
    }
}
