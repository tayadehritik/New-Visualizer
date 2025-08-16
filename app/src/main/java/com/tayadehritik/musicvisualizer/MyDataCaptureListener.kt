package com.tayadehritik.musicvisualizer

import android.media.audiofx.Visualizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.exp
import kotlin.math.roundToInt

class MyDataCaptureListener:
Visualizer.OnDataCaptureListener{
    // Number of logarithmic frequency bands for visualization
    private val NUM_BANDS = 128
    
    val _magnitudes = MutableStateFlow<FloatArray>(floatArrayOf())
    val magnitudes = _magnitudes.asStateFlow()

    override fun onWaveFormDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
    }
    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
        Log.d("MyDataCaptureListener", "onFftDataCapture:${fft?.contentToString()}")
        fft?.let {
            val n: Int = fft.size
            val magnitudes = FloatArray(n / 2 + 1)
            val phases = FloatArray(n / 2 + 1)

            magnitudes[0] = abs(fft[0].toFloat())  // DC
            magnitudes[n / 2] = abs(fft[1].toFloat()) // Nyquist
            phases[0] = 0f
            phases[n / 2] = 0f // Nyquist phase is undefined, set to 0

            for (k in 1..<n / 2) {
                val i = k * 2
                magnitudes[k] = hypot(fft[i].toFloat(), fft[i + 1].toFloat())
                phases[k] = atan2(fft[i + 1].toFloat(), fft[i].toFloat())
            }
            
            // Apply logarithmic frequency binning
            val logBands = FloatArray(NUM_BANDS)
            val minIndex = 1
            val maxIndex = n / 2 - 1
            val logMin = ln(minIndex.toFloat())
            val logMax = ln(maxIndex.toFloat())
            val logRange = logMax - logMin

            for(index in 0..<NUM_BANDS) {
                val progress = index.toFloat() / NUM_BANDS
                val nextIndexProgress = (index + 1).toFloat() / NUM_BANDS
                val startIndex = exp(logMin + (logRange * progress)).roundToInt()
                val endIndex = exp(logMin + (logRange * nextIndexProgress)).roundToInt()
                val maxVal = magnitudes.slice(startIndex..endIndex).maxOrNull() ?: 0f
                logBands[index] = maxVal

            }

            _magnitudes.value = logBands
        }
    }
}
