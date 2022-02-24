package com.example.invisiblefriend.ui.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.invisiblefriend.MainActivity
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.ActivityUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private var firebaseUserID: String = ""
    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tbProfile.setNavigationOnClickListener { finish() }

        //setupFirebase
        auth = FirebaseAuth.getInstance()

        instanceFirebase()
        setupLayout()

        binding.btCreateGroup.setOnClickListener {
            updateInfoInDB()
        }
    }

    private fun instanceFirebase() {
        firebaseUserID = auth.currentUser!!.uid
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
    }

    private fun setupLayout() {
        binding.tvUserName.setText(auth.currentUser?.displayName)
        binding.tvEmail.setText(auth.currentUser?.email)

        if(haveMissingData())
            binding.tvInfo.text = this.getString(R.string.have_missing_data)
        else
            binding.tvInfo.text = this.getString(R.string.have_no_missing_data)
    }

    private fun haveMissingData(): Boolean =
        !(!binding.tvUserName.text.isNullOrEmpty() && !binding.tvEmail.text.isNullOrEmpty())

    private fun updateInfoInDB() {
        if (!binding.tvUserName.text.isNullOrEmpty() || !binding.tvEmail.text.isNullOrEmpty()){

            val userHashMap = HashMap<String,Any>()
            userHashMap["uid"] = firebaseUserID

            userHashMap["username"] = binding.tvUserName.text.toString() ?: ""
            userHashMap["email"] = binding.tvEmail.text.toString() ?: ""

            auth.currentUser?.updateEmail(binding.tvEmail.text.toString() ?: "")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(binding.tvUserName.text.toString() ?: "")
                .build()

            var dataSaved =  false

            auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        dataSaved = true
                    }
                }


            refUsers.updateChildren(userHashMap)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        dataSaved = true
                        Log.d("TAG", "Dados atualizados com sucesso na Base de Dados!")

                    }
                }

            if(dataSaved){
                Toast.makeText(this, "Dados atualizados com sucesso!!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
            }
        }
    }
}