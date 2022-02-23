package com.example.invisiblefriend.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log

import androidx.lifecycle.ViewModel

import com.example.invisiblefriend.ui.message.MessageAdapter
import com.example.invisiblefriend.ui.message.Messages

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter


    fun onCreate(savedInstanceState: Bundle?) {

    }

    private fun onImageSelected(uri: Uri) {

        Log.d("GroupMessaging", "Uri: $uri")
        val user = auth.currentUser
        val tempMessage = Messages(null, getUserName(), getPhotoUrl(), "https://www.google.com/images/spin-32.gif")
        db.reference
            .child("messages")
            .push()
            .setValue(
                tempMessage,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Log.w(
                            "GroupMessaging", "Unable to write message to database.",
                            databaseError.toException()
                        )
                        return@CompletionListener
                    }
                    val key = databaseReference.key
                    val storageReference = Firebase.storage
                        .getReference(user!!.uid)
                        .child(key!!)
                        .child(uri.lastPathSegment!!)
                })
    }
    private fun getPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else "ANONYMOUS"
    }

}