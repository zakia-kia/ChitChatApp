package com.zakia.idn.chitchatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.zakia.idn.chitchatapp.R
import com.zakia.idn.chitchatapp.fragment.ChatFragment
import com.zakia.idn.chitchatapp.fragment.SettingFragment
import com.zakia.idn.chitchatapp.model.Users

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

    }
}