package com.visionDev.skin_cancer_detection

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class SkinReportAdapter : RecyclerView.Adapter<SkinReportAdapter.ResultVH>(),SkinCancerOutputListener {

    private var mReports : Map<String,Float>  = mutableMapOf()
    private val mainHandler : Handler = Handler(Looper.getMainLooper())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultVH {
      val item:View =   LayoutInflater.from(parent.context).inflate(R.layout.result_tile,parent,false)
        return  ResultVH(item)
    }

    override fun onBindViewHolder(holder: ResultVH, position: Int) {
       val report =  mReports.entries.elementAt(position)
        holder.setResult(report.key,report.value)
    }

    override fun getItemCount(): Int  = mReports.size

    override fun onReport(reports: Map<String, Float>) {

        mainHandler.post{
            notifyItemRangeRemoved(0,mReports.size)
            mReports = reports
                .toList()
                .filter { it.second > 0.2 }
                .sortedBy { it.second }
                .toMap()
            notifyItemRangeInserted(0,mReports.size)
        }
    }


    inner class ResultVH(iv: View):RecyclerView.ViewHolder(iv){
        private val diseaseName: TextView = iv.findViewById(R.id.skin_disease_name)
        private val confidence:ProgressBar = iv.findViewById(R.id.skin_disease_probability)

        fun setResult(disease:String,prob:Float){
            diseaseName.text = disease
            confidence.progress = (100 * prob).roundToInt()
        }
    }


}