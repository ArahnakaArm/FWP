package com.example.deimos.fwp

import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_bookmark.view.*
import kotlinx.android.synthetic.main.loading.view.*

import java.util.ArrayList

class NewsAdapter(recyclerView: RecyclerView,ctx: Context, private val ModelArrayList: ArrayList<ArticleData?>,var activity: Activity) :
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
                // Log.d("Detect", lastVisibleItem.toString())
                if(totalItemCount < 10){
                    totalItemCount = 10
                }
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is MyViewHolder) {
            try {
               // holder.cate.setText(ModelArrayList[position]!!.categoryId.categoryName.th)

                holder.title.setText(ModelArrayList[position]!!.articleName.th)
                holder.date.setText(ModelArrayList[position]!!.updatedAt.substring(0..9))
                holder.itemView.setOnClickListener {
                    //notifyItemRemoved(position)
                    val intent = Intent(holder.itemView.context, ArticleInfo::class.java)
                    intent.putExtra("ID", ModelArrayList[position]!!._id)
                    holder.itemView.context?.startActivity(intent)
                }
                Glide.with(holder.itemView.context)
                        .load(ModelArrayList[position]!!.imageThumbnail)
                        .into(holder.image)

                for (i in 0 until ModelArrayList[position]!!.categoryInfo.size) {
                    d("TestArray", i.toString())
                    if (ModelArrayList[position]!!.categoryInfo[i].categoryId.categoryName.th != "Top") {
                        holder.cate.setText(ModelArrayList[position]!!.categoryInfo[i].categoryId.categoryName.th)
                    }
                }
            }catch (e : Exception){

            }
        }
        else if(holder is ArticleAdapter.LoadingViewHolder){
            try {
                holder.progressBar.isIndeterminate = true
            }catch (e : java.lang.Exception){

            }
        }
    }


    private val inflater: LayoutInflater
    private val arraylist: ArrayList<ArticleData>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<ArticleData>()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_ITEMTYPE){
            val view = LayoutInflater.from(activity)
                    .inflate(R.layout.searchnews_item,parent,false)
            return MyViewHolder(view)
        }else if (viewType == VIEW_LOADINGTYPE){
            val view = LayoutInflater.from(activity)
                    .inflate(R.layout.loading,parent,false)
            return LoadingViewHolder(view)
        }
        return null!!
    }


    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    internal  class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var image : ImageView
        var cate : TextView
        init {
            cate = itemView.findViewById(R.id.categorysearchnews) as TextView
            title = itemView.findViewById(R.id.titlesearchnews) as TextView
            date = itemView.findViewById(R.id.datesearchnews) as TextView
            image = itemView.findViewById(R.id.imagesearchnews) as ImageView
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