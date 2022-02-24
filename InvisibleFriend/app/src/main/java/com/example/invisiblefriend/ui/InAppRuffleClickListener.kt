package com.example.invisiblefriend.ui

import android.util.Log
import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener
import com.google.firebase.inappmessaging.model.Action
import com.google.firebase.inappmessaging.model.InAppMessage

class InAppRuffleClickListener : FirebaseInAppMessagingClickListener {
    override fun messageClicked(inAppMessage: InAppMessage, action: Action) {
        // Determine which URL the user clicked
        val url = action.actionUrl
        // Get general information about the campaign
        val metadata = inAppMessage.campaignMetadata
        Log.d("AppTest", "URL is: " + url)
        Log.d("AppTest", "Meta Data: " + metadata)
    }
}