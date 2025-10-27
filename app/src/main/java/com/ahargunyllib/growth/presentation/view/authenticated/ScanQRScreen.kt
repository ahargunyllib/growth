package com.ahargunyllib.growth.presentation.view.authenticated

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQRScreen(
    authenticatedNavController: NavController
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(key1 = true) {
        delay(3000)
        authenticatedNavController.navigate(AuthenticatedNavObj.SuccessDeposit.route) {
            popUpTo(AuthenticatedNavObj.ScanQR.route) {
                inclusive = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (cameraPermissionState.status) {
            PermissionStatus.Granted -> {
                CameraPreview()
            }
            is PermissionStatus.Denied -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GrowthScheme.Third.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Izin kamera dibutuhkan untuk menggunakan fitur pemindaian.",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp),
                        style = GrowthTypography.BodyL.textStyle
                    )
                }
            }
        }

        ScannerOverlay(
            onBack = { authenticatedNavController.popBackStack() }
        )
    }
}

@Composable
private fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = CameraXPreview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("ScanQRScreen", "Gagal bind lifecycle kamera", e)
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ScannerOverlay(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScannerFrame(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        )

        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = "Kembali",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 24.dp)
                .size(32.dp)
                .clickable(onClick = onBack)
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            color = GrowthScheme.Black.color.copy(alpha = 0.8f),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Arahkan kamera ke QR Code",
                style = GrowthTypography.BodyL.textStyle,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}


@Composable
private fun ScannerFrame(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val boxSize = size.width * 0.65f
        val cornerLength = boxSize * 0.15f
        val strokeWidth = 8f

        val rectTopLeft = Offset(
            x = (canvasWidth - boxSize) / 2,
            y = (canvasHeight - boxSize) / 2
        )
        val rect = androidx.compose.ui.geometry.Rect(rectTopLeft, Size(boxSize, boxSize))

        val cornerColor = GrowthScheme.Primary.color

        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.left, rect.top + cornerLength)
                lineTo(rect.left, rect.top)
                lineTo(rect.left + cornerLength, rect.top)
            },
            style = Stroke(width = strokeWidth)
        )

        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.right - cornerLength, rect.top)
                lineTo(rect.right, rect.top)
                lineTo(rect.right, rect.top + cornerLength)
            },
            style = Stroke(width = strokeWidth)
        )

        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.left, rect.bottom - cornerLength)
                lineTo(rect.left, rect.bottom)
                lineTo(rect.left + cornerLength, rect.bottom)
            },
            style = Stroke(width = strokeWidth)
        )

        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.right - cornerLength, rect.bottom)
                lineTo(rect.right, rect.bottom)
                lineTo(rect.right, rect.bottom - cornerLength)
            },
            style = Stroke(width = strokeWidth)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScanQRScreenPreview() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(GrowthScheme.Third.color)) {
        ScannerOverlay(onBack = {})
    }
}
