package com.zakia.idn.chitchatapp.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.zakia.idn.chitchatapp.R
import com.zakia.idn.chitchatapp.model.Users
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment() {

    var userReference : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null
    private val RequestCode = 438
    private val imageUri : Uri? = null
    private var storageRef : StorageReference? = null
    private var coverCheck : String? = ""
    private var sosialMedia : String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().reference
            .child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        userReference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshots: DataSnapshot) {
                if (snapshots.exists()){
                    val user : Users? = snapshots.getValue(
                        Users::class.java)

                    if (context != null){
                        view.tv_username_setting.text = user!!.getUsername()
                        Picasso.get().load(user.getProfile()).into(view.iv_profile_setting)
                    }
                }

            }
        })

        return view
    }

}