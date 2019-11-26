package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import java.util.ArrayList


class VideoAdapter(ctx: Context, private val ModelArrayList: ArrayList<resultData3>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<resultData3>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<resultData3>()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_video, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoAdapter.MyViewHolder, position: Int) {
        var dateFormate = ModelArrayList[position].updatedAt.substring(range = 0..10)
        var dateOutput =Profilewithpicture.ConvertDate.ChangeFormatDate(dateFormate.substring(0..3),dateFormate.substring(5..6),dateFormate.substring(8..9))
        holder.date.setText(dateOutput)
        holder.title.setText(ModelArrayList[position].videoName.th)
        holder.category.setText(ModelArrayList[position].categoryId.categoryName.th)


        val videoLink : String= ModelArrayList[position].videoLink
        val idExtra : String = ModelArrayList[position]._id
        var VideoId = videoLink.substringAfterLast("=")
        val strURL = "http://img.youtube.com/vi/" + VideoId + "/0.jpg"

        Glide.with(holder.itemView.context)
                .load(ModelArrayList[position].image.path)
                .into(holder.image)

        holder.itemView.setOnClickListener {


            val intent=Intent(holder.itemView.context,VideoContent::class.java)

            //Toast.makeText(holder.itemView.context,video.title,Toast.LENGTH_SHORT).show()

            intent.putExtra("ID",idExtra)
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
        var category : TextView
        init {

            title = itemView.findViewById(R.id.videotopic) as TextView
            date = itemView.findViewById(R.id.date) as TextView
            image=itemView.findViewById(R.id.videoholder) as ImageView
            category=itemView.findViewById(R.id.category) as TextView

    }
    }
}