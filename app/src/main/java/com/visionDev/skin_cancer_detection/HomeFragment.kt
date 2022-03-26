package com.visionDev.skin_cancer_detection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    val liveDetectionFragment : LiveDetectionFragment by lazy {
        LiveDetectionFragment()
    }
    val staticDetectionFragment : StaticDetectionFragment by lazy {
        StaticDetectionFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val liveDetection:Button = view.findViewById(R.id.live_detection_btn)
        val staticDetection:Button = view.findViewById(R.id.static_detection_btn)

        liveDetection.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.host,liveDetectionFragment,"LIVE_DETECTION")
                .commit()
        }

        staticDetection.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.host,staticDetectionFragment,"STATIC_DETECTION")
                .commit()
        }
    }

}