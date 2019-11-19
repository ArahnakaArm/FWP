package com.example.deimos.fwp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList


class LocationChildAdapter(ctx: Context, private val ModelArrayList: ArrayList<LocationChildModel>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<LocationChildAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<LocationChildModel>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<LocationChildModel>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationChildAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.expandable_recyclerview_product, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationChildAdapter.MyViewHolder, position: Int) {

        holder.title.setText(ModelArrayList[position].getNames())
        holder.itemView.setOnClickListener {
          //  Toast.makeText(holder.itemView.context,ModelArrayList[position].getNames(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView

        init {

            title = itemView.findViewById(R.id.textView) as TextView

        }


    }
}