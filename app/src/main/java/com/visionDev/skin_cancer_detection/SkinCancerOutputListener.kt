package com.visionDev.skin_cancer_detection

interface SkinCancerOutputListener {
    fun onReport(reports:Map<String,Float>)
}