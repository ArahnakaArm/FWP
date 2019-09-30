package com.example.deimos.fwp

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.*
import kotlinx.android.synthetic.main.homefragment.*
import java.util.*

class ManiActivity : AppCompatActivity() {
    var x = 0
    var i = 0
    val stack = Stack<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mani)
        // stack.push(2)
        /*  li1.setOnClickListener {
            val intent = Intent(this@ManiActivity,GalleryInfo::class.java)
            startActivity(intent)
        }*/
        /*  val btt  = findViewById<LinearLayout>(R.id.li1);
        btt.setOnClickListener {
            Toast.makeText(this@ManiActivity,"Featured",Toast.LENGTH_SHORT).show()
        }
        val btt2 = findViewById<LinearLayout>(R.id.li2)
        btt2.setOnClickListener {
            Toast.makeText(this@ManiActivity,"Article",Toast.LENGTH_SHORT).show()
        }
        val btt3 = findViewById<LinearLayout>(R.id.li3)
        btt3.setOnClickListener {
            Toast.makeText(this@ManiActivity,"Video",Toast.LENGTH_SHORT).show()
        }
        val btt4 = findViewById<LinearLayout>(R.id.li4)
        btt4.setOnClickListener {
            Toast.makeText(this@ManiActivity,"Gallery",Toast.LENGTH_SHORT).show()
        }
        val bttSearch = findViewById<ImageView>(R.id.search)
        bttSearch.setOnClickListener {
            Toast.makeText(this@ManiActivity,"Search",Toast.LENGTH_SHORT).show()
        }
        val bttNotification = findViewById<ImageView>(R.id.notification)
            bttNotification.setOnClickListener {
                Toast.makeText(this@ManiActivity,"Notification",Toast.LENGTH_SHORT).show()
            }

      */
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        bottomNav.menu.getItem(0).isCheckable = false
        replaceFragment(FragmentHome())

    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.item_recent -> {
                item.isCheckable = true
                x = 0
                stack.push(x)
                replaceFragment(FragmentVideo())

                //  Toast.makeText(this@ManiActivity,item.itemId.toString(),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@ManiActivity,"Video",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_favorite -> {
                item.isCheckable = true
                x = 1
                stack.push(x)
                replaceFragment(FragmentComplain())
                ///Toast.makeText(this@ManiActivity,"Edit",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_nearby2 -> {
                item.isCheckable = true
                x = 2
                stack.push(x)
                replaceFragment(FragmentHome())
                //Toast.makeText(this@ManiActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_nearby3 -> {
                item.isCheckable = true
                x = 3
                stack.push(x)
                replaceFragment(RecyclerTest())
                //Toast.makeText(this@ManiActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_nearby4 -> {
                item.isCheckable = true
                x = 4
                stack.push(x)
                replaceFragment(Profilewithpicture())
                //Toast.makeText(this@ManiActivity,"Profile",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }

        }











        false


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()
       // fragmentTransaction.addToBackStack(null)

    }

    override fun onBackPressed() {


        //Toast.makeText(this@ManiActivity,stack.pop().toString(),Toast.LENGTH_SHORT).show()
        //bottomNav.menu.getItem(stack.pop()).isCheckable = true


        super.onBackPressed();

    }


}


