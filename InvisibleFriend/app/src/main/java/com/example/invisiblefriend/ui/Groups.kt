package com.example.invisiblefriend.ui

import android.util.ArrayMap

class Groups {
    private var uniqueID: String = ""
    private var creatorID: String = ""
    private var groupName: String = ""
    private var sorteio: Boolean = false
    // see if user accepted the invitation or not
    //private var usersInGroup: Map<String, Boolean> = mutableMapOf()
    private var usersInGroup: ArrayList<String> =  arrayListOf()
    private var listShuffle: ArrayList<String> = arrayListOf()


    constructor(
        groupName: String,
        uniqueID: String,
        sorteio: Boolean,
        creatorID: String,
        usersInGroup: ArrayList<String>,
        listShuffle: ArrayList<String>,

    ) {
        this.groupName = groupName
        this.uniqueID = uniqueID
        this.sorteio = sorteio
        this.creatorID = creatorID
        this.usersInGroup = usersInGroup
        this.listShuffle = listShuffle

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
    fun getListShuffle(): ArrayList<String> {
        return listShuffle
    }
    fun setListShuffle(updatedListShuffle: ArrayList<String>){
        this.listShuffle=updatedListShuffle
    }

}