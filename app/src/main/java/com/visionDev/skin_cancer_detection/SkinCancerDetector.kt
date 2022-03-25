package com.visionDev.skin_cancer_detection

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import com.visionDev.skin_cancer_detection.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class SkinCancerDetector(context:Context) {
    private val skinModel by lazy {
        Model.newInstance(context)
    }


    fun detect(imgP:ImageProxy){
        val buffer = imgP.planes.first().buffer
        val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature.loadBuffer(buffer)

        val outputs = skinModel.process(inputFeature)
        Log.i(TAG, "detected : $outputs")
        imgP.close()
    }


    fun dispose(){
        skinModel.close()
    }

    companion object{
        private const val TAG = "SkinCancerDetector"
    }
}