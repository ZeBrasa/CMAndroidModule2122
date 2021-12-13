package com.example.dialler

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dialler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Declare  views
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when(p0?.id){
            R.id.button -> {
                var number = binding.dialNumber.getText().toString().trim()
                var phoneDialer:Intent  =  Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
                startActivity(phoneDialer)
            }

        }

    }
}