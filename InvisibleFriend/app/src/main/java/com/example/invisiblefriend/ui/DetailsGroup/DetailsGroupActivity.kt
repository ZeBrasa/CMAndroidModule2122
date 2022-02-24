package com.example.invisiblefriend.ui.DetailsGroup

import android.Manifest
import android.R.attr
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.ActivityDetailsGroupBinding
import com.example.invisiblefriend.ui.CreateGroup.DetailsGroupAdapter
import com.example.invisiblefriend.ui.CreateGroup.Users
import com.example.invisiblefriend.ui.Groups
import com.example.invisiblefriend.ui.InAppRuffleClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.inappmessaging.model.Action
import com.google.firebase.inappmessaging.model.InAppMessage


class DetailsGroupActivity : AppCompatActivity(), DetailsGroupView.FilterViewListener {
    private lateinit var binding: ActivityDetailsGroupBinding
    private var groupID: String = ""
    private var groupName: String? = ""
    private var groupOwnerID: String? = ""
    private var groupOwnerName: String = ""

    private var groupMember: ArrayList<String> = arrayListOf()
    private var groupMemberNames: ArrayList<String> = arrayListOf()
    private var groupMemberEmails: ArrayList<String> = arrayListOf()
    private var sorteio: Boolean = false
    private var newValSorteio: Boolean = false
    private var listShuffle: ArrayMap<String, String> = arrayMapOf()
    private var listFriends: ArrayList<String> = arrayListOf()
    private var listFriendsOf: ArrayList<String> = arrayListOf()
    private var auxList: ArrayList<String> = arrayListOf()
    private var userAux: MutableList<Users> = mutableListOf()
    private var userObject: Users = Users()

    private var groupMembersList: ArrayList<String> = arrayListOf()

    private var yourFriend: String= ""

    private lateinit var firebaseIam: FirebaseInAppMessaging

