package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.bookmarkfragment.*
import java.util.zip.Inflater
data class Bookmark(val title : String,val date : String)
class FragmentBookmark : Fragment() {
    private  val BookmarkList = listOf(
            Bookmark("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bookmarkfragment,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapter(BookmarkList)
        }
        backprees.setOnClickListener {

            fragmentManager?.popBackStack()

        }
    }

    companion object {
        fun newInstance(): FragmentBookmark = FragmentBookmark()
    }


}