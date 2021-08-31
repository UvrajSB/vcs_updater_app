package com.vc.android.updater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.vc.android.updater.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2)
        binding.AddProductBtn.setOnClickListener{
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
        binding.ProductListBtn.setOnClickListener{
            val intent = Intent(this,ProductListActivity::class.java)
            startActivity(intent)
        }
    }
}