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
import kotlinx.android.synthetic.main.itembookmark.view.*
import org.w3c.dom.Text

import java.util.ArrayList

/**
 * Created by Parsania Hardik on 26-Jun-17.
 */
class ArticleAdapter(ctx: Context, private val ModelArrayList: ArrayList<ArticleData>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<resultData3>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<resultData3>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_article, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.MyViewHolder, position: Int) {

        holder.cate.setText(ModelArrayList[position].categoryId.categoryName.th)
        holder.date.setText(ModelArrayList[position].updatedAt.substring(range = 0..9))
        holder.title.setText(ModelArrayList[position].articleName.th)
       // holder.category.setText(ModelArrayList[position].categoryId.categoryName.th)
/*
        val titleExtra: String = ModelArrayList[position].videoName.th
        val dateExtra : String = ModelArrayList[position].updatedAt.substring(range = 0..9)

        */




        Glide.with(holder.itemView.context)
                .load(holder.URLImage+ModelArrayList[position].imageThumbnail.path)
                .into(holder.image)

        holder.itemView.setOnClickListener {


            val intent=Intent(holder.itemView.context,ArticleInfo::class.java)
            intent.putExtra("ID",ModelArrayList[position]._id)
            holder.itemView.context?.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var image : ImageView
        var cate : TextView
         var URLImage : String ="http://206.189.41.105:1210/"
        init {
            cate = itemView.findViewById(R.id.category) as TextView
            title = itemView.findViewById(R.id.articleTopic) as TextView
            date = itemView.findViewById(R.id.articleDate) as TextView
            image=itemView.findViewById(R.id.articleImage) as ImageView

        }
    }
}