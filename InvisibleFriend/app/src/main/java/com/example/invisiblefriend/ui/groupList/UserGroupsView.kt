package com.example.invisiblefriend.ui.groupList

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.UserGroupsViewBinding
import com.example.invisiblefriend.ui.Groups

class UserGroupsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    ripple: Boolean = true

): LinearLayoutCompat(ContextThemeWrapper(context, R.style.IF_Ripple), attrs, defStyleAttr) {
    private val binding = UserGroupsViewBinding.inflate(LayoutInflater.from(context), this)
    init{
        orientation = LinearLayoutCompat.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        minimumHeight = resources.getDimensionPixelSize(R.dimen.material_baseline_grid_6_5x)

    }
    fun bind(groups: Groups, listener: UserGroupsViewListener?){
        groups.run{
            binding.tvGroupName.text = getGroupName()
            if(groups.getSorteio()){
                binding.tvSorteio.isVisible=true
                binding.tvSorteio.isInvisible=false
            }
        }
        setOnClickListener{
            listener?.onGroupClick(groups)
        }

    }

    interface UserGroupsViewListener{
        fun onGroupClick(groups: Groups)
    }
}