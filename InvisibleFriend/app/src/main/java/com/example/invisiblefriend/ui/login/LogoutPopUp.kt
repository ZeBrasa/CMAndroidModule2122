package com.example.invisiblefriend.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LogoutPopUp : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.txt_ok -> auth.signOut()
        }
    }

}