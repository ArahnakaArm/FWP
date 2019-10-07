package com.example.deimos.fwp

import android.content.Context
import android.graphics.ColorSpace.Model
import android.support.v7.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

import com.example.deimos.fwp.User

import java.util.ArrayList


class CustomAdapter(private val context: Context, private val BookMarkList: ArrayList<Bookmark>) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>(), Filterable {
    private var filteredUserList: ArrayList<Bookmark>? = null

    init {
        this.filteredUserList = BookMarkList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomAdapter.MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.itembookmark, viewGroup, false)
        val myViewHolder = MyViewHolder(view)

        return myViewHolder
    }

    override fun onBindViewHolder(viewHolder: CustomAdapter.MyViewHolder, position: Int) {

        viewHolder.title.setText(filteredUserList!![position].title)


    }

    override fun getItemCount(): Int {
        return filteredUserList!!.size
    }

    override fun getFilter(): Filter {
        d("armtesttext","Work!!")
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                val searchString = charSequence.toString()

                if (searchString.isEmpty()) {

                    filteredUserList = BookMarkList

                } else {

                    val tempFilteredList = ArrayList<Bookmark>()

                    for (bookmark in BookMarkList) {

                        // search for user title
                        if (bookmark.title?.toLowerCase()!!.contains(searchString)) {

                            tempFilteredList.add(bookmark)
                        }
                    }

                    filteredUserList = tempFilteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filteredUserList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filteredUserList = filterResults.values as ArrayList<Bookmark>
                notifyDataSetChanged()
            }
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        public val title: TextView



        init {
            title = view.findViewById(R.id.title) as TextView



        }
    }
}
