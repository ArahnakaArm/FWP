package com.example.deimos.fwp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.itembookmark.view.*

import java.util.ArrayList

class BookMarkAdapter(ctx: Context, private val imageModelArrayList: ArrayList<BookMarkModel>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<BookMarkAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<BookMarkModel>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<BookMarkModel>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.itembookmark, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookMarkAdapter.MyViewHolder, position: Int) {

        holder.title.setText("  "+imageModelArrayList[position].getNames())
        holder.date.setText(imageModelArrayList[position].getDate())
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,imageModelArrayList[position].getNames(),Toast.LENGTH_SHORT).show()
        }
        Glide.with(holder.itemView.context)
                .load(imageModelArrayList[position].getImageUrl())
                .into(holder.imageUrl)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var imageUrl : ImageView

        init {

            title = itemView.findViewById(R.id.title) as TextView
            date = itemView.findViewById(R.id.datebookmark) as TextView
            imageUrl = itemView.findViewById(R.id.imageView1) as ImageView

        }


    }
}