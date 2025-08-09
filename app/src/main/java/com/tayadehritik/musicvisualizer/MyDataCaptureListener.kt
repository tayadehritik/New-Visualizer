package com.tayadehritik.musicvisualizer

import android.media.audiofx.Visualizer
import android.util.Log

class MyDataCaptureListener:
Visualizer.OnDataCaptureListener{
    override fun onWaveFormDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
    }
    override fun onFftDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
        Log.d("MyDataCaptureListener", "onFftDataCapture:${p1?.contentToString()}")
    }
}
