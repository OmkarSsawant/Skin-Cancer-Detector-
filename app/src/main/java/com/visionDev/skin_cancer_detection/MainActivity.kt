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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

 class MainActivity : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
         setContentView(R.layout.activity_main)
         super.onCreate(savedInstanceState)

         supportFragmentManager.beginTransaction()
             .add(R.id.host,HomeFragment(),"HOME")
             .addToBackStack(null)
             .commit()

     }

 }