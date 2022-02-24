package com.example.invisiblefriend.ui.CreateGroup

import android.util.ArrayMap

class Markers {
    private var friendID: String = ""
    private var friendOf: String = ""
    private var longitude: String = ""
    private var latitude: String = ""
    private var groupName: String = ""
    private var message: String = ""
    private var markerID: String = ""


    constructor(
        friendID: String,
        friendOf: String,
        longitude: String,
        latitude: String,
        message: String,
        groupName: String,

        ) {
        this.friendID = friendID
        this.friendOf = friendOf
        this.longitude = longitude
        this.latitude = latitude
        this.groupName = groupName
        this.message = message

    }
    //constructor(uid: String, username: String, email: String, phoneNumber: String, isSelectedUser: Boolean)
    constructor()
    fun getFriendID(): String {
        return friendID
    }

    fun setFriendID(newFriendID: String) {
        this.friendID = newFriendID
    }

    fun getFriendOf(): String {
        return friendID
    }

    fun setFriendOf(newFriendID: String) {
        this.friendID = newFriendID
    }

    fun getLongitude(): String {
        return friendID
    }

    fun setLongitude(newFriendID: String) {
        this.friendID = newFriendID
    }

    fun getLatitude(): String {
        return friendID
    }

    fun setLatitude(newFriendID: String) {
        this.friendID = newFriendID
    }

    fun getMessage(): String {
        return message
    }
    fun settMessage(newMessage: String) {
        this.message = newMessage
    }
}



