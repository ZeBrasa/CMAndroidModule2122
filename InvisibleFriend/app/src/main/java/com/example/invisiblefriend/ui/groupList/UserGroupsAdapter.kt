package com.example.invisiblefriend.ui.groupList

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.invisiblefriend.ui.CreateGroup.CreateGroupView
import com.example.invisiblefriend.ui.CreateGroup.Users
import com.example.invisiblefriend.ui.Groups
import java.security.acl.Group

class UserGroupsAdapter(
    listener: UserGroupsView.UserGroupsViewListener,
    groups: MutableList<Groups> = mutableListOf()
): RecyclerView.Adapter<UserGroupsAdapter.UserGroupsViewHolder>() {

    private var listener = listener
    private var groups: MutableList<Groups> = groups

    inner class UserGroupsViewHolder(val view: UserGroupsView): RecyclerView.ViewHolder(view){
        fun bind(
            groups: Groups,
            listener: UserGroupsView.UserGroupsViewListener
        ) {
            (itemView as UserGroupsView).run {
                bind(groups, listener)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserGroupsAdapter.UserGroupsViewHolder {
        return UserGroupsViewHolder(UserGroupsView(parent.context))
    }

    override fun onBindViewHolder(holder: UserGroupsViewHolder, position: Int) {
        holder.bind(getItem(position)!!, listener)
    }

    override fun getItemCount(): Int {
        return groups.size
    }
    fun getItem(index: Int): Groups?{
        return if (groups.isNotEmpty() && index >= 0 && index <= getItemCount()) this.groups[index] else null
    }

    open fun setItems(groups: MutableList<Groups>){
        //clear(false)
        addItems(groups)
    }
    open fun addItems(groups: List<Groups>, notify: Boolean = true) {
        if (groups.isNotEmpty())
            this.groups = groups as MutableList<Groups>
        //this.users.addAll(users)
        if (notify) notifyDataSetChanged()
    }
}