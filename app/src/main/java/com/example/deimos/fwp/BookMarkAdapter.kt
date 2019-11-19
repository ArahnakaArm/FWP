package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.articleinfo.*
import kotlinx.android.synthetic.main.itembookmark.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BookMarkAdapter(ctx: Context, private val ModelArrayList: ArrayList<resultDataFav>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<BookMarkAdapter.MyViewHolder>() {
    var mAPIService: ApiService? = null
    private var token : String?=null
    private val inflater: LayoutInflater
    private val arraylist: ArrayList<resultDataFav>
    private var sp : SharedPreferences?=null
    init {

        sp = ctx.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<resultDataFav>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.itembookmark, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookMarkAdapter.MyViewHolder, position: Int) {
        try {
            holder.title.setText(ModelArrayList[position].articleId.articleName.th)
            holder.date.setText(ModelArrayList[position].articleId.updatedAt.substring(0..9))
            holder.itemView.setOnClickListener {
             val intent = Intent(holder.itemView.context, ArticleInfo::class.java)
                intent.putExtra("ID", ModelArrayList[position].articleId._id)
                holder.itemView.context?.startActivity(intent)
            }
            holder.bookmark.setOnClickListener {
                mAPIService = ApiUtils.apiService
                val partnerId = "5dbfe99c776a690010deb237"
                val sdf = SimpleDateFormat("yyMMdd")
                val currentDate = sdf.format(Date())
                val r = (10..12).shuffled().first()
                mAPIService!!.deleteFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),ModelArrayList[position]._id).enqueue(object : Callback<ResponseFav> {
                    override fun onResponse(call: Call<ResponseFav>, response: Response<ResponseFav>) {
                        if (response.isSuccessful) {
                            try {
                                ModelArrayList.removeAt(position)
                                notifyItemRemoved(position)
                            } catch (e: java.lang.Exception) {
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseFav>, t: Throwable) {
                        d("arm","onFailure")
                    }

                })
            }
            Glide.with(holder.itemView.context)
                    .load(holder.URLImage + ModelArrayList[position].articleId.imageThumbnail.path)
                    .into(holder.imageUrl)

        }catch (e : Exception){

        }
    }
    override fun getItemCount(): Int {
        return ModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var imageUrl : ImageView
        var bookmark : ImageView
        var URLImage : String ="http://206.189.41.105:1210/"
        init {
            bookmark = itemView.findViewById(R.id.bookmarkicon) as ImageView
            title = itemView.findViewById(R.id.title) as TextView
            date = itemView.findViewById(R.id.datebookmark) as TextView
            imageUrl = itemView.findViewById(R.id.imageView1) as ImageView

        }


    }
    private fun deleteItem(){



    }

}