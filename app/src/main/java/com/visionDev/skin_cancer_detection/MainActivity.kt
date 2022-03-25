 package com.visionDev.skin_cancer_detection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.Camera
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture

 class MainActivity : AppCompatActivity() {

    lateinit var camera : Camera


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCameraX()
    }

     private fun initCameraX() {
        val camP =  ProcessCameraProvider.getInstance(this)
         camP.addListener( {
             val cameraProvider = camP.get()

             val cameraSelector  = CameraSelector.Builder()
                 .requireLensFacing(LENS_FACING_FRONT)
                 .build()

             val preview = Preview.Builder()
                 .build()

             val pv:PreviewView = findViewById(R.id.camera_view)
             preview.setSurfaceProvider(pv.surfaceProvider)

             camera = cameraProvider.bindToLifecycle(this,cameraSelector,preview)
         },ContextCompat.getMainExecutor(this))
     }




 }