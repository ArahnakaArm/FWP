package com.example.deimos.fwp

import android.graphics.Movie
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class ListAdapter(private val list: List<Bookmark>)
    : RecyclerView.Adapter<BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BookmarkViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark: Bookmark = list[position]
        holder.bind(bookmark)
    }

    override fun getItemCount(): Int = list.size

}