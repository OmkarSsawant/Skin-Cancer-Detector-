 package com.visionDev.skin_cancer_detection

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

 class MainActivity : AppCompatActivity() {

    private lateinit var camera : Camera
    private lateinit var skinCancerDetector: SkinCancerDetector
    private lateinit var executors: ExecutorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PermissionChecker.PERMISSION_GRANTED)
            initCameraX()
        else
        {
            Toast.makeText(this,"Please Grant Camera Permission to proceed",Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA),1)
            }
        }
    }

     private fun initCameraX() {
        val camP =  ProcessCameraProvider.getInstance(this)
         camP.addListener( {
             val cameraProvider = camP.get()

             val cameraSelector  = CameraSelector.Builder()
                 .requireLensFacing(LENS_FACING_BACK)
                 .build()

             val preview = Preview.Builder()
                 .build()
             val pv:PreviewView = findViewById(R.id.camera_view)
             preview.setSurfaceProvider(pv.surfaceProvider)

             val analyzer = ImageAnalysis.Builder()
                 .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                 .build()
             analyzer.setAnalyzer(executors,SkinCancerAnalyzer())

             camera = cameraProvider.bindToLifecycle(this,cameraSelector,preview,analyzer)
             skinCancerDetector = SkinCancerDetector(this)
         },ContextCompat.getMainExecutor(this))
     }




     inner class SkinCancerAnalyzer : ImageAnalysis.Analyzer{
         override fun analyze(image: ImageProxy) {
            skinCancerDetector.detect(image)
             image.close()
         }
     }


     override fun onDestroy() {
         if(::executors.isInitialized)
                executors.shutdownNow()
         skinCancerDetector.dispose()
         super.onDestroy()
     }

 }