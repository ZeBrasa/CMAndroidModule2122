package com.example.smalltalk.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smalltalk.R
import com.example.smalltalk.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener(this)
        binding.tvLoginHere.setOnClickListener(this)
    }

    private fun createUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.isEmpty()) {
            binding.etEmail.error = getString(R.string.email_is_empty)
            binding.etEmail.requestFocus()
        }else if(password.isEmpty()){
            binding.etPassword.error = getString(R.string.password_is_empty)
            binding.etPassword.requestFocus()
        }else
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, getString(R.string.authentication_successfully), Toast.LENGTH_SHORT).show()
                        updateUI(email, password)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, getString(R.string.authentication_failed, task.exception), Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun updateUI(email: String, password: String) {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)
            .putExtra("Email", email)
            .putExtra("Password", password))
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnRegister -> createUser()
            R.id.tvLoginHere -> startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

}