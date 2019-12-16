package com.example.deimos.fwp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_bookmark.view.*
import kotlinx.android.synthetic.main.loading.view.*
import org.w3c.dom.Text

import java.util.ArrayList

/**
 * Created by Parsania Hardik on 26-Jun-17.
 */
class ArticleAdapter(recyclerView: RecyclerView,ctx: Context,var activity: Activity, private val ModelArrayList: ArrayList<ArticleData>) : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_LOADINGTYPE = 1
    val VIEW_ITEMTYPE = 0
    internal var  loadMore : ILoadMore ? =null
    internal var isLoading: Boolean=false
    internal var  visibleThreshold = 4
    internal var  lastVisibleItem : Int =0
    internal var totalItemCount : Int = 0
    private val inflater: LayoutInflater
    private val arraylist: ArrayList<resultData3>

    init {

        val grid = recyclerView.layoutManager as GridLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = grid.itemCount
                lastVisibleItem = grid.findLastVisibleItemPosition()
                if(!isLoading && totalItemCount <= lastVisibleItem+visibleThreshold){
                    if(loadMore != null){
                        d("Detect",totalItemCount.toString())
                        d("Detect","Yes")
                        loadMore!!.onLoadMore()
                    isLoading = true
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })


        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<resultData3>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if(viewType == VIEW_ITEMTYPE){
            val view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_article,parent,false)
            return MyViewHolder(view)
        }else if (viewType == VIEW_LOADINGTYPE){
            val view = LayoutInflater.from(activity)
                    .inflate(R.layout.loading,parent,false)
            return LoadingViewHolder(view)
        }
            return null!!
    }

    override fun getItemViewType(position: Int): Int {
        return if(ModelArrayList[position]==null)VIEW_LOADINGTYPE else VIEW_ITEMTYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        if(holder is MyViewHolder){
            var dateFormat = ModelArrayList[position].updatedAt.substring(0..10)
            d("DateChange",dateFormat)
            var dateOutput = Profilewithpicture.ConvertDate.ChangeFormatDate(dateFormat.substring(0..3),dateFormat.substring(5..6),dateFormat.substring(8..9))
            d("DateChange",dateOutput)
            for (i in 0 until ModelArrayList[position].categoryInfo.size) {
                d("TestArray",i.toString())
                if (ModelArrayList[position].categoryInfo[i].categoryId.categoryName.th != "Top") {
                    holder.cate.setText(ModelArrayList[position].categoryInfo[i].categoryId.categoryName.th)
                }
            }
            //holder.cate.setText(ModelArrayList[position].categoryId.categoryName.th)
            holder.date.setText(dateOutput)
            holder.title.setText(ModelArrayList[position].articleName.th)
            Glide.with(holder.itemView.context)
                    .load(ModelArrayList[position].imageThumbnail)
                    .into(holder.image)

            holder.itemView.setOnClickListener {
                try {
                    val intent = Intent(holder.itemView.context, ArticleInfo::class.java)
                    intent.putExtra("ID", ModelArrayList[position]._id)
                    holder.itemView.context?.startActivity(intent)
                }catch (e:Exception){}

            }
        }
        else if(holder is LoadingViewHolder){
            holder.progressBar.isIndeterminate = true
        }




       // holder.category.setText(ModelArrayList[position].categoryId.categoryName.th)
/*
        val titleExtra: String = ModelArrayList[position].videoName.th
        val dateExtra : String = ModelArrayList[position].updatedAt.substring(range = 0..9)

        */






    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
    }
    internal class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title = itemView.articleTopic
        var date = itemView.articleDate
        var image = itemView.articleImage
        var cate = itemView.category

        /*
           var date : TextView
           var image : ImageView
           var cate : TextView

           init {
               cate = itemView.findViewById(R.id.category) as TextView

               date = itemView.findViewById(R.id.articleDate) as TextView
               image=itemView.findViewById(R.id.articleImage) as ImageView

           }*/
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
