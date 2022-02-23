package com.example.invisiblefriend.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

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
                        val user = auth.currentUser
                        val email: String = user!!.email.toString()
                        val username: String = user!!.displayName.toString()
                        val phoneNumber: String = user!!.phoneNumber.toString()
                        //val phoneNumber: String = "960495251"
                        firebaseUserID = auth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                        val userHashMap = HashMap<String,Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["email"] = email
                        userHashMap["username"] = username
                        userHashMap["phoneNumber"] = phoneNumber
                        userHashMap["inviteGroups"]
                        userHashMap["acceptedGroups"]

                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    updateUI(email, password)
                                }
                            }
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