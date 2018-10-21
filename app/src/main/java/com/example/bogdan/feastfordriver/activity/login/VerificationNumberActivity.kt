package com.example.bogdan.feastfordriver.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseActivity
import com.example.bogdan.feastfordriver.util.Const
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.content_verification_number.*
import java.util.concurrent.TimeUnit

class VerificationNumberActivity : BaseActivity() {

    var storedVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_number)
        setSupportActionBar(findViewById(R.id.toolbar))

        initViews()
    }

    private fun initViews() {
        btnGetCode.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString()
            if (phoneNumber.trim().isEmpty()) {
                showToast(getString(R.string.field_can_not_be_empty))
            } else {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    1,
                    TimeUnit.SECONDS,
                    this,
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            Log.d("MeTag", "onVerificationCompleted:$credential")
                            updateData()
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            if (e is FirebaseTooManyRequestsException) {
                                showToast("SMS Quota exceeded.")
                            } else {
                                showToast(e.localizedMessage)
                            }
                        }

                        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                            Log.d("MeTag1", "code sent")
                            btnGetCode.text = getString(R.string.resend_code)
                            storedVerificationId = verificationId
                        }
                    }
                )
            }
        }
        btnVerify.setOnClickListener { verifyCode() }
    }

    private fun verifyCode() {
        if (storedVerificationId == null) {
            showToast(getString(R.string.first_get_the_code))
        } else {
            Log.d("MeTag2", "stored Verification not null")
            storedVerificationId?.let { id ->
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(id, etCode.text.toString()))
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showProgress()
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            hideProgress()
            if (task.isSuccessful) {
                Log.d("MeTag2", intent.getParcelableExtra<AuthCredential>(EXTRA_CREDENTIAL).toString())
                FirebaseAuth.getInstance().signInWithCredential(intent.getParcelableExtra(EXTRA_CREDENTIAL))
                    .addOnSuccessListener {
                        updateData()
                    }
            } else {
                showToast(getString(R.string.verification_code_entered_was_invalid))
            }
        }
    }

    private fun updateData() {
        Log.d("MeTag2", FirebaseAuth.getInstance().currentUser!!.uid)
        Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("phoneNumber", etPhoneNumber.text.toString()).addOnSuccessListener {
                startActivity(VerificationCertificateActivity.newIntent(this@VerificationNumberActivity))
                finish()
            }
    }

    companion object {
        const val EXTRA_CREDENTIAL = "credential"

        fun newIntent(context: Context, credential: AuthCredential): Intent {
            val intent = Intent(context, VerificationNumberActivity::class.java)
            intent.putExtra(EXTRA_CREDENTIAL, credential)
            return intent
        }
    }

}
