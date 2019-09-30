package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast

import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

import java.util.ArrayList

class RecyclerTest : Fragment() {
    private val companyViewHolder: CompanyViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recyclertest,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val companies = ArrayList<Company>()

        val googleProduct = ArrayList<Product>()
        googleProduct.add(Product("Google AdSense"))
        googleProduct.add(Product("Google Drive"))
        googleProduct.add(Product("Google Mail"))
        googleProduct.add(Product("Google Doc"))
        googleProduct.add(Product("Android"))

        val google = Company("Google", googleProduct)
        companies.add(google)

        val microsoftProduct = ArrayList<Product>()
        microsoftProduct.add(Product("Windows"))
        microsoftProduct.add(Product("Skydrive"))
        microsoftProduct.add(Product("Microsoft Store"))
        microsoftProduct.add(Product("Microsoft Office"))

        val microsoft = Company("Microsoft", microsoftProduct)
        companies.add(microsoft)

        val adapter = ProductAdapter(companies)
        Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()


        for (i in adapter.groups.size - 1 downTo 0) {
            adapter.toggleGroup(i)
        }


        recyclerView.adapter = adapter
    }



}
