package com.example.deimos.fwp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.loading.view.*
import java.lang.Exception

import java.util.ArrayList


class VideoAdapter(recyclerView: RecyclerView,ctx: Context, private val ModelArrayList: ArrayList<resultData3?>,var activity: Activity) :
        androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val VIEW_LOADINGTYPE = 1
    val VIEW_ITEMTYPE = 0
    internal var  loadMore : ILoadMore ? =null
    internal var isLoading: Boolean=false
    internal var  visibleThreshold = 3
    internal var  pastVisibleItems  : Int =0
    internal var totalItemCount : Int = 0
    internal var visibleItemCount  : Int = 0


    init {
        val grid = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = grid.itemCount
                visibleItemCount = grid.getChildCount()
                pastVisibleItems  = grid.findFirstVisibleItemPosition()
                if(totalItemCount < 10){
                    totalItemCount = 10
                }
               // Log.d("Detect", lastVisibleItem.toString())
                if(!isLoading && totalItemCount <= pastVisibleItems+ visibleItemCount){

                    if(loadMore != null){
                      //  Log.d("Detect", totalItemCount.toString())
                        loadMore!!.onLoadMore()
                        isLoading = true
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })



    }

    override fun getItemViewType(position: Int): Int {
        return if(ModelArrayList[position] == null )VIEW_LOADINGTYPE else VIEW_ITEMTYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_ITEMTYPE){
            val view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_video,parent,false)
            return MyViewHolder(view)
        }else if (viewType == VIEW_LOADINGTYPE){
            val view = LayoutInflater.from(activity)
                    .inflate(R.layout.loading,parent,false)
            return LoadingViewHolder(view)
        }
        return null!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyViewHolder) {
        try {


            var dateFormate = ModelArrayList[position]!!.updatedAt.substring(range = 0..10)
            var dateOutput =Profilewithpicture.ConvertDate.ChangeFormatDate(dateFormate.substring(0..3),dateFormate.substring(5..6),dateFormate.substring(8..9))
            holder.date.setText(dateOutput)
            holder.title.setText(ModelArrayList[position]!!.videoName.th)

           holder.category.setText(ModelArrayList[position]!!.categoryInfo[0].categoryId.categoryName.th)
            val videoLink : String= ModelArrayList[position]!!.videoLink
            val idExtra : String = ModelArrayList[position]!!._id
            var VideoId = videoLink.substringAfterLast("=")
            val strURL = "http://img.youtube.com/vi/" + VideoId + "/0.jpg"

            Glide.with(holder.itemView.context)
                    .load(ModelArrayList[position]!!.image)
                    .into(holder.image)

            holder.itemView.setOnClickListener {


                val intent=Intent(holder.itemView.context,VideoContent::class.java)

                //Toast.makeText(holder.itemView.context,video.title,Toast.LENGTH_SHORT).show()

                intent.putExtra("ID",idExtra)
                holder.itemView.context?.startActivity(intent)

            }
        }catch (e : Exception){
            d("Exception",e.toString())
        }
        }
        else if(holder is ArticleAdapter.LoadingViewHolder){
            holder.progressBar.isIndeterminate = true
        }

    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    internal  class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

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
    internal class LoadingViewHolder(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        var progressBar = itemView.progress_bar
    }
    fun setLoaded(){
        isLoading = false
    }
    fun setLoadMore(iLoadMore: ILoadMore){
        this.loadMore = iLoadMore
    }
}