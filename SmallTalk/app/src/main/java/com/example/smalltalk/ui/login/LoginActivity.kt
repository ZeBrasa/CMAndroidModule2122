package com.example.smalltalk.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.smalltalk.MainActivity
import com.example.smalltalk.R
import com.example.smalltalk.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.example.smalltalk.ui.home.HomeFragment


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegisterHere.setOnClickListener(this)
        binding.container.isVisible = false

        checkLogin()
    }

    private fun checkLogin(){
        val email: String? = intent.getStringExtra("Email")
        val password: String? = intent.getStringExtra("Password")

        if(!email.isNullOrEmpty() && !password.isNullOrEmpty())
            signIn(email, password)
    }

    private fun loginUser() {
         val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.error = getString(R.string.email_is_empty)
            binding.etEmail.requestFocus()
        }else if(binding.etPassword.text.isNullOrEmpty()){
            binding.etPassword.error = getString(R.string.insert_password)
            binding.etPassword.requestFocus()
        }else{
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(baseContext, getString(R.string.authentication_successfully), Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    if((task.exception as FirebaseAuthInvalidUserException)?.errorCode == "ERROR_USER_NOT_FOUND"){
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage(getString(R.string.user_not_identifier))
                            .setCancelable(false)
                            .setPositiveButton(
                                getString(R.string.register)
                            ) { _, _ ->
                                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                            }
                            .setNegativeButton(
                                getString(R.string.close)
                            ) { dialog, id -> dialog.cancel() }
                        val alert = builder.create()
                        alert.show()
                    }else
                        Toast.makeText(baseContext, getString(R.string.authentication_failed, task.exception?.message), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        //start a fragment
        //binding.container.isVisible = true
        //supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment()).commit()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnLogin -> loginUser()
            R.id.tvRegisterHere -> startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}