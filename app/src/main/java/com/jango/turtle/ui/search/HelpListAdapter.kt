package com.jango.turtle.ui.search

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jango.turtle.R

/**
 * Created by Ashwani on 16/06/18.
 */

class HelpListAdapter(private val context: Context,val helpOptions: ArrayList<HelpOption>,
                      val optionClickCallback: ((HelpOption) -> Unit)?) : RecyclerView.Adapter<HelpListAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_help_list, parent, false)
        return ViewHolder(context,v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: HelpListAdapter.ViewHolder, position: Int) {
        holder.bindItems(helpOptions[position],optionClickCallback)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return helpOptions.size
    }

    //the class is hodling the list view
    class ViewHolder(val context: Context,itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(option: HelpOption,optionClickCallback: ((HelpOption) -> Unit)?) {
            val icon = itemView.findViewById(R.id.icon) as ImageView
            val name  = itemView.findViewById(R.id.name) as TextView
            icon.setImageResource(option.resId)
            name.text = option.name
            val rowContainer = itemView.findViewById(R.id.rowContainer) as ConstraintLayout
            rowContainer.setOnClickListener {
                Log.d("ashwani","clicked")
                optionClickCallback?.invoke(option)
            }
        }
    }
}