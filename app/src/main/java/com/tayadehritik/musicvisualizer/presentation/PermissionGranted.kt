package com.tayadehritik.musicvisualizer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
    magnitudesAvg: FloatArray,
    magnitudesMax: FloatArray,
) {
    Text(
        text = "Permission granted",
        style = MaterialTheme.typography.titleLarge,
    )
    Column {
        if(magnitudesAvg.isNotEmpty()) {
            Row(modifier = Modifier.height(200.dp)) {
                magnitudesAvg.forEach { magnitude ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(magnitude.toInt().dp)
                            .background(Color.Red)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(magnitudesMax.isNotEmpty()) {
            Row(modifier = Modifier.height(200.dp)) {
                magnitudesMax.forEach { magnitude ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(magnitude.toInt().dp)
                            .background(Color.Blue)
                    )
                }
            }
        }
    }

}