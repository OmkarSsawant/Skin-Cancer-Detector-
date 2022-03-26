package com.visionDev.skin_cancer_detection

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.SensorManager
import android.view.OrientationEventListener
import androidx.camera.core.ImageProxy
import com.visionDev.skin_cancer_detection.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class SkinCancerDetector(context:Context) : OrientationEventListener(context,SensorManager.SENSOR_DELAY_NORMAL) {
    private val skinModel by lazy {
        Model.newInstance(context)
    }
    private var orientation:Int = 0

    fun detect(imgP: ImageProxy): Map<String, Float>? {
        val modelInput: TensorBuffer = prepareModelInput(imgP) ?: return null
        val outputs = skinModel.process(modelInput)
        return mapToRecognitions(outputs)
    }

    private fun mapToRecognitions(outputs: Model.Outputs): Map<String,Float> {
        val mOutputs = mutableMapOf<String,Float>()
        outputs.outputFeature0AsTensorBuffer.floatArray.forEachIndexed{i,confidence ->
            mOutputs[SkinCancerModelLabeler.getSkinClass(i)] = confidence
        }
        return mOutputs
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