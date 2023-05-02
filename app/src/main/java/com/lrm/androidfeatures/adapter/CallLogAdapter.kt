package com.lrm.androidfeatures.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.model.CallLogModel

class CallLogAdapter(
    val callLogList: List<CallLogModel>
): RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {


    class CallLogViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.name_cl)
        val number = view.findViewById<TextView>(R.id.number_cl)
        val callType = view.findViewById<TextView>(R.id.call_type)
        val duration = view.findViewById<TextView>(R.id.duration)
        val date = view.findViewById<TextView>(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.call_log_list_item, parent, false)

        return CallLogViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return callLogList.size
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val item = callLogList[position]
        holder.name.text = item.name
        holder.number.text = item.number
        holder.callType.text = item.callType
        holder.duration.text = item.duration
        holder.date.text = item.date
    }

}