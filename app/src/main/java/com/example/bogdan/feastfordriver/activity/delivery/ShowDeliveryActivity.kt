package com.example.bogdan.feastfordriver.activity.delivery

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseBackActivity
import com.example.bogdan.feastfordriver.entity.Delivery
import com.example.bogdan.feastfordriver.entity.Order
import com.example.bogdan.feastfordriver.util.Const
import kotlinx.android.synthetic.main.content_show_delivery.*
import java.util.*

class ShowDeliveryActivity : BaseBackActivity() {

    lateinit var delivery: Delivery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_delivery)
        delivery = intent.getParcelableExtra(EXTRA_CREDENTIAL)

        initViews()
    }

    override fun initViews() {
        super.initViews()
        if (delivery.realDeliveryTime == 0L) {
            btnDone.visibility = View.GONE
        }
        showProgress()
        Const.ORDERS_REF.document(delivery.orderId).get().addOnSuccessListener { task ->
            Const.RESTAURANTS_REF.document(delivery.restaurantId).get().addOnSuccessListener { doc ->
                hideProgress()
                val order = task.toObject(Order::class.java)
                tvUserName.text = getString(R.string.client, order.userName)
                tvAddressOrder.text = order.address
                tvPhoneNumber.text = order.phoneNumber
                tvAddressRestaurant.text = doc["address"].toString()
                tvRestaurantPhoneNumber.text =
                        getString(R.string.restaurant_phone_number, doc["phoneNumber"].toString())
            }
        }
        btnDone.setOnClickListener { _ ->
            showSwitchDialog()
        }
    }

    private fun showSwitchDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.are_you_sure)
        alertDialog.setMessage(R.string.confirm)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->
            Const.DELIVERIES_REF.document(delivery.id).update("realDeliveryTime", Calendar.getInstance().timeInMillis)
                .addOnSuccessListener { _ ->
                    Const.ORDERS_REF.document(delivery.orderId).update("status", "delivered").addOnSuccessListener {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
        }
        alertDialog.setNegativeButton(R.string.no) { _, _ -> }
        alertDialog.create()
        alertDialog.show()
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