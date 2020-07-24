package com.senjapagi.kolaborasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.senjapagi.kolaborasi.Fragment.fragment_organization_home
import com.senjapagi.kolaborasi.Fragment.fragment_organization_info
import com.senjapagi.kolaborasi.Fragment.fragment_organization_manage_agenda
import com.senjapagi.kolaborasi.Fragment.fragment_organization_manage_divisi
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.PrefEntity
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_organization_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.custom_navdraw.view.*
import kotlinx.android.synthetic.main.header_organization.*
import kotlinx.android.synthetic.main.header_organization.view.*

class OrganizationDashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var fragmentOrgHome: fragment_organization_home
    lateinit var fragmentOrgInfo: fragment_organization_info
    lateinit var fragmentOrgDivisi: fragment_organization_manage_divisi
    lateinit var fragmentOrgAgenda: fragment_organization_manage_agenda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_dashboard)

        actionBar?.title = "Pecinta Kucing Tel-U"
        titleAppbar?.text="Dashboard Admin"

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

        val navView = nav_view.getHeaderView(0)


        downloadPicasso(navView.ndOrgProfile)
        navView.ndOrgNama.text=PrefEntity(this).getPrefString(Constant.NAMA_ENTITAS)
        navView.ndOrgEmail.text=PrefEntity(this).getPrefString(Constant.USERNAME_ENTITAS)
    }
    private fun downloadPicasso(target: ImageView){
        Picasso.get()
            .load(URL.ENTITAS_PROFILE_PIC_URL + PrefEntity(this).getPrefString(Constant.ENTITAS_PROFILE_URL))
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .fit()
            .placeholder(R.drawable.add_profile)
            .error(R.drawable.add_profile)
            .into(target)
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