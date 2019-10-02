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
import kotlinx.android.synthetic.main.recyclertest.*

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
        googleProduct.add(Product("Location 1","Info(Location1)","0978512369","Location1@Bankok.com"))
        googleProduct.add(Product("Location 2","Info(Location2)","0978512369","Location2@Bankok.com"))
        googleProduct.add(Product("Location 3","Info(Location3)","0978512369","Location3@Bankok.com"))
        googleProduct.add(Product("Location 4","Info(Location4)","0978512369","Location4@Bankok.com"))
        googleProduct.add(Product("Location 5","Info(Location5)","0978512369","Location5@Bankok.com"))

        val google = Company("" +
                "Bankok And Metropolitan Region", googleProduct)
        companies.add(google)

        val microsoftProduct = ArrayList<Product>()
        microsoftProduct.add(Product("Location 1","Info(Location1)","0978512369","Location1@Central.com"))
        microsoftProduct.add(Product("Location 2","Info(Location2)","0978512369","Location2@Central.com"))
        microsoftProduct.add(Product("Location 3","Info(Location3)","0978512369","Location3@Central.com"))
        microsoftProduct.add(Product("Location 4","Info(Location4)","0978512369","Location4@Central.com"))

        val microsoft = Company("Central Region", microsoftProduct)
        companies.add(microsoft)

        val adapter = ProductAdapter(companies)
        //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()


        for (i in adapter.groups.size - 1 downTo 0) {
            adapter.toggleGroup(i)
        }


        recyclerView.adapter = adapter


        map.setOnClickListener {
            replaceFragment(MapMarker())
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }

}
