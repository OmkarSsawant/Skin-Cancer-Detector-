package com.visionDev.skin_cancer_detection

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class LiveDetectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_detection, container, false)
    }
    private lateinit var camera : Camera
    private lateinit var skinCancerDetector: SkinCancerDetector
    private lateinit var executors: ExecutorService
    private val skinReportAdapter:  SkinReportAdapter = SkinReportAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CAMERA)== PermissionChecker.PERMISSION_GRANTED)
            initCameraX(view)
        else
        {
            Toast.makeText(requireContext(),"Please Grant Camera Permission to proceed", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA),1)
            }
        }
    }

    private fun initCameraX(view: View) {
        val camP =  ProcessCameraProvider.getInstance(requireContext())
        camP.addListener( {
            val cameraProvider = camP.get()

            val cameraSelector  = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val preview = Preview.Builder()
                .build()
            val pv: PreviewView = view.findViewById(R.id.camera_view)
            preview.setSurfaceProvider(pv.surfaceProvider)

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)

                .build()
            analyzer.setAnalyzer(executors,SkinCancerAnalyzer())

            camera = cameraProvider.bindToLifecycle(this,cameraSelector,preview,analyzer)
            skinCancerDetector = SkinCancerDetector(requireContext())
            val results : RecyclerView = view.findViewById(R.id.result_list)
            results.layoutManager = LinearLayoutManager(requireContext())
            results.adapter = skinReportAdapter
        }, ContextCompat.getMainExecutor(requireContext()))
    }




    inner class SkinCancerAnalyzer : ImageAnalysis.Analyzer{
        override fun analyze(image: ImageProxy) {
            val reports =  skinCancerDetector.detect(image)
            if (reports != null) {
                skinReportAdapter.onReport(reports)
            }
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