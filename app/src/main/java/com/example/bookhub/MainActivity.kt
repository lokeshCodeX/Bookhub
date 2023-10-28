package com.example.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawer:DrawerLayout
    lateinit var coordinator:CoordinatorLayout
    lateinit var toolbar:Toolbar
    lateinit var frame:FrameLayout
    lateinit var nagivationBar:NavigationView
    var previousMenu:MenuItem?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer=findViewById(R.id.drawerLayout)
        coordinator=findViewById(R.id.coordinatorLayout)
        frame=findViewById(R.id.frameLayout)
        toolbar=findViewById<Toolbar>(R.id.toolbarLayout)
        nagivationBar=findViewById(R.id.navigationLayout)

        setUpToolbaar()


        openDashBoard()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,
            drawer, R.string.open_Drawer,R.string.close_drawer




        )
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        nagivationBar.setNavigationItemSelectedListener {
            if (previousMenu!=null){
                previousMenu?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenu=it

            when(it.itemId){
                R.id.dashBoard ->{
                    openDashBoard()
                    drawer.closeDrawers()


                }
                R.id.favourites ->{

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,favouritesFragment()).commit()
                    supportActionBar?.title="Favourites"
                    drawer.closeDrawers()

                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,profileFragment()).commit()
                    supportActionBar?.title="Profile"
                    drawer.closeDrawers()
                }
                R.id.aboutApp ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,aboutAppFragment()).commit()
                    supportActionBar?.title="About App"
                    drawer.closeDrawers()
                }
            }

            return@setNavigationItemSelectedListener true
        }
    }
    fun setUpToolbaar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="BoookStore"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         val id =item.itemId
        if (id==android.R.id.home){
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashBoard(){
        val fragment=fragementLayout()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
        supportActionBar?.title="DashBoard"
        nagivationBar.setCheckedItem(R.id.dashBoard)
    }

    override fun onBackPressed() {

        val  frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is fragementLayout ->
                openDashBoard()

            else -> super.onBackPressed()



        }

    }

}