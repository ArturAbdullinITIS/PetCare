package ru.tbank.petcare.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ru.tbank.petcare.presentation.root.PetCareAppRoot
import ru.tbank.petcare.presentation.root.SplashViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseFirestore.setLoggingEnabled(true)
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.startDestination.value == null
        }
        setContent {
            PetCareAppRoot(splashViewModel = splashViewModel)
        }
    }
}
