package com.example.bogdan.feastfordriver.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseActivity

class OrderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, OrderActivity::class.java)
        }
    }
}
