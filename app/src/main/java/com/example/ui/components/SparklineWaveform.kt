package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun SparklineWaveform(
    dataPoints: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier,
    fillGradient: Boolean = true,
    strokeWidth: Float = 4f
) {
    Canvas(modifier = modifier) {
        if (dataPoints.size < 2) return@Canvas

        val minVal = (dataPoints.minOrNull() ?: 0f)
        val maxVal = (dataPoints.maxOrNull() ?: 1f)
        val range = (maxVal - minVal).coerceAtLeast(0.01f)

        val width = size.width
        val height = size.height
        val stepX = width / (dataPoints.size - 1)

        val path = Path()
        val fillPath = Path()

        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedY = 1f - ((value - minVal) / range)
            val y = normalizedY * (height * 0.8f) + (height * 0.1f)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        fillPath.lineTo(width, height)
        fillPath.close()

        if (fillGradient) {
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.35f),
                        lineColor.copy(alpha = 0.0f)
                    )
                )
            )
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw glowing point at the newest reading (right edge)
        val lastVal = dataPoints.last()
        val lastX = width
        val lastY = (1f - ((lastVal - minVal) / range)) * (height * 0.8f) + (height * 0.1f)

        drawCircle(
            color = lineColor.copy(alpha = 0.4f),
            radius = strokeWidth * 2f,
            center = Offset(lastX, lastY)
        )
        drawCircle(
            color = lineColor,
            radius = strokeWidth * 1.1f,
            center = Offset(lastX, lastY)
        )
    }
}
