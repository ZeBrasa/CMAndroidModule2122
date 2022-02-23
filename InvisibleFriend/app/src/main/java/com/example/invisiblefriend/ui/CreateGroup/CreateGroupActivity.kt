package com.example.invisiblefriend.ui.CreateGroup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.invisiblefriend.databinding.ActivityCreateGroupBinding
import com.example.invisiblefriend.ui.Groups
import com.example.invisiblefriend.ui.groupList.UserGroupsActivity
import com.example.invisiblefriend.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreateGroupActivity : AppCompatActivity(), CreateGroupView.FilterViewListener{
    private var  userList: ArrayList<Users>? = null

    /*
    val usersTest = Users(
        uid = "coisa",
        username = "user coisa",
        email = "coisa@coisa.pt",
        phoneNumber = "000"
    )
    */

    //private var usersDummy = mutableListOf(Users("asd", "Ana Perfeita", "sdsg", "sdjfg", isSelectedUser = true))
    private var users: ArrayList<Users> = arrayListOf()

    private var group: Groups = Groups()
    //private var invited: ArrayMap<String, Boolean> = arrayMapOf()
    private var invited: ArrayList<String> = arrayListOf()


    private lateinit var binding: ActivityCreateGroupBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var refGroups: DatabaseReference

    private val createGroupAdapter by lazy { CreateGroupAdapter(this, users) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //initRecyclerView()
        retrieveAllUsers()
        //setup adapter
        binding.rvListUsersGroup.run {
            adapter = createGroupAdapter
            layoutManager = LinearLayoutManager(this@CreateGroupActivity, RecyclerView.VERTICAL, false)
        }
        auth = FirebaseAuth.getInstance()
        binding.btCreateGroup.setOnClickListener {
            onCreateGroupButtonClicked()
            val groupsIntent = Intent(this, UserGroupsActivity::class.java)
            groupsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(groupsIntent)
            //finish()
        }

    }

    override fun onUserClick(users: Users) {
        users.setIsSelected()
        users.getIsSelected()

    }
    private fun onCreateGroupButtonClicked(){
        invited.add(auth.currentUser?.uid.toString())
        users.forEach{
            if(it.getIsSelected()){
                invited.add(it.getUID())
            }

        }
        group.setCreatorID(auth.currentUser?.uid.toString())

        group.setGroupName(binding.tvGroupName.text.toString())
        group.setUsersInGroup(invited)
        group.setSorteio(false)
        group.setUniqueID(binding.tvGroupName.text.toString())
        println("Invited group$invited")
        println("Created group"+group.getUniqueID())
        //val groupListIntent = Intent(this, MainActivity::class.java)
        //key = refGroups.database.getReference("Groups").push().getKey().toString()
        //println("This is the key"+key)
        refGroups = FirebaseDatabase.getInstance().reference.child("Groups").child(auth.currentUser!!.uid+group.getGroupName())
        val groupHashMap = HashMap<String,Any>()
        groupHashMap["groupName"] = group.getGroupName()
        groupHashMap["uniqueID"] = group.getUniqueID()
        groupHashMap["sorteio"] = group.getSorteio()
        groupHashMap["creatorID"] = group.getCreatorID()
        groupHashMap["usersInGroup"] = group.getUsersInGroup()

        refGroups.updateChildren(groupHashMap)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    // Very good very niiice
                }
            }


    }

    private fun retrieveAllUsers(){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        var dbRefUsers = FirebaseDatabase.getInstance().reference.child("Users")

        dbRefUsers.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                users.clear()
                for(snapshot in p0.children) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    if(!(user!!.getUID()).equals(firebaseUserID))
                    {
                        users.add(user)
                    }
                }
                dbRefUsers.removeEventListener(this)
                createGroupAdapter.setItems(users)

                //createGroupAdapter = CreateGroupAdapter(usersDummy!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    /*
    private fun initRecyclerView(){
        binding.rvListUsersGroup.apply{
            layoutManager = LinearLayoutManager(this@CreateGroupActivity)
            createGroupAdapter = CreateGroupAdapter()
            adapter = createGroupAdapter
        }


    }
    */

}
