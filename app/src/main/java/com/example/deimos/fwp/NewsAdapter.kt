package com.example.deimos.fwp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.itembookmark.view.*

import java.util.ArrayList

class NewsAdapter(ctx: Context, private val imageModelArrayList: ArrayList<ArticleData>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {
    private var URLImage : String ="http://206.189.41.105:1210/"
    private val inflater: LayoutInflater
    private val arraylist: ArrayList<ArticleData>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<ArticleData>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.searchnews_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.MyViewHolder, position: Int) {

        holder.title.setText("  "+imageModelArrayList[position].articleName.th)
        holder.date.setText(imageModelArrayList[position].updatedAt)
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,imageModelArrayList[position].articleName.th,Toast.LENGTH_SHORT).show()
        }
        Glide.with(holder.itemView.context)
                .load(URLImage+imageModelArrayList[position].imageThumbnail.path)
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var image : ImageView
        init {

            title = itemView.findViewById(R.id.titlesearchnews) as TextView
            date = itemView.findViewById(R.id.datesearchnews) as TextView
            image = itemView.findViewById(R.id.imagesearchnews) as ImageView
        }


    }
}