package com.example.deimos.fwp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList


class LocationChildAdapter(ctx: Context, private val imageModelArrayList: ArrayList<LocationChildModel>) :
        RecyclerView.Adapter<LocationChildAdapter.MyViewHolder>() {

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

        holder.title.setText(imageModelArrayList[position].getNames())
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,imageModelArrayList[position].getNames(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView

        init {

            title = itemView.findViewById(R.id.textView) as TextView

        }


    }
}