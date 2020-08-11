package com.zakia.idn.chitchatapp.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.zakia.idn.chitchatapp.R
import com.zakia.idn.chitchatapp.adapter.ChatAdapter
import com.zakia.idn.chitchatapp.model.Chat
import com.zakia.idn.chitchatapp.model.Users
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity : AppCompatActivity() {
    var firebaseUser : FirebaseUser? = null
    var reference : DatabaseReference? = null
    var mChatList : List<Chat>? = null
    var userIdVisit : String = ""
    var chatAdapter : ChatAdapter? = null
    var notify = false

    lateinit var recyclerViewChat : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_message_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recyclerViewChat = findViewById(R.id.rv_chat_message)
        recyclerViewChat.setHasFixedSize(true)
        var linearlayoutManager = LinearLayoutManager(applicationContext)
        linearlayoutManager.stackFromEnd = true
        recyclerViewChat.layoutManager = linearlayoutManager

        reference = FirebaseDatabase.getInstance().reference.child("Users").child(userIdVisit)
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshots: DataSnapshot) {
                val user : Users? = snapshots.getValue(Users::class.java)
                tv_username_mchat.text = user!!.getUsername()
                Picasso.get().load(user.getProfile()).into(iv_profile_mchat)

                retrieveMessage(firebaseUser!!.uid, userIdVisit, user.getProfile())
            }

        })

        attach_image_file_btn.setOnClickListener {
            notify = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image)), 438)
        }

        seenMessage(userIdVisit)

        iv_send_message.setOnClickListener {
            val message = et_message.text.toString()
            if (message == ""){
                Toast.makeText(this, getString(R.string.write_message_fisrt), Toast.LENGTH_LONG).show()
            } else {
                sendMessageToUser(firebaseUser!!.uid, userIdVisit, message)
            }

            et_message.setText("")
        }
    }

    var seenListener : ValueEventListener? = null
    private fun seenMessage(userIdVisit: String) {

    }


    private fun retrieveMessage(senderId: String, receiverId: String, imageProfile: String?) {
        mChatList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshots: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for (snapshot in snapshots.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.getReceiver().equals(senderId) && chat.getSender().equals(receiverId)
                        || chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId)){
                        (mChatList as ArrayList<Chat>).add(chat)
                    }
                    //adapter u/chat

                    chatAdapter = ChatAdapter(this@MessageChatActivity, (mChatList as ArrayList<Chat>), imageProfile!!)
                    recyclerViewChat.adapter = chatAdapter
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 438 && resultCode == Activity.RESULT_OK && data != null && data!!.data != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage(getString(R.string.image_upload))
            progressDialog.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")
            var uploadTask : StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{
                    task ->
                if (!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageHashMap = HashMap<String, Any?>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = "sent you an image"
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["iseen"] = false
                    messageHashMap["url"] = url
                    messageHashMap["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageHashMap).addOnCompleteListener {
                            task ->
                        if (task.isSuccessful){
                            progressDialog.dismiss()

                            val reference = FirebaseDatabase.getInstance().reference
                                .child("Users").child(firebaseUser!!.uid)
                            reference.addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(error: DatabaseError) {

                                }

                                override fun onDataChange(snapshots: DataSnapshot) {
                                    val user = snapshots.getValue(Users::class.java)
                                    if (notify){
                                        sendNotification(userIdVisit,
                                            user!!.getUsername(), "Sent you an image")
                                    }
                                    notify = false
                                }

                            })
                        }
                    }
                }
            }
        }
    }

    private fun sendMessageToUser(senderId: String, receiverId: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["iseen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey

        reference.child("Chats").child(messageKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val chatListReference = FirebaseDatabase.getInstance()
                        .reference.child("ChatList").child(firebaseUser!!.uid)
                        .child(userIdVisit)


                    chatListReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshots: DataSnapshot) {
                            if (!snapshots.exists()){
                                chatListReference.child("id").setValue(userIdVisit)
                                val chatListReceiverReference = FirebaseDatabase.getInstance()
                                    .reference
                                    .child("ChatList")
                                    .child(userIdVisit)
                                    .child(firebaseUser!!.uid)

                                chatListReceiverReference.child("id")
                                    .setValue(firebaseUser!!.uid)
                            }

                        }
                    })
                }
            }

        //push notif
        val userReference = FirebaseDatabase.getInstance().reference
            .child("Users").child(firebaseUser!!.uid)
        userReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshots: DataSnapshot) {
                val user = snapshots.getValue(Users::class.java)
                if (notify){
                    sendNotification(receiverId, user!!.getUsername(), message)
                }
            }
        })

    }

    private fun sendNotification(receiverId: String, username: String?, message: String) {

    }

    override fun onPause() {
        super.onPause()
        reference!!.removeEventListener(seenListener!!)
    }


}