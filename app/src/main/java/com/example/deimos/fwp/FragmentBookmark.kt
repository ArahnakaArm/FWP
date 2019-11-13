package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.bookmarkfragment.*
import java.util.zip.Inflater
data class Bookmark(val title : String,val date : String)
class FragmentBookmark : androidx.fragment.app.Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private val adaptertest: CustomAdapter? = null
    var usr : Userstate = Userstate()
    var array_sort = java.util.ArrayList<BookMarkModel>()

    var bookmarks = ArrayList<BookMarkModel>()

    private  var BookmarkList = listOf(
            Bookmark("เตือนภัยชาวกรุงเทพ วันนี้ฝุ่นพิษ PM2.5 มีค่าเกินมาตรฐานหลายจุด ส่งผลกระทบต่อสุขภาพประชาชน","14/12/61"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","15/8/61"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","14/9/60")

    )
    var Bok = ArrayList(BookmarkList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bookmarkfragment,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarks.add(BookMarkModel("เตือนภัยชาวกรุงเทพ วันนี้ฝุ่นพิษ PM2.5 มีค่าเกินมาตรฐานหลายจุด ส่งผลกระทบต่อสุขภาพประชาชน","14/12/61","https://scontent.fbkk7-2.fna.fbcdn.net/v/t1.0-9/75233780_2561968107212101_4982463471279931392_o.jpg?_nc_cat=1&_nc_eui2=AeE-c0KKAlhqwpc9NkYnx4jhoHjNYvjlIGrEEWFqYCgknXevzIF0EXu44B4diIaZJFC6RQ9zCvdoB2bWdAR0dE6Cr_00CuMOspdBlzEqxBpQeg&_nc_oc=AQlPLOw_0_3XQpIM3xB75i_75MkeS7n_na7b2Pt9480FNkWtWMGPkfldpQlBGtT7xMI&_nc_ht=scontent.fbkk7-2.fna&oh=6579dcfdb1922398e6748e69f5d25e38&oe=5E453959"))
        bookmarks.add(BookMarkModel("Future Youth Camp ค่ายเยาวชนผู้ไม่ยอมจำนน ภาคอีสาน : เพราะความหลากหลายคือพื้นฐานของประชาธิปไตย","14/12/61","https://scontent.fbkk7-2.fna.fbcdn.net/v/t1.0-9/73256185_2542715819137330_6287071624369799168_o.jpg?_nc_cat=104&_nc_eui2=AeFJ_TO9gYzATJiAvidCNdEFaxNk8C-Prpkhmq2sFGcww1MUnpIOLpW2HQrKW7iXgSb94EKxEA2rPHK5fuUaVylVjrIgXTnmCIyUGYHqqmpXew&_nc_oc=AQkU3UkvJT957Le4n9EPCABEnQ4W9OfFC6GCymtzKp3i5HgabAY4u41nrrISUH4IzTM&_nc_ht=scontent.fbkk7-2.fna&oh=bcc0f5d98e0da8a63a3d6368ded811e5&oe=5E4F0342"))
        bookmarks.add(BookMarkModel("HackaFuture ร่วมสร้าง Application อนาคตใหม่","14/12/61","https://scontent.fbkk7-2.fna.fbcdn.net/v/t1.0-9/71335924_126104602118912_1811943408218931200_o.jpg?_nc_cat=102&_nc_eui2=AeHMKom8wqQza4NCHo_ejjqO2qVpvlcYBus3J0QhPtRycc5acY0Avxo7hG3U4xZVooJwiwsQGDfhWxIaXf5EY2y4WYcRz5cLtjc30cvRTiY3gA&_nc_oc=AQn4UnnTzpbUOtMzSjJYVKWhwXcWy-Beh-LKwg-Fs9YwEL1zx4vyYfWjoYu4Nj7IISg&_nc_ht=scontent.fbkk7-2.fna&oh=280c0b229a3e4ced7ab64ca6b180d89c&oe=5E422467"))




        list_recycler_view.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            adapter = BookMarkAdapter(context,bookmarks)
        }

        ///// Searching /////
        etsearch = view.findViewById(R.id.searchbookmark) as EditText
        array_sort = java.util.ArrayList<BookMarkModel>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in bookmarks.indices) {
                    if (textlength <= bookmarks[i].getNames().length) {
                        Log.d("ertyyy", bookmarks[i].getNames().toLowerCase().trim())
                        if (bookmarks[i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(bookmarks[i])
                        }
                    }
                }
                list_recycler_view.apply {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                    adapter = BookMarkAdapter(context,array_sort)

                }

            }
        })


///// Searching /////






        backprees.setOnClickListener {

            fragmentManager?.popBackStack()

        }
        d("armtesttext",Bok.toString())

        searchbookmark.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adaptertest?.filter?.filter(s)

            }

        })
    }

    companion object {
        fun newInstance(): FragmentBookmark = FragmentBookmark()
    }
    private fun populateList(): java.util.ArrayList<BookMarkModel> {

        val list = java.util.ArrayList<BookMarkModel>()

        for (i in 0..7) {

            list.add(BookMarkModel("","",""))
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

}