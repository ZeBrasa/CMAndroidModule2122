package com.example.invisiblefriend.ui.DetailsGroup

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.DetailsGroupViewBinding
import com.example.invisiblefriend.databinding.UserListGroupViewBinding
import com.example.invisiblefriend.ui.CreateGroup.Users
import com.example.invisiblefriend.ui.UserAdapter

class DetailsGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    ripple: Boolean = true
) : LinearLayoutCompat(if (ripple) ContextThemeWrapper(context, R.style.IF_Ripple) else context, attrs, defStyleAttr) {

    private val binding = DetailsGroupViewBinding.inflate(LayoutInflater.from(context), this)
    private var usersList: ArrayList<Users>? = null
    private var listToRecycler: ArrayList<Users>? = null
    private var userAdapter: UserAdapter? =null


    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        minimumHeight = resources.getDimensionPixelSize(R.dimen.material_baseline_grid_6_5x)
    }

    fun bind(users: Users, listener: FilterViewListener?) {
        users.run {

            binding.tvUserName.text = users.getUsername()
            binding.tvUserName.isVisible = true

        }

    }


    interface FilterViewListener {
        fun onUserClick(users: Users)
    }

}