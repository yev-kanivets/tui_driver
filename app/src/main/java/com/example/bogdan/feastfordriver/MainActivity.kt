package com.example.bogdan.feastfordriver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        Picasso.get()
            .load("https://fbcd.co/product-lg/794bcfe8d71abedb73f6b938a57376a3_resize.jpg")
            .into(ivMain)

        btnSignIn.setOnClickListener {
            startActivityForResult(SignInActivity.newIntent(this), REQUEST_SIGN_IN)
        }
        btnSignUp.setOnClickListener {
            startActivityForResult(SignUpActivity.newIntent(this), REQUEST_SIGN_UP)
        }
    }

    companion object {
        const val REQUEST_SIGN_IN = 1
        const val REQUEST_SIGN_UP = 2
    }

}