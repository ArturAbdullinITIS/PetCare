package ru.tbank.petcare.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ru.tbank.petcare.presentation.navigation.NavHost
import ru.tbank.petcare.presentation.screen.mypets.MyPetsScreen
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme
import java.net.InetAddress
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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
