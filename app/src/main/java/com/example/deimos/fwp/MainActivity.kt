package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log.d
import java.util.*
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.widget.Toast
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {
    var x = 0
    var i = 0

    var sp: SharedPreferences? = null

    var usr : Userstate = Userstate()
    val stack = Stack<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)


           var  info : PackageInfo = packageManager.getPackageInfo("com.example.deimos.fwp", PackageManager.GET_SIGNATURES)
          for(signature:Signature in info.signatures) {
              var md: MessageDigest = MessageDigest.getInstance("SHA")
              md.update(signature.toByteArray())
              d("KeyHash", Base64.getEncoder().encodeToString(md.digest()))
          }







        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val editor = sp?.edit()
        editor?.commit()
        d("armtest",sp?.getBoolean("LogIn_State", true).toString())
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
        replaceFisrtFragment(FragmentHome())

    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.video -> {
                item.isCheckable = true
                x = 0
                stack.push(x)
                replaceFragment(FragmentVideo())

                //  Toast.makeText(this@MainActivity,item.itemId.toString(),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@MainActivity,"Video",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.complain -> {
               item.isCheckable = false
                x = 1
                stack.push(x)
                val intent = Intent(this@MainActivity,ComplainHolder::class.java)
                startActivity(intent)

                ///Toast.makeText(this@MainActivity,"Edit",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                item.isCheckable = true
                x = 2
                stack.push(x)
                replaceFragment(FragmentHome())
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.location -> {
                item.isCheckable = false
                x = 3
                stack.push(x)
                replaceFragment(LocationList())
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                val intent = Intent(this@MainActivity, LogInAndProfileHolder::class.java)
                this@MainActivity.startActivity(intent)

                return@OnNavigationItemSelectedListener true
            }

        }











        false


    }

    public fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()


    }
    public fun replaceFisrtFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()


    }

    override fun onBackPressed() {

        super.onBackPressed();

    }
    companion object {
        fun newInstance(): MainActivity = MainActivity()
    }

    override fun onResume() {
        d("tokentest","sad")
        super.onResume()
    }


}


