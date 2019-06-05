package com.partners.hostpital

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.partners.hostpital.helpers.Constants
import io.paperdb.Paper

class PatientActivity : AppCompatActivity() {


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.book().write(Constants.isDoctor, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        drawerLayout = findViewById(R.id.drawer_layout_patient)
        //Set name
        val navView: NavigationView = findViewById(R.id.nav_view_patient)
        val headerView = navView.getHeaderView(0)
        val fullName = Paper.book().read<String>(Constants.fullName)
        headerView.findViewById<TextView>(R.id.user_name_patient).text = fullName

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_patient) as NavHostFragment? ?: return
        navController = host.navController

        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

    }
    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, drawerLayout)  || super.onSupportNavigateUp()

    override fun onResume() {
        super.onResume()
        val navView: NavigationView = findViewById(R.id.nav_view_patient)
        val headerView = navView.getHeaderView(0)
        val fullName = Paper.book().read<String>(Constants.fullName)
        headerView.findViewById<TextView>(R.id.user_name_patient).text = fullName
    }

    override fun onBackPressed() {
        if ( Paper.book().read<Int>(Constants.hasDoctor) == 0){
            moveTaskToBack(true)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_patient)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.patient, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings_patient -> {
                Paper.book().destroy()
                val intent = Intent(applicationContext, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
