package com.example.invisiblefriend.ui.CreateGroup

import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.example.invisiblefriend.ui.DetailsGroup.DetailsGroupView

class DetailsGroupAdapter(
    listener: DetailsGroupView.FilterViewListener,
    users: MutableList<Users> = mutableListOf(),
) : RecyclerView.Adapter<DetailsGroupAdapter.DetailsGroupViewHolder>(){

    private var listener = listener
    private var users: MutableList<Users> = users

    inner class DetailsGroupViewHolder(val view: DetailsGroupView) : RecyclerView.ViewHolder(view){
        fun bind(
            users: Users,
            listener: DetailsGroupView.FilterViewListener
        ) {
            (itemView as DetailsGroupView).run {
                bind(users, listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):DetailsGroupViewHolder {
        return DetailsGroupViewHolder(DetailsGroupView(parent.context))
    }

    override fun onBindViewHolder(holder: DetailsGroupViewHolder, position: Int) {
        holder.bind(getItem(position)!!, listener)
    }

    fun getItem(index: Int): Users?{
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
}
