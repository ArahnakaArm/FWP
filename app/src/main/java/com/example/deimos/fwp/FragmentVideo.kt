package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.bookmarkfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.zip.Inflater
data class Video2(val title : String,val date : String)
data class Video(val name : String,val username : String)
class FragmentVideo : Fragment() {
   /* private  val VideoList = listOf(
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","14/12/61"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","15/8/61"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","14/9/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","1/5/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","4/7/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","8/7/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","19/7/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","23/7/60")
    )
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.videofragment,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(ApiService::class.java)

        api.fecthAllUsers().enqueue(object : Callback<List<Video>>{

            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                d("arm","onRespones" )
                showData(response.body()!!)
            }

            override fun onFailure(call: Call<List<Video>>, t: Throwable) {
                d("arm","onFailure")
            }

        })



    }

    companion object {
        fun newInstance(): FragmentVideo = FragmentVideo()
    }
    private fun  showData(users : List<Video>){
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapterVideo(users)
        }

    }



}