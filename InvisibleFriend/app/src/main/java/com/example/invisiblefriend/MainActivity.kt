package com.example.invisiblefriend

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.invisiblefriend.databinding.ActivityMainBinding
import com.example.invisiblefriend.ui.CreateGroup.CreateGroupActivity
import com.example.invisiblefriend.ui.UserAdapter
import com.example.invisiblefriend.ui.CreateGroup.Users
import com.example.invisiblefriend.ui.MapsActivity
import com.example.invisiblefriend.ui.Profile.UserProfileActivity
import com.example.invisiblefriend.ui.groupList.UserGroupsActivity
import com.example.invisiblefriend.ui.login.LoginActivity
import com.example.invisiblefriend.ui.message.MessageAdapter
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.*
import org.json.JSONArray
import org.json.JSONException


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    //private lateinit var createGroupButton:

    private val updatePhoneNumber: EditText? = null
    private val myRef: DatabaseReference? = null

    private var userAdapter: UserAdapter? = null
    private var mUsers: ArrayList<Users> = arrayListOf()


    // firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        auth = FirebaseAuth.getInstance()

        binding.appBarMain.fab.setOnClickListener { view ->
            val createGroupIntent = Intent(this, CreateGroupActivity::class.java)
            startActivity(createGroupIntent)
        }

        if (auth.currentUser?.displayName == null) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.no_username))
                .setCancelable(false)
                .setPositiveButton(
                    getString(R.string.update_profile)
                ) { _, _ ->
                    startActivity(Intent(this@MainActivity, UserProfileActivity::class.java))
                }
                .setNegativeButton(
                    getString(R.string.maybe_later)
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
        /*else if( auth.currentUser?.phoneNumber == null){
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)
            builder.setMessage(getString(R.string.user_without_phoneNumber))
                .setCancelable(false)
                .setPositiveButton(
                    getString(R.string.update_profile)
                ) { _, _ ->
                    input.setHint("Enter Text")
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    builder.setView(input)

                    //startActivity(Intent(this@MainActivity, RegisterActivity::class.java))

                }
                .setNegativeButton(
                    getString(R.string.maybe_later)
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }*/


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        updateUserInformation(navigationView)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_groups, R.id.nav_map, R.id.nav_profile, R.id.nav_logOut
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_logOut) {
                auth.signOut()
                LoginManager.getInstance().logOut()
                val logoutIntent = Intent(this, LoginActivity::class.java)
                logoutIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(logoutIntent)
            }
            if (it.itemId == R.id.nav_groups) {
                val groupsIntent = Intent(this, UserGroupsActivity::class.java)
                startActivity(groupsIntent)
            }
            if (it.itemId == R.id.nav_home) {

                val homeIntent = Intent(this, MainActivity::class.java)
                startActivity(homeIntent)
            }
            if (it.itemId == R.id.nav_profile) {
                val profileIntent = Intent(this, UserProfileActivity::class.java)
                startActivity(profileIntent)
            }

            if (it.itemId == R.id.nav_map) {
                val mapsIntent = Intent(this, MapsActivity::class.java)
                startActivity(mapsIntent)
            }





            false

        }
        //navView.setupWithNavController(navController)

    }

    fun updateProfile() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_logOut) {
            auth.signOut()
            val logoutIntent = Intent(this, LoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        }
        return true
    }

    fun updateUserInformation(navigationView: NavigationView) {
        val header = navigationView?.getHeaderView(0)
        val userEmail = header?.findViewById<TextView>(R.id.tvEmail)
        val userName = header?.findViewById<TextView>(R.id.tvUsername)
        val userImage = header?.findViewById<ImageView>(R.id.imageView)
        userEmail?.text = auth.currentUser?.email
        userName?.text = auth.currentUser?.displayName

        val photoUrl: String =
            auth.currentUser?.photoUrl.toString() + "/&access_token=" + AccessToken.getCurrentAccessToken()?.token //+"?fields={fieldname_of_type_ProfilePictureSource}"
        println("Photo url after login" + photoUrl)
        Glide.with(this).load(photoUrl).into(userImage!!)
    }

    /*
    class UserService {
        var user = FirebaseAuth.getInstance().currentUser
        fun updateUserProfile(username: String?) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener(object : OnCompleteListener<Void?> {
                    override fun onComplete(@NonNull task: Task<Void?>) {
                        if (task.isSuccessful()) {
                            Log.d("Info", "User profile updated.")
                        }
                    }
                })
        }
    }*/
    companion object {
        private const val TAG = "MainActivity"
    }

    private fun UpdateMobileNumberAth(credential: PhoneAuthCredential) {
        val user = FirebaseAuth.getInstance().currentUser!!
        user.updatePhoneNumber(credential).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User mobile updated.", Toast.LENGTH_SHORT).show()
                myRef!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("userMobile")
                    .setValue(updatePhoneNumber?.getText().toString())
                finish()
            } else Log.d(TAG, "User mobile not updated.")
            Toast.makeText(this, "User mobile not updated.", Toast.LENGTH_SHORT).show()
        }
    }
}
/*
    private fun searchForUsersToInvite(str: String){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        var querryUsers = FirebaseDatabase.getInstance().reference
            .child("Users")
            .orderByChild("invitedGroups").startAt(str)
            .orderByValue().equalTo(false)
            .endAt(str+"\uf8ff")
        querryUsers.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(snapshot in p0.children)
                {
                    val user: Users? = p0.getValue(Users::class.java)
                    if(!(user!!.getUID()).equals(firebaseUserID))
                    {
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter = UserAdapter(mUsers!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}


// val url: String ="https://graph.facebook.com/v2.7/me/friends?access_token="+ AccessToken.getCurrentAccessToken()?.token

val c: Cursor? = contentResolver.query(
    RawContacts.CONTENT_URI,
    arrayOf(RawContacts.CONTACT_ID, RawContacts.DISPLAY_NAME_PRIMARY),
    RawContacts.ACCOUNT_TYPE + "= ?",
    arrayOf("com.whatsapp"),
    null
)

val myWhatsappContacts = ArrayList<String>()
val contactNameColumn: Int = c!!.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY)
while (c.moveToNext()) {
    // You can also read RawContacts.CONTACT_ID to read the
    // ContactsContract.Contacts table or any of the other related ones.
    myWhatsappContacts.add(c.getString(contactNameColumn))
}
println("My whatsapp contacts"+myWhatsappContacts)

        val request = GraphRequest.newMyFriendsRequest(
            AccessToken.getCurrentAccessToken(),
            object : GraphRequest.GraphJSONArrayCallback {
                override fun onCompleted(jsonArray: JSONArray?, graphResponse: GraphResponse?) {
                    println("this is my friends list"+graphResponse.toString())
                }
            })
        val bundle = null

        val graphRequestAsyncTask = GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/friends",
            bundle,
            HttpMethod.GET,
            { response ->
                try {
                    val rawName = response.getJSONObject()!!.getJSONArray("data")
                    var friendList = "{\"friendlist\":$rawName}"
                    //String friendlist =  rawName.toString() ;
                    Log.d("TAG", "response of friendlist is : $friendList")



                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }).executeAsync()

*/