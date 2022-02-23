package com.example.invisiblefriend.ui.CreateGroup

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.UserListGroupViewBinding
import com.example.invisiblefriend.ui.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    ripple: Boolean = true
) : LinearLayoutCompat(if (ripple) ContextThemeWrapper(context, R.style.IF_Ripple) else context, attrs, defStyleAttr) {

    private val binding = UserListGroupViewBinding.inflate(LayoutInflater.from(context), this)
    private var usersList: ArrayList<Users>? = null
    private var listToRecycler: ArrayList<Users>? = null
    private var userAdapter: UserAdapter? =null


    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        minimumHeight = resources.getDimensionPixelSize(R.dimen.material_baseline_grid_8_5x)
    }

    fun bind(users: Users, listener: FilterViewListener?) {
        users.run {
            binding.tvName.text = getUsername()
            binding.cbSelect.isChecked = getIsSelected()
        }

        binding.cbSelect.setOnClickListener {
            binding.cbSelect.isChecked = !users.getIsSelected()
            listener?.onUserClick(users)
        }
    }

    interface FilterViewListener {
        fun onUserClick(users: Users)
    }

}