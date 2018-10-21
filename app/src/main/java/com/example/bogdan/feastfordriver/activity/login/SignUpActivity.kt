package com.example.bogdan.feastfordriver.activity.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseBackActivity
import com.example.bogdan.feastfordriver.entity.Driver
import com.example.bogdan.feastfordriver.util.Const
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_sign_up.*
import java.util.*

class SignUpActivity : BaseBackActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initViews()
    }

    override fun initViews() {
        super.initViews()
        btnContinue.setOnClickListener { signUp() }
    }

    private fun signUp() {
        val fullName = etFullName.text.toString()

        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        if (email.trim().isEmpty() || email.trim().isEmpty() || fullName.trim().isEmpty()) {
            showToast(getString(R.string.field_can_not_be_empty))
        } else {

            showProgress()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { signUpTask ->
                    hideProgress()
                    if (signUpTask.isSuccessful) {
                        val userId = signUpTask.result.user.uid
                        val driverRef = Const.DRIVERS_REF.document(userId)

                        showProgress()
                        driverRef.set(
                            Driver(
                                id = userId,
                                email = email,
                                fullName = fullName,
                                dateOfAdd = Calendar.getInstance().timeInMillis
                            )
                        )
                            .addOnCompleteListener { task ->
                                hideProgress()
                                if (task.isSuccessful) {
                                    startActivity(
                                        VerificationNumberActivity.newIntent(
                                            this,
                                            EmailAuthProvider.getCredential(email, password)
                                        )
                                    )
                                    finish()
                                } else {
                                    showToast(task.exception.toString())
                                }
                            }
                    } else {
                        showToast(signUpTask.exception.toString())
                    }
                }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

}
