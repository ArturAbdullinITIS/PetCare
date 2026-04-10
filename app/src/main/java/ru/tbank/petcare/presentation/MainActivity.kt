package ru.tbank.petcare.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ru.tbank.petcare.presentation.root.PetCareAppRoot
import ru.tbank.petcare.presentation.screen.userprofile.UserProfileScreen
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseFirestore.setLoggingEnabled(true)
        setContent {
            PetCareAppRoot()
        }
    }
}
