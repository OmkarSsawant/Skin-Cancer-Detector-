package com.visionDev.skin_cancer_detection

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StaticDetectionFragment(
    private val detector :SkinCancerDetector
) : Fragment() {
    private val skinReportAdapter:  SkinReportAdapter = SkinReportAdapter()
    private lateinit var launcher :ActivityResultLauncher<String>
    private var srcImage:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         launcher   = registerForActivityResult(ActivityResultContracts.GetContent()) {
             srcImage?.setImageURI(it)
             requireContext().contentResolver.openFileDescriptor(it, "r")
                 ?.let {pdf->
                     val bitmap = BitmapFactory.decodeFileDescriptor(pdf.fileDescriptor)
                     val reports = detector.detect(bitmap)
                     if (reports != null) {
                         skinReportAdapter.onReport(reports)
                     }
                 }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_static_detection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pickImage: Button = view.findViewById(R.id.pick_image)
        srcImage = view.findViewById(R.id.input_image)
        val results : RecyclerView = view.findViewById(R.id.result_list)
        results.layoutManager = LinearLayoutManager(requireContext())
        results.adapter = skinReportAdapter
        pickImage.setOnClickListener {

            launcher.launch("image/*")
        }
    }

}