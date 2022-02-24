package com.example.invisiblefriend.ui

import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf


class Groups {
    private var uniqueID: String = ""
    private var creatorID: String = ""
    private var groupName: String = ""
    private var sorteio: Boolean = false
    // see if user accepted the invitation or not
    //private var usersInGroup: Map<String, Boolean> = mutableMapOf()
    private var usersInGroup: ArrayList<String> =  arrayListOf()
    private var listFriends: ArrayList<String> = arrayListOf()
    private var listFriendsOf: ArrayList<String> = arrayListOf()

    constructor(
        groupName: String,
        uniqueID: String,
        sorteio: Boolean,
        creatorID: String,
        usersInGroup: ArrayList<String>,
        listFriends: ArrayList<String>,
        listFriendsOf : ArrayList<String>,

    ) {
        this.groupName = groupName
        this.uniqueID = uniqueID
        this.sorteio = sorteio
        this.creatorID = creatorID
        this.usersInGroup = usersInGroup
        this.listFriends = listFriends
        this.listFriendsOf = listFriendsOf

    }
    constructor()


    fun getGroupName(): String{
        return groupName
    }
    fun setGroupName(newGroupName: String)
    {
        this.groupName=newGroupName
    }
    fun getUniqueID(): String {
        return uniqueID
    }
    fun setUniqueID( newGroupID: String){
        this.uniqueID = newGroupID
    }

    fun getSorteio():Boolean{
        return sorteio
    }
    fun setSorteio(sorteio: Boolean){
        this.sorteio = sorteio
    }
    fun getCreatorID(): String {
        return creatorID
    }
    fun setCreatorID( newCreatorID: String){
        this.creatorID = newCreatorID
    }

    fun getUsersInGroup(): ArrayList<String> {
        return usersInGroup
    }
    fun setUsersInGroup(updatedUsersInGroup: ArrayList<String>){
        this.usersInGroup=updatedUsersInGroup
    }
    fun getListFriends():ArrayList<String> {
        return listFriends
    }
    fun setListFriends(updatedListFriends: ArrayList<String>){
        this.listFriends=updatedListFriends
    }
    fun getListFriendsOf():ArrayList<String> {
        return listFriendsOf
    }
    fun setListFriendsOf(updatedListFriendsOf: ArrayList<String>){
        this.listFriendsOf=updatedListFriendsOf
    }

}