package com.example.bogdan.feastfordriver.activity

import android.os.Bundle
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseActivity
import com.example.bogdan.feastfordriver.activity.delivery.DeliveryActivity
import com.example.bogdan.feastfordriver.activity.login.SignInActivity
import com.example.bogdan.feastfordriver.activity.login.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(DeliveryActivity.newIntent(this))
        }
        initViews()
    }

    private fun initViews() {
        Picasso.get()
            .load("https://fbcd.co/product-lg/794bcfe8d71abedb73f6b938a57376a3_resize.jpg")
            .into(ivMain)

        btnSignIn.setOnClickListener {
            startActivity(SignInActivity.newIntent(this))
        }
        btnSignUp.setOnClickListener {
            startActivity(SignUpActivity.newIntent(this))
        }
    }

}