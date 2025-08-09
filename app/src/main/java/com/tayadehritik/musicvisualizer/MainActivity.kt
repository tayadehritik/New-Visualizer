package com.tayadehritik.musicvisualizer

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.audiofx.Visualizer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tayadehritik.musicvisualizer.ui.theme.MusicVisualizerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class MainActivity : ComponentActivity() {

    var _permissionGranted  = MutableStateFlow<Boolean>(false)
    val permissionGranted = _permissionGranted.asStateFlow()
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            _permissionGranted.value = isGranted
        }

    val myDataCaptureListener = MyDataCaptureListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val permissionGrantedNow = permissionGranted.collectAsState()
            val context = LocalContext.current
            MusicVisualizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(24.dp)) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED || permissionGrantedNow.value -> {
                                val visualizer = Visualizer(0)
                                visualizer.setDataCaptureListener(myDataCaptureListener, Visualizer.getMaxCaptureRate(), true, true)
                                visualizer.setEnabled(true)
                                PermissionGranted()
                            }
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity, Manifest.permission.RECORD_AUDIO
                            ) -> {
                                NeedPermission()
                            }
                            else -> {
                                NeedPermission()
                                requestPermissionLauncher.launch(
                                    Manifest.permission.RECORD_AUDIO
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeedPermission() {
    Text(
        text = "Need permission to record audio",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun PermissionGranted() {
    Text(
        text = "Permission granted",
        style = MaterialTheme.typography.titleLarge,
    )
}