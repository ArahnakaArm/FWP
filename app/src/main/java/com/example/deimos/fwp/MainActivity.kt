package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log.d
import java.util.*
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import java.security.MessageDigest
import androidx.core.content.edit


class MainActivity : AppCompatActivity() {
    var x = 0
    var i = 0
    var sharedPreferences : SharedPreferences?=null
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
        bottomNav.menu.getItem(4).isCheckable = false

        replaceFragment(FragmentHome())
        d("Test","Ye")

    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {

            R.id.video -> {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true
                item.isCheckable = true
                x = 0
                stack.push(x)
                replaceFisrtFragment(FragmentVideo())
                item.isEnabled=false
                //  Toast.makeText(this@MainActivity,item.itemId.toString(),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@MainActivity,"Video",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.complain -> {

                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true
                bottomNav.menu.getItem(3).isCheckable = false

                if(sp?.getBoolean("LogIn_State", false)==false){
                    val intent = Intent(this@MainActivity, LogInWithSkip::class.java)
                    startActivity(intent)
                }else {
                    val intent = Intent(this@MainActivity, Complian::class.java)
                    startActivity(intent)
                }

              /* if(sp?.getBoolean("LogIn_State", false)==false) {
                   replaceFisrtFragment(LogInWithSkip())
                }else{
                   replaceFisrtFragment(ComplianHolder())
                }*/
             /*  val intent = Intent(this@MainActivity,ComplainHolder::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
*/
                ///Toast.makeText(this@MainActivity,"Edit",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                item.isCheckable = true
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true
                x = 2
                stack.push(x)
                replaceFisrtFragment(FragmentHome())
                item.isEnabled=false
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.location -> {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true

                bottomNav.menu.getItem(4).isEnabled = true
                item.isCheckable = false
                x = 3
                stack.push(x)
                replaceFisrtFragment(LocationList())
                item.isEnabled=false
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {


                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
            val goLogIn = Intent(this@MainActivity,LogIn::class.java)
           if(sp?.getBoolean("LogIn_State", false)==false) {
               this@MainActivity.startActivity(goLogIn)

                }else{
             replaceFisrtFragment(Profilewithpicture())
                }
              /*  val intent = Intent(this@MainActivity, LogInAndProfileHolder::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION*/


                return@OnNavigationItemSelectedListener true

            }

        }


        false


    }

    public fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment,"fragment_tag").commit()
       /* fragmentTransaction
                .add(R.id.fragmentcontainer, fragment, "frag1")
                .commit()
*/

    }
    public fun replaceFisrtFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()


    }
    public fun replaceBack(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment,"BACK")
        fragmentTransaction.commit()


    }


    /* override fun onBackPressed() {


            if (supportFragmentManager.backStackEntryCount <= 1) {
                finish()
            } else {
                super.onBackPressed()
            }




      /*
        /*
        val count = supportFragmentManager.backStackEntryCount
        d("Test",count.toString())
        if(count == 1) {
            d("Test", "OK")
        }
        supportFragmentManager.popBackStackImmediate()*/
        super.onBackPressed();
*/
    }*/
   override fun onBackPressed() {
       val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
       bottomNav.menu.getItem(0).isEnabled = true
       bottomNav.menu.getItem(1).isEnabled = true
       bottomNav.menu.getItem(2).isEnabled = true
       bottomNav.menu.getItem(3).isEnabled = true
       bottomNav.menu.getItem(4).isEnabled = true



        val fragmentManager = supportFragmentManager

        val currentFragment = fragmentManager.findFragmentById(R.id.fragmentcontainer)

        if (currentFragment!!.tag == "fragment_tag") {
            d("BACK","Ar")
            finish()

        }
        if (supportFragmentManager.backStackEntryCount == 0 && currentFragment!!.tag != "fragment_tag"){
            replaceFragment(FragmentHome())
        }
       supportFragmentManager.popBackStack()
   }


    override fun onResume() {
        super.onResume()

        if(sp!!.getBoolean("isFromLogin",false)==true && (sp!!.getBoolean("LogIn_State",false))==true ) {
            replaceFisrtFragment(Profilewithpicture())
            sp!!.edit {
                putBoolean("isFromLogin", false)
            }

        }
        d("Resume","YES")
    }
}


