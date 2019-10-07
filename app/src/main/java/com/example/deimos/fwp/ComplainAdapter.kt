package com.example.deimos.fwp

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.itembookmark.view.*

import java.util.ArrayList

/**
 * Created by Parsania Hardik on 26-Jun-17.
 */
class ComplainAdapter(ctx: Context, private val imageModelArrayList: ArrayList<ComplainModel>) :
        RecyclerView.Adapter<ComplainAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<ComplainModel>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<ComplainModel>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.itemcomplain, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplainAdapter.MyViewHolder, position: Int) {





        holder.date.setText(imageModelArrayList[position].getDate())
        holder.status.setText(imageModelArrayList[position].getStatus())
        holder.title.setText(imageModelArrayList[position].getNames())
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,imageModelArrayList[position].getNames(),Toast.LENGTH_SHORT).show()
        }

        if (imageModelArrayList[position].getStatus()=="In Progress"){
            val colorval = ContextCompat.getColor(holder.itemView.context, R.color.Primary2)
            holder.status.setTextColor(colorval)

        }
        if(imageModelArrayList[position].getStatus()=="Complete"){
            val colorValue = ContextCompat.getColor(holder.itemView.context, R.color.white2)
            holder.itemView.setBackgroundColor(colorValue)
        }

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var status : TextView

        init {

            title = itemView.findViewById(R.id.titlecomplain) as TextView
            date = itemView.findViewById(R.id.datecomplain) as TextView
            status = itemView.findViewById(R.id.statuscomplain) as TextView

        }


    }
}