package com.example.deimos.fwp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
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
class NotificationAdapter(ctx: Context, private val ModelArrayList: ArrayList<NotificationModel>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<NotificationModel>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<NotificationModel>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_noti, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {
        holder.info.setText(ModelArrayList[position].getInfo())
        holder.title.setText(ModelArrayList[position].getNames())
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,ModelArrayList[position].getNames(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var info : TextView

        init {

            title = itemView.findViewById(R.id.title) as TextView
            info = itemView.findViewById(R.id.infotitle) as TextView

        }


    }
}