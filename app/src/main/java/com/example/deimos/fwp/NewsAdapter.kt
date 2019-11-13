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

class NewsAdapter(ctx: Context, private val imageModelArrayList: ArrayList<NewsModel>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<NewsModel>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<NewsModel>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.searchnews_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.MyViewHolder, position: Int) {

        holder.title.setText("  "+imageModelArrayList[position].getNames())
        holder.date.setText(imageModelArrayList[position].getDate())
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,imageModelArrayList[position].getNames(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView

        init {

            title = itemView.findViewById(R.id.titlesearchnews) as TextView
            date = itemView.findViewById(R.id.datesearchnews) as TextView

        }


    }
}