package com.example.yoga

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.atan2

typealias LumaListener = (luma: Double) -> Unit

class CameraActivity : AppCompatActivity() {

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var poseDetector: PoseDetector
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

//        camera_capture_button.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Base pose detector with streaming frames, when depending on the pose-detection sdk
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()

        poseDetector = PoseDetection.getClient(options)

        switch_camera_button.setOnClickListener {
            toggleFrontBackCamera()
        }
    }

//    private fun takePhoto() {}

    private fun toggleFrontBackCamera() {

        if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        } else if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer {

                    })
                }


            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    fun poseDetectorProcess(image: InputImage) {
        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                // Task completed successfully
                // ...
                poseIdentification(pose)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Log.e(TAG, "Exception", e)
            }
    }

    private fun getAngle(
        firstPoint: PoseLandmark,
        midPoint: PoseLandmark,
        lastPoint: PoseLandmark
    ): Double {
        var result = Math.toDegrees(
            (atan2(
                lastPoint.position.y - midPoint.position.y,
                lastPoint.position.x - midPoint.position.x
            )
                    - atan2(
                firstPoint.position.y - midPoint.position.y,
                firstPoint.position.x - midPoint.position.x
            )).toDouble()
        )
        result = abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

    @SuppressLint("SetTextI18n")
    private fun poseIdentification(pose: Pose) {
        val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
        if (nose != null){
            draw.drawPoseLandmarkPoint(nose)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"

        //        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    inner class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(
                        mediaImage,
                        imageProxy.imageInfo.rotationDegrees
                    )
                poseDetectorProcess(image)

            }

            val buffer = imageProxy.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            imageProxy.close()
        }
    }
}

class Drawable(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private var poseLandmark : PoseLandmark? = null
    private val whitePaint = Paint().also {
        it.color = Color.WHITE
        it.strokeWidth = 16.0f
    }
    override fun onDraw(canvas: Canvas?) {
        poseLandmark?.position3D?.let {
            canvas?.drawPoint(it.x, it.y, whitePaint)
        }
    }

    fun drawPoseLandmarkPoint(pl: PoseLandmark){
        this.poseLandmark = pl
        invalidate()
    }
}