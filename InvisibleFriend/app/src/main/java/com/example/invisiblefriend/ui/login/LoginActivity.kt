package com.example.invisiblefriend.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.invisiblefriend.MainActivity
import com.example.invisiblefriend.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.example.invisiblefriend.R
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.internal.fuseable.HasUpstreamPublisher

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""


    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegisterHere.setOnClickListener(this)
        binding.container.isVisible = false
        checkLogin()
        facebookButton=findViewById(R.id.login_button_facebook)
        callbackManager = CallbackManager.Factory.create()
        facebookButton.setPermissions(listOf("email", "public_profile","user_photos","openid","user_friends"))
        facebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
                val user: FirebaseUser? = auth.getCurrentUser()
                val photoUrl: String = auth.currentUser?.photoUrl.toString()+"/picture?height=500&access_token="+loginResult.accessToken.token
                println("Photo url "+photoUrl)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }

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
                    //getEmailCredentials()
                    //Toast.makeText(baseContext, getString(R.string.authentication_successfully), Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    when {
                        (task.exception as FirebaseAuthException)?.errorCode == "ERROR_USER_NOT_FOUND" -> {
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

                        }
                        (task.exception as FirebaseAuthInvalidCredentialsException)?.errorCode == "ERROR_CREDENTIAL_ALREADY_IN_USE" -> Toast.makeText(this@LoginActivity, "CREDENTIAL ALREADY IN USE",
                            Toast.LENGTH_SHORT).show()
                        (task.exception as FirebaseAuthException)?.errorCode == "ERROR_INVALID_EMAIL" ->Toast.makeText(this@LoginActivity, "INVALID EMAIL",
                            Toast.LENGTH_SHORT).show()
                        (task.exception as FirebaseAuthInvalidCredentialsException)?.errorCode == "ERROR_PASSWORD_NOT_FOUND" -> Toast.makeText(this@LoginActivity, "Use Facebook to athenticate",
                            Toast.LENGTH_SHORT).show()
                        (task.exception as FirebaseAuthInvalidCredentialsException)?.errorCode == "ERROR_INVALID_PASSWORD"-> Toast.makeText(this@LoginActivity, "Invalid password, try again",
                            Toast.LENGTH_SHORT).show()
                        else -> {
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage(getString(R.string.user_used_facebook))
                                .setCancelable(false)
                                .setNegativeButton(
                                    getString(R.string.close)
                                ) { dialog, id -> dialog.cancel() }
                            val alert = builder.create()
                            alert.show()

                        }

                    }
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
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
                                updateUI(user)
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    companion object {
        private const val TAG = "LoginActivity"
    }
    private fun getGoogleCredentials() {
        val googleIdToken = ""
        // [START auth_google_cred]
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        // [END auth_google_cred]
    }

    private fun getFbCredentials() {
        val token = AccessToken.getCurrentAccessToken()
        // [START auth_fb_cred]
        val credential = FacebookAuthProvider.getCredential(token!!.token)
        // [END auth_fb_cred]
    }

    private fun getEmailCredentials() {
        val email = ""
        val password = ""
        // [START auth_email_cred]
        val credential = EmailAuthProvider.getCredential(email, password)
        // [END auth_email_cred]
    }
    private fun linkAccount() {
        // Create EmailAuthCredential with email and password
        val credential = EmailAuthProvider.getCredential("", "")
        // [START link_credential]
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "linkWithCredential:success")
                    val user = task.result?.user
                    updateUI(user)
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END link_credential]
    }
}