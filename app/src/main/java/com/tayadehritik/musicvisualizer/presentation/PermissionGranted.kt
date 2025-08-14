package com.tayadehritik.musicvisualizer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PermissionGranted(
    magnitudes: FloatArray,
) {
    Text(
        text = "Permission granted",
        style = MaterialTheme.typography.titleLarge,
    )
    if(magnitudes.isNotEmpty()) {
        Box(modifier = Modifier.width(20.dp).height(magnitudes[0].toInt().dp).background(Color.Red))
    }
}