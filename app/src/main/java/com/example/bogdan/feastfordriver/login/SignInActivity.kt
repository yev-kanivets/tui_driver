package com.example.bogdan.feastfordriver.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.base.BaseBackActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.content_sign_in.*

class SignInActivity : BaseBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initViews()
    }

    override fun initViews() {
        super.initViews()
        fab.setOnClickListener { loginWithEmailAndPassword() }
    }

    private fun loginWithEmailAndPassword() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        showProgress()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                hideProgress()
                if (task.isSuccessful) {
                    showToast(getString(R.string.signed_in))
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                } else {
                    showToast(task.exception.toString())
                }
            }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }

}
