package com.example.deimos.fwp

import android.content.Intent
import android.graphics.Movie
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

class ListAdapterVideo(private val list: List<Video>)
    : RecyclerView.Adapter<VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VideoViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video: Video = list[position]
        holder.bind(video)
        holder.itemView.setOnClickListener {
            val titleExtra: String = video.name
            val dateExtra : String = video.username
            val intent=Intent(holder.itemView.context,VideoContent::class.java)
            //Toast.makeText(holder.itemView.context,video.title,Toast.LENGTH_SHORT).show()
            intent.putExtra("Title",titleExtra)
            intent.putExtra("Date",dateExtra)
            holder.itemView.context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size

}