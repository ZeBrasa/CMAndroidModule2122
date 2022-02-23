package com.example.invisiblefriend.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.core.view.isVisible
import com.example.invisiblefriend.MainActivity
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.ActivityDetailsGroupBinding
import com.example.invisiblefriend.ui.CreateGroup.CreateGroupView
import com.example.invisiblefriend.ui.CreateGroup.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class DetailsGroupActivity : AppCompatActivity(), CreateGroupView.FilterViewListener {
    private lateinit var binding: ActivityDetailsGroupBinding
    private var groupID: String? = ""
    private var groupName: String? = ""
    private var groupOwnerID: String? = ""
    private var groupOwnerName: String = ""

    private var groupMember: ArrayList<String> = arrayListOf()
    private var groupMemberNames: ArrayList<String> = arrayListOf()
    private var groupMemberEmails: ArrayList<String> = arrayListOf()
    private var sorteio: Boolean = false
    private var listShuffle: ArrayMap<String, String> = arrayMapOf()

    private lateinit var auth: FirebaseAuth

    //private val createGroupAdapter by lazy { CreateGroupAdapter(this, group) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        groupID = intent.getStringExtra("EXTRA_GROUP_ID")
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        groupMember = intent.getStringArrayListExtra("EXTRA_MEMBERS") as ArrayList<String>
        groupOwnerID = intent.getStringExtra("EXTRA_CREATOR_NAME")
        sorteio = intent.getBooleanExtra("EXTRA_SORTEIO", false)
        groupOwnerID?.let { getGroupOwner(it) }
        groupID?.let { getGroupDetails(it) }
        println("group Owner name$groupOwnerName")
        groupMember.forEach{
            getUserDetails(it)
        }
        if(sorteio){

        }
        else{
            binding.btShuffle.isVisible = groupOwnerID==auth.currentUser?.uid.toString()
        }

        setupLayout()
    }

    private fun setupLayout() {
        //o get deve ser do nome do criador do grupo, nao o get do nome do grupo
        //groupObject = groupInfo.get(0)
        //binding.tvCreatorName.text = getString(R.string.creator_name, groupOwnerName)//group?.getCreatorName())
        binding.userGroupsToolbar.title = groupName

        binding.btShuffle.setOnClickListener{
            val tombola: ArrayList<Int> = setRangeList(0, groupMemberNames.size - 1)
            shuffleFriends(tombola)
            println("tombola${tombola}")
            var count = 0
            for (i in tombola) {
                listShuffle[groupMemberNames[count]] = groupMemberNames[i]
                count++

            }
            println("Shuffle is$listShuffle")


        }
        /*binding.rvListUsersGroup.run {
            adapter = createGroupAdapter
            layoutManager = LinearLayoutManager(this@DetailsGroupActivity, RecyclerView.VERTICAL, false)
        }*/
    }
    private fun getGroupDetails(groupID: String){
        val getGroups = FirebaseDatabase.getInstance().getReference("Groups")
        getGroups.child(groupID).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
    private fun getGroupOwner(ownerID: String){
        val getOwner = FirebaseDatabase.getInstance().getReference("Users")
        getOwner.child(ownerID).get().addOnSuccessListener {
            for(owner in it.children){
                if(owner.key=="username"){

                    groupOwnerName = owner.value.toString()
                    println("group Owner name$groupOwnerName")
                    binding.tvCreatorName.text = getString(R.string.creator_name, groupOwnerName)
                }


            }
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
    private fun getUserDetails(userID: String){
        val getUsers = FirebaseDatabase.getInstance().getReference("Users")
        getUsers.child(userID).get().addOnSuccessListener {
            for(user in it.children){
                if(user.key =="email"){
                    groupMemberEmails.add(user.value.toString())
                }
                if(user.key=="username"){

                    groupMemberNames.add(user.value.toString())
                }


            }
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
    override fun onUserClick(users: Users) {
        TODO("Not yet implemented")
    }

    private fun checkWriteExternalPermission(): Boolean {
        val pm = this.baseContext.packageManager
        val hasPerm = pm.checkPermission(
            Manifest.permission.SEND_SMS,
            this.baseContext.packageName
        )
        return hasPerm == PackageManager.PERMISSION_GRANTED
    }
    private fun setRangeList(min: Int, max: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (i in min..max) {
            list.add(i)
        }
        return list
    }
    private fun shuffleFriends(listToShuffle: ArrayList<Int>){
        while(!checkPairMatch(listToShuffle)){
            listToShuffle.shuffle()
        }

    }
    private fun checkPairMatch(listToShuffle: ArrayList<Int>): Boolean {

        assert(listToShuffle.size === groupMemberNames.size)
        for (i in 0 until groupMemberNames.size) {
            if (i == listToShuffle[i]) {
                return false
            }

        }
        return true
    }




}