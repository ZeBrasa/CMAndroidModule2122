package com.example.invisiblefriend.ui.CreateGroup

import android.util.ArrayMap
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

//@IgnoreExtraProperties

class Users{
    private var uid: String =""
    private var username: String =""
    private var email: String =""
    private var phoneNumber: String = ""
    private var inviteGroups: ArrayMap<String, Boolean>? = null
    private var shuffledGroups: ArrayMap<String,String>? = null
    private var isSelectedUser : Boolean = false

/*
data class Users(
    var uid: String ="",
    var username: String ="",
    var email: String ="",
    var phoneNumber: String = "",
    var inviteGroups: ArrayMap<String, Boolean>? = null,
    var acceptedGroups: ArrayMap<String,Boolean>? = null,
    var isSelectedUser : Boolean = false
)
*/



    constructor(
        uid: String,
        username: String,
        email: String,
        phoneNumber: String,
        inviteGroups: ArrayMap<String, Boolean>,
        shuffledGroups: ArrayMap<String, String>,
        isSelectedUser: Boolean
    ) {
        this.uid = uid
        this.username = username
        this.email = email
        this.phoneNumber = phoneNumber
        this.inviteGroups = inviteGroups
        this.shuffledGroups = shuffledGroups
        this.isSelectedUser = isSelectedUser
    }

    constructor(uid: String, username: String, email: String, phoneNumber: String, isSelectedUser: Boolean)
    constructor()

    fun getUID(): String {
        return uid
    }
    fun setUID( newUid: String){
        this.uid = newUid
    }
    fun getPhoneNumber(): String{
        return phoneNumber
    }
    fun setPhoneNumber( newPhoneNumber: String){
        this.phoneNumber = newPhoneNumber
    }
    fun getUsername(): String{
        return username
    }
    fun setUsername(newUsername: String)
    {
        this.username=newUsername
    }
    fun getEmail(): String{
        return email
    }
    fun setEmail(email:String){
        this.email=email
    }
    fun getIsSelected(): Boolean {
        return isSelectedUser
    }
    fun setIsSelected(){
        this.isSelectedUser = !isSelectedUser
    }
    /*
    fun getInvitedGroups(): ArrayMap<String,Boolean>{
        return inviteGroups
    }
    fun setInvitedGroups(updatedInviteGroups:ArrayMap<String,Boolean>){
        this.inviteGroups=updatedInviteGroups
    }
    */
    fun getShuffledGroups(): ArrayMap<String, String>? {
        return shuffledGroups
    }
    fun setAcceptedGroups(updatedShuffledGroups:ArrayMap<String,String>){
        this.shuffledGroups=updatedShuffledGroups
    }

}

