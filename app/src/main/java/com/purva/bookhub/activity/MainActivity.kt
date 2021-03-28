package com.purva.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.purva.bookhub.*
import com.purva.bookhub.fragment.AboutAppFragment
import com.purva.bookhub.fragment.DashboardFragment
import com.purva.bookhub.fragment.FavouriteFragment
import com.purva.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolBar: Toolbar
    lateinit var FrameLayout: FrameLayout
    lateinit var NavigationView: NavigationView

    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolBar = findViewById(R.id.toolBar)
        FrameLayout = findViewById(R.id.frame)
        NavigationView = findViewById(R.id.NavigationView)
        setUpToolBar()

        val setUpToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawr,
            R.string.close_drawr
        )
        drawerLayout.addDrawerListener(setUpToggle)     //making icon functional
        setUpToggle.syncState()         //change hamburger into back arrow

        openDashBoard()

        NavigationView.setNavigationItemSelectedListener {              //adding click listners to the items of navigation drawer

            if (previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId){
                R.id.Dashboard ->{
                    openDashBoard()

                    drawerLayout.closeDrawers()
                }
                R.id.Favourites ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouriteFragment()
                        )

                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }

                R.id.Profil ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )

                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()

                }
                R.id.About ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutAppFragment()
                        )

                        .commit()
                    supportActionBar?.title="About App"
                    drawerLayout.closeDrawers()
                }
            }

            return@setNavigationItemSelectedListener true

        }
    }

    fun setUpToolBar(){
        setSupportActionBar(toolBar)
        supportActionBar?.title="TitleBar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {       //making action bar functional as hamburger is placed on it .

        val id = item.itemId
        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)        //open drawer layout from left

        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashBoard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="DashBoard"
        NavigationView.setCheckedItem(R.id.Dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment ->openDashBoard()
            else->super.onBackPressed()
        }
    }
}