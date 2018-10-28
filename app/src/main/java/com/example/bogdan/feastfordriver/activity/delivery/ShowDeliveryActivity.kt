package com.example.bogdan.feastfordriver.activity.delivery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseBackActivity
import com.example.bogdan.feastfordriver.entity.Delivery
import com.example.bogdan.feastfordriver.util.Const
import kotlinx.android.synthetic.main.content_show_delivery.*
import java.util.*

class ShowDeliveryActivity : BaseBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_delivery)

        initViews()
    }

    override fun initViews() {
        super.initViews()
        val delivery = intent.getParcelableExtra<Delivery>(EXTRA_CREDENTIAL)
        btnDone.setOnClickListener {
            Const.DELIVERIES_REF.document(delivery.id).update("realDeliveryTime", Calendar.getInstance().timeInMillis)
                .addOnSuccessListener {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
        }
    }

    companion object {
        private const val EXTRA_CREDENTIAL = "extra_credential"

        fun newIntent(context: Context, delivery: Delivery): Intent {
            val intent = Intent(context, ShowDeliveryActivity::class.java)
            intent.putExtra(ShowDeliveryActivity.EXTRA_CREDENTIAL, delivery)
            return intent
        }
    }

}