package com.example.bogdan.feastfordriver.activity.delivery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseBackActivity
import com.example.bogdan.feastfordriver.activity.login.VerificationNumberActivity
import com.example.bogdan.feastfordriver.entity.Delivery

class ShowDeliveryActivity : BaseBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_delivery)

        initViews()
    }

    companion object {

        fun newIntent(context: Context, delivery: Delivery): Intent {
            val intent = Intent(context, VerificationNumberActivity::class.java)
            intent.putExtra(VerificationNumberActivity.EXTRA_CREDENTIAL, delivery)
            return intent
        }
    }
}
