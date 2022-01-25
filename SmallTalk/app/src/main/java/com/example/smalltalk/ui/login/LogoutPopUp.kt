package com.example.smalltalk.ui.login

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.smalltalk.R
import com.example.smalltalk.databinding.ActivityLoginBinding
import com.example.smalltalk.databinding.ActivityLogoutPopUpBinding
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