package com.example.deimos.fwp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList
class ComplianAdapter(ctx: Context, private val ModelArrayList: ArrayList<CompliansData>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ComplianAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<CompliansData>

    init {

        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<CompliansData>()
        // this.arraylist.addAll(Search.movieNamesArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplianAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.item_complian, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplianAdapter.MyViewHolder, position: Int) {





        holder.date.setText(ModelArrayList[position].updatedAt.substring(0..9))
        holder.id.setText(ModelArrayList[position].complainNumber)
        holder.type.setText(ModelArrayList[position].complainType)
        holder.title.setText(ModelArrayList[position].subject)
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,ModelArrayList[position].subject,Toast.LENGTH_SHORT).show()
        }

        if (ModelArrayList[position].status=="Inprogress"){
            holder.status.setImageResource(R.drawable.doingstatus)
        }
        if(ModelArrayList[position].status=="Done"){
            holder.status.setImageResource(R.drawable.successstatus)
        }
        if(ModelArrayList[position].status=="Cancle"){
            holder.status.setImageResource(R.drawable.cancelstatus)
        }
        if(ModelArrayList[position].status=="New"){
            holder.status.setImageResource(R.drawable.recivedstatus)
        }

    }

    override fun getItemCount(): Int {
        return ModelArrayList.size
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