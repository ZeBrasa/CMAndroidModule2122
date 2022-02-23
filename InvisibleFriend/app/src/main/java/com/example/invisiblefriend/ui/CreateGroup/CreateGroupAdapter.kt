package com.example.invisiblefriend.ui.CreateGroup

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CreateGroupAdapter(
    listener: CreateGroupView.FilterViewListener,
    var users: MutableList<Users> = mutableListOf()
) : RecyclerView.Adapter<CreateGroupAdapter.GroupViewHolder>(){

    private var listener = listener

    inner class GroupViewHolder(val view: CreateGroupView) : RecyclerView.ViewHolder(view){
        fun bind(
            users: Users,
            listener: CreateGroupView.FilterViewListener
        ) {
            (itemView as CreateGroupView).run {
                bind(users, listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size//usersList!!.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):GroupViewHolder {
        return GroupViewHolder(CreateGroupView(parent.context))
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        /*
        when(holder){
            is GroupViewHolder ->{
                listToRecycler = retrieveAllUsers()
                holder.bind(listToRecycler!!.get(position))
        }
        }*/
        holder.bind(getItem(position)!!, listener)
        //(holder as ViewHolder).bind(usersList[position])

        //(holder as ViewHolder).bindUser
    }
    fun getItem(index: Int):Users?{
        return if (users.isNotEmpty() && index >= 0 && index <= getItemCount()) this.users[index] else null
    }

    open fun setItems(users: MutableList<Users>){
        //clear(false)
        addItems(users)
    }
    fun clear(notify: Boolean = true) {
        users.clear()
        if (notify) notifyDataSetChanged()
    }
    open fun addItems(users: List<Users>, notify: Boolean = true) {
        if (users.isNotEmpty())
            this.users = users as MutableList<Users>
            //this.users.addAll(users)
        if (notify) notifyDataSetChanged()
    }

    /*
    class GroupViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        val tvUserName: TextView = itemView.
        fun bind(usersList: Users){
            tvUserName.setText(usersList.getUsername())
        }
    }
    */


}