    private lateinit var auth: FirebaseAuth
    private val detailsGroupAdapter by lazy { DetailsGroupAdapter(this, userAux) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailsGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseIam = FirebaseInAppMessaging.getInstance()
        firebaseIam.isAutomaticDataCollectionEnabled = true
        binding.userGroupsToolbar.setNavigationOnClickListener{finish()}
        getUserList()
        auth = FirebaseAuth.getInstance()
        groupID = intent.getStringExtra("EXTRA_GROUP_ID").toString()
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        groupMember = intent.getStringArrayListExtra("EXTRA_MEMBERS") as ArrayList<String>
        groupOwnerID = intent.getStringExtra("EXTRA_CREATOR_NAME")
        sorteio = intent.getBooleanExtra("EXTRA_SORTEIO", false)
        //listShuffle = intent.getSerializableExtra("EXTRA_SHUFFLE_RESULTS") as ArrayMap<String, String>
        groupOwnerID?.let { getGroupOwner(it) }
        getGroupDetails(groupID)
        groupMember.forEach{
            getUserDetails(it)
        }

        binding.rvListUsersGroup.run{
            adapter = detailsGroupAdapter
            layoutManager = LinearLayoutManager(this@DetailsGroupActivity, RecyclerView.VERTICAL, false)

        }



        if(newValSorteio || sorteio){
            findUserFriend()

            binding.btShuffle.isVisible = false
            println("Your invisible Friend is$yourFriend")
            binding.rvListUsersGroup.isVisible=false
            binding.tvYourFriend.isVisible = true
            //binding.tvYourFriend.text = "Your Invisible friend is $yourFriend"
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
        addClickListener()
        binding.btShuffle.setOnClickListener{
            firebaseIam.triggerEvent("on_ruffle")
            val tombola: ArrayList<Int> = setRangeList(0, groupMemberNames.size - 1)
            shuffleFriends(tombola)
            println("tombola${tombola}")
            var count = 0
            listFriends.clear()
            listFriendsOf.clear()
            for (i in tombola) {
                listFriends.add(groupMemberNames[count])
                listFriendsOf.add(groupMemberNames[i])
                listShuffle[groupMemberNames[count]] = groupMemberNames[i]
                count++
            }
            sorteio = true
            newValSorteio = true
            setUserFriend(listFriends,listFriendsOf)
            if(newValSorteio || sorteio){
                findUserFriend()
                binding.btShuffle.isVisible = false
                println("Your invisible Friend is$yourFriend")
                binding.rvListUsersGroup.isVisible=false
                binding.tvYourFriend.isVisible = true
            }
            println("Shuffle is$listShuffle")

            FirebaseInAppMessaging.getInstance()
            addClickListener()
        }
        /*binding.rvListUsersGroup.run {
            adapter = createGroupAdapter
            layoutManager = LinearLayoutManager(this@DetailsGroupActivity, RecyclerView.VERTICAL, false)
        }*/

    }
    private fun addClickListener() {
        val listener = InAppRuffleClickListener()
        firebaseIam.addClickListener(listener)
    }
    private fun messageClicked(inAppMessage: InAppMessage, action: Action) {
        // Determine which URL the user clicked
        val url = action.actionUrl
        // Get general information about the campaign
        val metadata = inAppMessage.campaignMetadata
        Log.d("AppTest", "URL is: " + url)
        Log.d("AppTest", "Meta Data: " + metadata)
    }

    private fun setUserFriend(listFriends:ArrayList<String>,listFriendsOf: ArrayList<String>){
        val updates = HashMap<String,Any>()
        //val updates2 = HashMap<String,Any>()
        var refFriend = FirebaseDatabase.getInstance().reference.child("Groups").child(groupID)
        //var refFriendOf = FirebaseDatabase.getInstance().reference.child("Groups").child(groupID)
        updates["listFriends"] = listFriends
        updates["listFriendsOf"] = listFriendsOf
        updates["sorteio"] = true
        refFriend.updateChildren(updates)
        //refFriendOf.updateChildren(updates2)

    }

    private fun findUserFriend(){
        val getMyList = FirebaseDatabase.getInstance().getReference("Groups").child(groupID)
        val getMyFriend = FirebaseDatabase.getInstance().getReference("Groups").child(groupID)
        var myIndex = 0

        getMyList.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                auxList.clear()

                println("Print the key"+snapshot.key)
                val group: Groups? = snapshot.getValue(Groups::class.java)
                if (group != null) {
                    auxList=group.getListFriends()
                }
                for(name in auxList){
                    if(name == auth.currentUser?.displayName.toString()){
                        break
                    }
                    myIndex++
                }
                getMyList.removeEventListener(this)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        getMyFriend.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                auxList.clear()
                //for(p0 in snapshot.children) {
                println("Print the key"+snapshot.key)
                val group: Groups? = snapshot.getValue(Groups::class.java)
                if (group != null) {
                    auxList=group.getListFriendsOf()
                    yourFriend=auxList[myIndex]
                    println("Your invisible Friend is$yourFriend")
                    binding.tvYourFriend.text = "Your Invisible friend is: $yourFriend"
                }
                // }
                getMyList.removeEventListener(this)

                //createGroupAdapter = CreateGroupAdapter(usersDummy!!)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


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
            //userAux.clear()
            for(user in it.children){
                var name = ""
                var email = ""
                if(user.key =="email"){
                    name = user.value.toString()
                    this.groupMemberEmails.add(name)

                }
                if(user.key=="username"){
                    val name: String? = user.getValue(String::class.java)
                    //this.groupMemberNames.add(name)
                    if (name != null) {
                        groupMemberNames.add(name)
                        //userObject.setUsername(name)
                       // userAux.add(userObject)
                    }


                }
            }
           // detailsGroupAdapter.setItems(userAux)

            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        /*
        getUsers.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userAux.clear()
                val user: Users? = snapshot.getValue(Users::class.java)
                if(user!=null){
                    userAux.add(user)
                }
            }
            getUsers.removeEventListener(this)
            detailsGroupAdapter.setItems(userAux)

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/

    }
    private fun getUserList(){
        val getUsers = FirebaseDatabase.getInstance().getReference("Users")
        getUsers.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                userAux.clear()
                for( snapshot in p0.children){
                    val user: Users? = snapshot.getValue(Users::class.java)
                    if(user!=null){
                        if(user.getUsername()!="null" && groupMember.contains(user?.getUID())){
                            userAux.add(user)
                        }
                    }
                }

                getUsers.removeEventListener(this)
                detailsGroupAdapter.setItems(userAux)
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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