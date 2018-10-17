package com.example.bogdan.feastfordriver

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

}
