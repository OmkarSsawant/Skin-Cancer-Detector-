package com.visionDev.skin_cancer_detection

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.SensorManager
import android.util.Log
import android.view.OrientationEventListener
import androidx.camera.core.ImageProxy
import com.visionDev.skin_cancer_detection.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class SkinCancerDetector(context:Context) : OrientationEventListener(context,SensorManager.SENSOR_DELAY_NORMAL) {
    private val skinModel by lazy {
        Model.newInstance(context)
    }
    private var orientation:Int = 0
    fun detect(imgP:ImageProxy){
        val modelInput:TensorBuffer = prepareModelInput(imgP) ?: return
        val outputs = skinModel.process(modelInput)
        Log.i(TAG, "detected : ${outputs.outputFeature0AsTensorBuffer}")
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun prepareModelInput(imgP: ImageProxy): TensorBuffer? {
        val mBITMAP = imgP.image?.toBitmap() ?: return null
        val resized = Bitmap.createScaledBitmap(mBITMAP,224,224,true)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resized)
        return tensorImage.tensorBuffer
    }

    fun dispose(){
        skinModel.close()
    }

    companion object{
        private const val TAG = "SkinCancerDetector"
    }

    override fun onOrientationChanged(orientation: Int) {
        this.orientation  = orientation
    }
}