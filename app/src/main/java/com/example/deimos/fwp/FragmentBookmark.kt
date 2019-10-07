package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
class FragmentBookmark : Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private val adaptertest: CustomAdapter? = null
    var usr : Userstate = Userstate()
    var array_sort = java.util.ArrayList<BookMarkModel>()

    var bookmarks = ArrayList<BookMarkModel>()

    private  var BookmarkList = listOf(
            Bookmark("ags”\n" +
                    "ทำประเทศไม่พัฒนา","14/12/61"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","15/8/61"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","14/9/60"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","1/5/60"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","4/7/60"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","8/7/60"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","19/7/60"),
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","23/7/60")
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

        bookmarks.add(BookMarkModel("dawr","14/12/61"))
        bookmarks.add(BookMarkModel("hgjg","14/12/61"))
        bookmarks.add(BookMarkModel("agewwcxvs","14/12/61"))
        bookmarks.add(BookMarkModel("agrtrcs","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))
        bookmarks.add(BookMarkModel("agsadths","14/12/61"))



        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
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
                    layoutManager = LinearLayoutManager(activity)
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

            list.add(BookMarkModel("",""))
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(
            context: Context,
            recyclerView: RecyclerView,
            private val clickListener: ClickListener?
    ) : RecyclerView.OnItemTouchListener {

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

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }

    }

}