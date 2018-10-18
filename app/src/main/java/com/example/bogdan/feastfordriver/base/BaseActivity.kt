package com.example.bogdan.feastfordriver.base

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.bogdan.feastfordriver.util.ProgressDialogFragment
import com.example.bogdan.feastfordriver.R

abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialogFragment? = null

    fun showProgress(message: String = getString(R.string.loading)) {
        if (progressDialog != null) hideProgress()
        progressDialog = ProgressDialogFragment.newInstance(message)
        progressDialog?.isCancelable = false
        progressDialog?.show(supportFragmentManager, "progressDialog")
    }

    fun hideProgress() {
        progressDialog?.dismissAllowingStateLoss()
        progressDialog = null
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //protected fun isUserLoggedIn(): Boolean = FirebaseAuth.getInstance().currentUser != null

}
