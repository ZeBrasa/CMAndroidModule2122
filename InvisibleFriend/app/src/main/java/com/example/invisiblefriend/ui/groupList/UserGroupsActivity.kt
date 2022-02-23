package com.example.invisiblefriend.ui.groupList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.invisiblefriend.MainActivity
import com.example.invisiblefriend.databinding.ActivityUserGroupsBinding
import com.example.invisiblefriend.ui.DetailsGroupActivity
import com.example.invisiblefriend.ui.Groups
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class UserGroupsActivity: AppCompatActivity(), UserGroupsView.UserGroupsViewListener{
    private var  groups: ArrayList<Groups> = arrayListOf()
    private var  groupsTest: ArrayList<Groups> = arrayListOf()

    private lateinit var binding: ActivityUserGroupsBinding


    private lateinit var auth: FirebaseAuth
    private lateinit var refGroups: DatabaseReference

    private lateinit var dbRefGroups: DatabaseReference
    private lateinit var postListener: ValueEventListener

    private val userGroupsAdapter by lazy{UserGroupsAdapter(this,groups)}


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityUserGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveUserGroups()
        binding.userGroupsRecyclerView.run {
            adapter = userGroupsAdapter
            layoutManager = LinearLayoutManager(this@UserGroupsActivity, RecyclerView.VERTICAL, false)
        }
        binding.backButton.setOnClickListener{
            val backMain = Intent(this, MainActivity::class.java)
            backMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(backMain)
        }


    }
    override fun onGroupClick(groups: Groups) {
        val intent = Intent(this, DetailsGroupActivity::class.java)
        intent.putExtra("EXTRA_GROUP_ID", groups.getUniqueID())
        intent.putExtra("EXTRA_GROUP_NAME", groups.getGroupName())
        intent.putExtra("EXTRA_CREATOR_NAME", groups.getCreatorID())
        intent.putStringArrayListExtra("EXTRA_MEMBERS", groups.getUsersInGroup())
        intent.putExtra("EXTRA_SORTEIO",groups.getSorteio())
        startActivity(intent)
    }


    private fun onSelectGroupView(){

    }


    private fun retrieveUserGroups(){
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
         dbRefGroups = FirebaseDatabase.getInstance().getReference("Groups")

        //dbRefGroups.addValueEventListener(object: ValueEventListener
        postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                groups.clear()
                for(snapshot in p0.children)
                {
                    val group: Groups? = snapshot.getValue(Groups::class.java)
                    if((group!!.getUsersInGroup()).contains(firebaseUserID))   //&&  (group.getUsersInGroup().)
                    {
                        groups.add(group)
                    }

                }

                //dbRefGroups.removeEventListener(this)
                userGroupsAdapter.setItems(groups)

                //createGroupAdapter = CreateGroupAdapter(usersDummy!!)
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        dbRefGroups.addValueEventListener(postListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        dbRefGroups.removeEventListener(postListener)
    }
}