package com.tayadehritik.musicvisualizer

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tayadehritik.musicvisualizer.presentation.PermissionGranted
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

    var visualizer: Visualizer? = null
    val myDataCaptureListener = MyDataCaptureListener()
    var audioSession:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaPlayer = MediaPlayer.create(this, R.raw.test_music)
        audioSession = mediaPlayer.audioSessionId
        enableEdgeToEdge()
        setContent {
            val permissionGrantedNow = permissionGranted.collectAsState()
            val magnitudes = myDataCaptureListener.magnitudes.collectAsState()
            val context = LocalContext.current

            MusicVisualizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(24.dp)) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED || permissionGrantedNow.value -> {
                                mediaPlayer.start()
                                PermissionGranted(magnitudes.value)
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

    override fun onResume() {
        super.onResume()
        Log.d("OnResume", "onResume called")
        if(permissionGranted.value || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && audioSession != null) {
            visualizer = Visualizer(audioSession!!)
            visualizer?.setDataCaptureListener(myDataCaptureListener, Visualizer.getMaxCaptureRate(), true, true)
            visualizer?.setEnabled(true)
        }
    }

    override fun onPause() {
        super.onPause()
        visualizer?.release()
        visualizer = null
        Log.d("OnPause", "onPause called")
    }

    override fun onDestroy() {
        super.onDestroy()
        visualizer?.release()
        visualizer = null
        Log.d("OnDestroy", "onDestroy called")
    }
}

@Composable
fun NeedPermission() {
    Text(
        text = "Need permission to record audio",
        style = MaterialTheme.typography.titleLarge,
    )
}