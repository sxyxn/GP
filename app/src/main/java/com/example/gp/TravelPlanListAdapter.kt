package com.example.gp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gp.data.TravelPlan

class TravelPlanListAdapter(
    context: Context,
    private val travelPlans: List<TravelPlan>
) : ArrayAdapter<TravelPlan>(context, 0, travelPlans) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.travel_plan_item, parent, false)

        val plan = travelPlans[position]

        val timeTextView: TextView = view.findViewById(R.id.planTime)
        val destinationTextView: TextView = view.findViewById(R.id.planDestination)
        val activityTextView: TextView = view.findViewById(R.id.planActivity)

        timeTextView.text = "시간: ${plan.time}"
        destinationTextView.text = "여행지: ${plan.destination}"
        activityTextView.text = "활동: ${plan.activity}"

        return view
    }
}