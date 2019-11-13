package com.example.deimos.fwp

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.itembookmark.view.*

import java.util.ArrayList

/**
 * Created by Parsania Hardik on 26-Jun-17.
 */
class ComplainAdapter(ctx: Context, private val imageModelArrayList: ArrayList<CompliansData>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ComplainAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<CompliansData>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<CompliansData>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_complian, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplainAdapter.MyViewHolder, position: Int) {





        holder.date.setText(imageModelArrayList[position].updatedAt.substring(0..9))
        holder.id.setText(imageModelArrayList[position]._id)
        holder.type.setText(imageModelArrayList[position].complainType)
        holder.title.setText(imageModelArrayList[position].subject)
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,imageModelArrayList[position].subject,Toast.LENGTH_SHORT).show()
        }

        if (imageModelArrayList[position].status=="Inprogress"){
            holder.status.setImageResource(R.drawable.doingstatus)
        }
        if(imageModelArrayList[position].status=="Done"){
            holder.status.setImageResource(R.drawable.successstatus)
        }
        if(imageModelArrayList[position].status=="Cancle"){
            holder.status.setImageResource(R.drawable.cancelstatus)
        }
        if(imageModelArrayList[position].status=="New"){
            holder.status.setImageResource(R.drawable.recivedstatus)
        }

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date : TextView
        var status : ImageView
        var id : TextView
        var type : TextView

        init {
            type = itemView.findViewById(R.id.typeComplian) as TextView
            id = itemView.findViewById(R.id.idComplian) as TextView
            title = itemView.findViewById(R.id.topicComplian) as TextView
            date = itemView.findViewById(R.id.dateComplian) as TextView
            status = itemView.findViewById(R.id.status) as ImageView


        }


    }
}