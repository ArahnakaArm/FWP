package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_bookmark.view.*
import org.w3c.dom.Text
import java.lang.Exception

import java.util.ArrayList


class ArticleInfoAdapter(ctx: Context, private val ModelArrayList: ArrayList<Content>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ArticleInfoAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<Content>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<Content>()

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleInfoAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_articleinfo, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleInfoAdapter.MyViewHolder, position: Int) {

                  if (ModelArrayList[position].messageType == "Image") {
               holder.des.visibility = View.GONE
               holder.image.visibility = View.VISIBLE
                Glide.with(holder.itemView.context)
                        .load(holder.URLImage + ModelArrayList[position].image.path)
                        .into(holder.image)

                d("TestInfo", ModelArrayList[position].image.path)

                } else if(ModelArrayList[position].messageType == "Text"){
               holder.des.visibility = View.VISIBLE
               holder.image.visibility = View.GONE
                holder.des.text = ModelArrayList[position].articleDescription.th
                d("TestInfo", ModelArrayList[position].articleDescription.th)
                }
        holder.image.setOnClickListener {
            d("ClickTest","Image"+position.toString())
        }
        holder.des.setOnClickListener {
            d("ClickTest","Text"+position.toString())
        }


    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var des : TextView
        var image : ImageView
        var URLImage : String ="http://206.189.41.105:1210/"
        init {

            des = itemView.findViewById(R.id.infoTextView) as TextView
            image=itemView.findViewById(R.id.infoImageView) as ImageView

        }
    }
}