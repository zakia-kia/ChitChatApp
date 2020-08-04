package com.zakia.idn.chitchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zakia.idn.chitchatapp.fragment.CallFragment
import com.zakia.idn.chitchatapp.fragment.ChatFragment
import com.zakia.idn.chitchatapp.fragment.SettingFragment

class MainActivity : AppCompatActivity() {

    var firebaseUser : FirebaseUser? = null
    var refUsers : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseUser= FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance()
            .reference.child("Users").child(firebaseUser!!.uid)

        val toolBar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolBar)
        supportActionBar!!.title = ""

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager : ViewPager = findViewById(R.id.view_pager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatFragment(), "chats")
        viewPagerAdapter.addFragment(CallFragment(), "call")
        viewPagerAdapter.addFragment(SettingFragment(), "setting")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {

        private val titles : ArrayList<String>
        private val fragments : ArrayList<Fragment>

        init {
            titles = ArrayList()
            fragments = ArrayList()
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}




