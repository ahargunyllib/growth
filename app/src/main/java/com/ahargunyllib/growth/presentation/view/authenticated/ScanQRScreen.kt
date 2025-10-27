package com.ahargunyllib.growth.presentation.view.authenticated

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlin.random.Random

private data class ScanResult(val points: Int, val weight: Double)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQRScreen(
    authenticatedNavController: NavController
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
        }
    )

    LaunchedEffect(key1 = cameraPermissionState.status) {
        if (!cameraPermissionState.status.isGranted) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var scanResult by remember { mutableStateOf<ScanResult?>(null) }
    var isScanning by remember { mutableStateOf(true) }

    val onQRCodeScanned: (String) -> Unit = { qrValue ->
        if (isScanning) {
            isScanning = false
            Log.i("ScanQRScreen", "QR Code Ditemukan: $qrValue")

            scanResult = ScanResult(
                points = Random.nextInt(50, 200),
                weight = Random.nextDouble(0.5, 5.0)
            )
            showDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                onQRCodeScanned = onQRCodeScanned,
                isScanningActive = isScanning
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GrowthScheme.Third.color),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Izin kamera dibutuhkan untuk memindai QR Code.",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp),
                        style = GrowthTypography.BodyL.textStyle
                    )
                    Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                        Text("Minta Izin")
                    }
                }
            }
        }

        ScannerOverlay(
            onBack = { authenticatedNavController.popBackStack() }
        )

        AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            scanResult?.let { result ->
                ScanResultDialog(
                    result = result,
                    onDismiss = {
                        showDialog = false
                        isScanning = true
                    },
                    onConfirm = {
                        showDialog = false
                        // Navigasi ke SuccessDepositScreen
                        authenticatedNavController.navigate(AuthenticatedNavObj.SuccessDeposit.route) {
                            popUpTo(AuthenticatedNavObj.ScanQR.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CameraPreview(
    onQRCodeScanned: (String) -> Unit,
    isScanningActive: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = CameraXPreview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                        setAnalyzer(
                            executor,
                            QRCodeAnalyzer(
                                onQRCodeScanned = onQRCodeScanned,
                                isScanningActiveProvider = { isScanningActive }
                            )
                        )
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
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

private class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit,
    private val isScanningActiveProvider: () -> Boolean
) : ImageAnalysis.Analyzer {
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanningActiveProvider()) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.firstOrNull()?.rawValue?.let { qrValue ->
                        onQRCodeScanned(qrValue)
                    }
                }
                .addOnFailureListener {
                    Log.e("QRCodeAnalyzer", "Gagal memproses gambar", it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
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

        // Sudut kiri atas
        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.left, rect.top + cornerLength)
                lineTo(rect.left, rect.top)
                lineTo(rect.left + cornerLength, rect.top)
            },
            style = Stroke(width = strokeWidth)
        )

        // Sudut kanan atas
        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.right - cornerLength, rect.top)
                lineTo(rect.right, rect.top)
                lineTo(rect.right, rect.top + cornerLength)
            },
            style = Stroke(width = strokeWidth)
        )

        // Sudut kiri bawah
        drawPath(
            color = cornerColor,
            path = Path().apply {
                moveTo(rect.left, rect.bottom - cornerLength)
                lineTo(rect.left, rect.bottom)
                lineTo(rect.left + cornerLength, rect.bottom)
            },
            style = Stroke(width = strokeWidth)
        )

        // Sudut kanan bawah
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

@Composable
private fun ScanResultDialog(
    result: ScanResult,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Setoran Terdeteksi",
                    style = GrowthTypography.HeadingL.textStyle,
                    color = GrowthScheme.Black.color
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Poin Didapat", style = GrowthTypography.BodyM.textStyle, color = GrowthScheme.Disabled.color)
                        Text(
                            text = result.points.toString(),
                            style = GrowthTypography.HeadingL.textStyle,
                            color = GrowthScheme.Primary.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Berat", style = GrowthTypography.BodyM.textStyle, color = GrowthScheme.Disabled.color)
                        Text(
                            text = "${String.format("%.1f", result.weight)} kg",
                            style = GrowthTypography.HeadingL.textStyle,
                            color = GrowthScheme.Primary.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = "Apakah Anda yakin ingin melanjutkan setoran ini?",
                    style = GrowthTypography.BodyM.textStyle,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp, brush = SolidColor(GrowthScheme.Primary.color))
                    ) {
                        Text("Tidak", color = GrowthScheme.Primary.color)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = GrowthScheme.Primary.color)
                    ) {
                        Text("Ya", color = Color.White)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScanQRScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Third.color)
    ) {
        ScannerOverlay(onBack = {})
        ScanResultDialog(
            result = ScanResult(points = 120, weight = 2.5),
            onDismiss = {},
            onConfirm = {}
        )
    }
}
