package com.example.deimos.fwp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log.d
import java.util.*
import android.content.SharedPreferences


class MainActivity : AppCompatActivity() {
    var x = 0
    var i = 0

    var sp: SharedPreferences? = null

    var usr : Userstate = Userstate()
    val stack = Stack<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val editor = sp?.edit()
        editor?.commit()
        d("armtest",sp?.getBoolean("My_Value", true).toString())
        // stack.push(2)
        /*  li1.setOnClickListener {
            val intent = Intent(this@MainActivity,GalleryInfo::class.java)
            startActivity(intent)
        }*/
        /*  val btt  = findViewById<LinearLayout>(R.id.li1);
        btt.setOnClickListener {
            Toast.makeText(this@MainActivity,"Featured",Toast.LENGTH_SHORT).show()
        }
        val btt2 = findViewById<LinearLayout>(R.id.li2)
        btt2.setOnClickListener {
            Toast.makeText(this@MainActivity,"Article",Toast.LENGTH_SHORT).show()
        }
        val btt3 = findViewById<LinearLayout>(R.id.li3)
        btt3.setOnClickListener {
            Toast.makeText(this@MainActivity,"Video",Toast.LENGTH_SHORT).show()
        }
        val btt4 = findViewById<LinearLayout>(R.id.li4)
        btt4.setOnClickListener {
            Toast.makeText(this@MainActivity,"Gallery",Toast.LENGTH_SHORT).show()
        }
        val bttSearch = findViewById<ImageView>(R.id.search)
        bttSearch.setOnClickListener {
            Toast.makeText(this@MainActivity,"Search",Toast.LENGTH_SHORT).show()
        }
        val bttNotification = findViewById<ImageView>(R.id.notification)
            bttNotification.setOnClickListener {
                Toast.makeText(this@MainActivity,"Notification",Toast.LENGTH_SHORT).show()
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

                //  Toast.makeText(this@MainActivity,item.itemId.toString(),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@MainActivity,"Video",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_favorite -> {
                item.isCheckable = true
                x = 1
                stack.push(x)
                replaceFragment(FragmentComplain())
                ///Toast.makeText(this@MainActivity,"Edit",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_nearby2 -> {
                item.isCheckable = true
                x = 2
                stack.push(x)
                replaceFragment(FragmentHome())
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_nearby3 -> {
                item.isCheckable = true
                x = 3
                stack.push(x)
                replaceFragment(LocationList())
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_nearby4 -> {
                d("armtest",sp?.getBoolean("My_Value", true).toString())
                if (sp?.getBoolean("My_Value", false)==false){
                    replaceFragment(FragmentProfile())
                }else if(sp?.getBoolean("My_Value", false)==true){
                    item.isCheckable = true
                    x = 4
                    stack.push(x)
                    replaceFragment(Profilewithpicture())
                }

                //Toast.makeText(this@MainActivity,"Profile",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }

        }











        false


    }

    public fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()
       // fragmentTransaction.addToBackStack(null)

    }

    override fun onBackPressed() {


        //Toast.makeText(this@MainActivity,stack.pop().toString(),Toast.LENGTH_SHORT).show()
        //bottomNav.menu.getItem(stack.pop()).isCheckable = true


        super.onBackPressed();

    }companion object {
        fun newInstance(): MainActivity = MainActivity()
    }


}


