package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.bookmark.*

import kotlinx.android.synthetic.main.loacationlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

data class LocationModel(var resultCode : String,var developerMessage : String,var resultData : ArrayList<result>,var rowCount : Int)
data class result(var locationName : locationame,var address : address,var map : map,var region : region,var image: imageinfo,var email : String,var _id : String)
data class imageinfo(var path : String)
data class locationame(var en : String,var th : String)
data class address(var en : String ,var th : String)
data class map(var lat: Number , var long : Number)
data class region(var en : String, var th : String)
class LocationList : androidx.fragment.app.Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private var adapter2: LocationChildAdapter? = null
    private val companyViewHolder: CompanyViewHolder? = null
    var mAPIService: ApiService? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(0).isCheckable=true
        view.menu.getItem(0).isChecked=true

        return inflater.inflate(R.layout.loacationlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var array_sort = ArrayList<LocationChildModel>()

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        val companies = ArrayList<Company>()

     /*   val googleProduct2 = ArrayList<LocationChildModel>()
        googleProduct2.add(LocationChildModel("Location 1","Info(Location1)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 2","Info(Location2)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 3","Info(Location3)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 4","Info(Location4)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 5","Info(Location5)","0978512369","Location1@Bankok.com"))

*/

       /* val googleProduct = ArrayList<Product>()
        googleProduct.add(Product("Location 1","Info(Location1)","0978512369","Location1@Bankok.com"))
        googleProduct.add(Product("Location 2","Info(Location2)","0978512369","Location2@Bankok.com"))
        googleProduct.add(Product("Location 3","Info(Location3)","0978512369","Location3@Bankok.com"))
        googleProduct.add(Product("Location 4","Info(Location4)","0978512369","Location4@Bankok.com"))
        googleProduct.add(Product("Location 5","Info(Location5)","0978512369","Location5@Bankok.com"))

        val google = Company("" +
                "ภาคเหนือ", googleProduct)
        companies.add(google)
*/
     /*   val microsoftProduct = ArrayList<Product>()
        microsoftProduct.add(Product("Location 1","Info(Location1)","0978512369","Location1@Central.com"))
        microsoftProduct.add(Product("Location 2","Info(Location2)","0978512369","Location2@Central.com"))
        microsoftProduct.add(Product("Location 3","Info(Location3)","0978512369","Location3@Central.com"))
        microsoftProduct.add(Product("Location 4","Info(Location4)","0978512369","Location4@Central.com"))

        val central = Company("ภาคกลาง", microsoftProduct)
        companies.add(central)


        val north = Company("ภาคใต้", microsoftProduct)
        companies.add(north)


        val south = Company("ภาคตะวันตก", microsoftProduct)
        companies.add(south)

        val east = Company("ภาคตะวันออก", microsoftProduct)
        companies.add(east)

        val west = Company("ภาคตะวันออกเฉียงเหนือ", microsoftProduct)
        companies.add(west)
*/


       // recyclerView.adapter = adapter



        map.setOnClickListener {
            replaceFragment(MapsActivity())
        }



        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        var i = 0
        val northList= ArrayList<Product>()
        val centralList = ArrayList<Product>()
        val westList = ArrayList<Product>()
        val eastList = ArrayList<Product>()
        val eastnorthList = ArrayList<Product>()
        val southList = ArrayList<Product>()
        val bankkok = ArrayList<Product>()
        val mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
        mAPIService!!.getLocation(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<LocationModel> {

            override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {

        ///        d("Location",response.body()!!.resultData[0]!!.locationName.th)
          //      d("Location",response.body()!!.resultData[1]!!.locationName.th.toString())
try {
    for (i in 0..response.body()!!.rowCount.toInt()-1) {
        if(response.body()!!.resultData[i]!!.region.th=="ภาคเหนือ") {

            val googleProduct = ArrayList<Product>()
            var lt : String = response.body()!!.resultData[i].map.lat.toString()
            var ln  : String = response.body()!!.resultData[i].map.long.toString()
            d("Lc",lt)
            d("Lc",ln)
            northList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369", response.body()!!.resultData[i]!!.email,
                    lt,ln,response.body()!!.resultData[i]!!._id))


            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }
        else if(response.body()!!.resultData[i]!!.region.th=="ภาคกลาง") {
            val googleProduct = ArrayList<Product>()
            centralList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))


            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }
        else if(response.body()!!.resultData[i]!!.region.th=="ภาคใต้") {

            southList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }
        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันตก") {
            val googleProduct = ArrayList<Product>()
            westList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))


            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }
        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันออก") {
            val googleProduct = ArrayList<Product>()
            eastList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))


            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }
        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันออกเฉียงเหนือ") {
            val googleProduct = ArrayList<Product>()
            eastnorthList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))


            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }
        else if(response.body()!!.resultData[i]!!.region.th=="กรุงเทพมหานคร") {
            val googleProduct = ArrayList<Product>()
            bankkok.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))


            //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()
        }




    }
    val Bankok = Company("" +
            "กรุงเทพมหานคร", bankkok)
    companies.add(Bankok)
    val North = Company("" +
            "ภาคเหนือ", northList)
    companies.add(North)
    val Central = Company("" +
            "ภาคกลาง", centralList)
    companies.add(Central)
    val South = Company("" +
            "ภาคใต้", southList)
    companies.add(South)
    val  West= Company("" +
            "ภาคตะวันตก", westList)
    companies.add(West)

    val East = Company("" +
            "ภาคตะวันออก", eastList)
    companies.add(East)

    val EastNorth = Company("" +
            "ภาคตะวันออกเฉียงเหนือ", eastnorthList)
    companies.add(EastNorth)





    var adapter = ProductAdapter(companies)
   /* for (i in adapter.groups.size - 1 downTo 0) {
        adapter.toggleGroup(i)
    }*/
    recyclerView.adapter = adapter
    }catch (e : Exception){

}
///// Searching /////


                mProgressDialog.dismiss();

            }
///// Searching /////

            override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                d("arm",t.toString())
            }

        })


















































        ///// Searching /////
   /*  etsearch = view.findViewById(R.id.searchlocation) as EditText
        array_sort = ArrayList<LocationChildModel>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in googleProduct2!!.indices) {
                    if (textlength <= googleProduct2!![i].getNames().length) {
                        Log.d("ertyyy", googleProduct2!![i].getNames().toLowerCase().trim())
                        if (googleProduct2!![i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(googleProduct2[i])
                        }
                        var adapter2 = LocationChildAdapter(view.context,array_sort)
                        recyclerView.adapter=adapter2
                    }
                    else{

                    }

                    if (textlength.equals(0)) {
                        var adapter = ProductAdapter(companies)
                        for (i in adapter.groups.size - 1 downTo 0) {
                            adapter.toggleGroup(i)
                        }
                        recyclerView.adapter=adapter
                        d("zerodetect", "!!")
                    }
                }


            }
        })
*/
///// Searching /////

    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
    //    fragmentTransaction?.addToBackStack(null)
    }
    private fun populateList(): java.util.ArrayList<LocationChildModel> {

        val list = java.util.ArrayList<LocationChildModel>()

        for (i in 0..7) {

            list.add(LocationChildModel("","","",""))
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(
            context: Context,
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            private val clickListener: ClickListener?
    ) : androidx.recyclerview.widget.RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(0).isCheckable=true
        view.menu.getItem(0).isChecked=true

        super.onResume()
    }
}
