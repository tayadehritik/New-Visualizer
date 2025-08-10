package com.tayadehritik.musicvisualizer

import android.media.audiofx.Visualizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyDataCaptureListener:
Visualizer.OnDataCaptureListener{
    val _fftData = MutableStateFlow<Byte>(0)
    val fftData = _fftData.asStateFlow()
    override fun onWaveFormDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
    }
    override fun onFftDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
        Log.d("MyDataCaptureListener", "onFftDataCapture:${p1?.contentToString()}")
        p1?.let {
            _fftData.value = p1[0] // Assuming you want to capture the first byte of FFT data
        }
    }
}
