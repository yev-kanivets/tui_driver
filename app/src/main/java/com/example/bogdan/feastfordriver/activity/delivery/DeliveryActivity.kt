package com.example.bogdan.feastfordriver.activity.delivery

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.base.BaseActivity
import com.example.bogdan.feastfordriver.activity.delivery.adapter.DeliveryAdapter
import com.example.bogdan.feastfordriver.entity.Delivery
import com.example.bogdan.feastfordriver.entity.Driver
import com.example.bogdan.feastfordriver.util.Const
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.content_order.*

class DeliveryActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var adapter: DeliveryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        initViews()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_log_out -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                return true
            }
        }
        return false
    }

    private fun showSwitchDialog(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->
            Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid)
                .update("online", !swOnline.isChecked).addOnSuccessListener {
                    swOnline.isChecked = !swOnline.isChecked
                }
        }
        alertDialog.setNegativeButton(R.string.no) { _, _ -> }
        alertDialog.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid).addSnapshotListener { value, _ ->
            value?.let { doc ->
                if (doc.exists()) {
                    val driver = doc.toObject(Driver::class.java)
                    if (driver.check) {
                        swOnline.visibility = View.VISIBLE
                        tvWait.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        swOnline.visibility = View.INVISIBLE
                        tvWait.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
                }
            }
        }
        adapter = DeliveryAdapter(listOf()) { delivery -> showDelivery(delivery) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mDrawerLayout = findViewById(R.id.drawerLayout)
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener(this)
        swOnline.setOnClickListener {
            swOnline.isChecked = !swOnline.isChecked
            if (!swOnline.isChecked) {
                showSwitchDialog(
                    getString(R.string.start_deliver_orders),
                    getString(R.string.message_online)
                )
            } else {
                showSwitchDialog(
                    getString(R.string.finish_deliver_orders),
                    getString(R.string.message_offline)
                )
            }
        }
    }

    private fun showDelivery(delivery: Delivery) {
        startActivityForResult(ShowDeliveryActivity.newIntent(this, delivery), REQUEST_SHOW_DELIVERY)
    }

    companion object {
        const val REQUEST_SHOW_DELIVERY = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, DeliveryActivity::class.java)
        }
    }
}
