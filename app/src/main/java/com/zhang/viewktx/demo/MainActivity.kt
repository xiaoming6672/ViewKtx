package com.zhang.viewktx.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zhang.lib.ktx.widget.setOnViewClickListener
import com.zhang.viewktx.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v , insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left , systemBars.top , systemBars.right , systemBars.bottom)
            insets
        }

        val list = listOf("哈哈哈" , "奥斯u月份的个" , "哦股轻微都会" , "去哦i我饿退还给预备vi" , getText(R.string.app_name))

        binding.tvStroke.setOnViewClickListener {
            it.text = list.shuffled()[0]
        }
    }
}