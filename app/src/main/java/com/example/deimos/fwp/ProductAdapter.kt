package com.example.deimos.fwp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.support.v4.app.FragmentActivity



class ProductAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<CompanyViewHolder, ProductViewHolder>(groups) {
    private val companyViewHolder: CompanyViewHolder? = null
    private val context: Context? = null
    private val myContext: FragmentActivity? = null
    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.expandable_recyclerview_company, parent, false)

        return CompanyViewHolder(v)

    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.expandable_recyclerview_product, parent, false)

        return ProductViewHolder(v)
    }

    override fun onBindChildViewHolder(holder: ProductViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {

        val product = group.items[childIndex] as Product
        holder.bind(product)
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, product.name + "+" + product.info, Toast.LENGTH_SHORT).show()
            val nameExtra : String = product.name
            val infoExtra : String = product.info
            val telExtra : String = product.tel
            val emailExtra : String = product.email
            val intent = Intent(holder.itemView.context, LocationContent::class.java)
            intent.putExtra("Name",nameExtra)
            intent.putExtra("Info",infoExtra)
            intent.putExtra("Tel",telExtra)
            intent.putExtra("Email",emailExtra)
            holder.itemView.context?.startActivity(intent)


        }


    }

    override fun onBindGroupViewHolder(holder: CompanyViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        val company = group as Company
        holder.bind(company)


    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = myContext?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }

}
