package com.senjapagi.kolaborasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.senjapagi.kolaborasi.Fragment.fragment_organization_home
import com.senjapagi.kolaborasi.Fragment.fragment_organization_info
import com.senjapagi.kolaborasi.Fragment.fragment_organization_manage_agenda
import com.senjapagi.kolaborasi.Fragment.fragment_organization_manage_divisi
import kotlinx.android.synthetic.main.activity_organization_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main.*

class OrganizationDashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var fragmentOrgHome: fragment_organization_home
    lateinit var fragmentOrgInfo: fragment_organization_info
    lateinit var fragmentOrgDivisi: fragment_organization_manage_divisi
    lateinit var fragmentOrgAgenda: fragment_organization_manage_agenda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_dashboard)

        actionBar?.title = "Pecinta Kucing Tel-U"

        //custom button for drawer toggle
        toggle_drawer.setOnClickListener() {
            //Kalau drawer layout dibuka akan ditutup , vice versa
            // If the navigation drawer is not open then open it, if its already open then close it.
            if (drawerLayoutOrganization.isDrawerOpen(GravityCompat.START)) {
                drawerLayoutOrganization.closeDrawer(GravityCompat.START)
            } else
                drawerLayoutOrganization.openDrawer(GravityCompat.START)
        }


        //setting the original/native drawer Toggle
        //in this app this val actually never used
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayoutOrganization,
            toolBar,
            (R.string.open),
            (R.string.close)
        ) {}

        drawerToggle.isDrawerIndicatorEnabled = false
        drawerLayoutOrganization.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fragmentOrgHome = fragment_organization_home()
        fragmentOrgInfo = fragment_organization_info()
        fragmentOrgDivisi = fragment_organization_manage_divisi()
        fragmentOrgAgenda = fragment_organization_manage_agenda()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragmentOrgHome)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                transactionFragment(fragmentOrgHome)
            }
            R.id.menu2 -> {
                transactionFragment(fragmentOrgDivisi)
            }
            R.id.menu3 -> {
                transactionFragment(fragmentOrgAgenda)
            }
            R.id.menu4 -> {
                transactionFragment(fragmentOrgInfo)
            }
            R.id.nav_landing -> {
                val logout = Logout(this)
                logout.logoutDialog()
            }
        }
        drawerLayoutOrganization.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayoutOrganization.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutOrganization.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun transactionFragment(Fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, Fragment)
            .commit()
    }

}