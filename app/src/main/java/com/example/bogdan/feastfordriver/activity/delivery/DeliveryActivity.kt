package com.example.bogdan.feastfordriver.activity.delivery

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.MainActivity
import com.example.bogdan.feastfordriver.activity.base.BaseActivity
import com.example.bogdan.feastfordriver.activity.delivery.adapter.DeliveryAdapter
import com.example.bogdan.feastfordriver.entity.Delivery
import com.example.bogdan.feastfordriver.entity.Driver
import com.example.bogdan.feastfordriver.gps.DistanceTrackerService
import com.example.bogdan.feastfordriver.util.Const
import com.google.android.gms.location.places.ui.PlacePicker
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
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(MainActivity.newIntent(this))
            finish()
        }

        initViews()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_log_out -> {
                showProgress()
                Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .update("online", false).addOnSuccessListener {
                        hideProgress()
                        stopTracking()
                        FirebaseAuth.getInstance().signOut()
                        finish()
                    }
                return true
            }
            R.id.action_send_location -> {
                pickAddress()
            }
        }
        return false
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PLACE_PICKER -> {
                    val place = PlacePicker.getPlace(this, data)
                    Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .update("gps", place.latLng.latitude.toString() + ", " + place.latLng.longitude.toString())
                }
            }
        }
    }

    private fun showSwitchDialog(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->
            Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid)
                .update("online", !swOnline.isChecked).addOnSuccessListener {
                    if (swOnline.isChecked) {
                        navigationView.menu.findItem(R.id.action_send_location).isVisible = false
                        stopTracking()
                        adapter.setItems(listOf())
                    } else {
                        navigationView.menu.findItem(R.id.action_send_location).isVisible = true
                        updateRecycler()
                        checkPermissions()
                    }
                    swOnline.isChecked = !swOnline.isChecked
                }
        }
        alertDialog.setNegativeButton(R.string.no) { _, _ -> }
        alertDialog.create()
        alertDialog.show()
    }

    private fun updateRecycler() {
        showProgress()
        val driverId = FirebaseAuth.getInstance().currentUser?.uid
        Const.DELIVERIES_REF
            .whereEqualTo("driverId", driverId)
            .addSnapshotListener { value, _ ->
                hideProgress()
                val deliveries = mutableListOf<Delivery>()
                value?.documents?.forEach { doc ->
                    val delivery = doc.toObject(Delivery::class.java)
                    if (delivery.realDeliveryTime < 2) {
                        deliveries.add(delivery)
                    }
                }
                adapter.setItems(deliveries)
            }
    }

    private fun initViews() {
        FirebaseAuth.getInstance().currentUser?.let { driver ->
            Const.DRIVERS_REF.document(driver.uid).get().addOnSuccessListener { task ->
                val firestoreDriver = task.toObject(Driver::class.java)
                swOnline.isChecked = firestoreDriver.online
                navigationView.menu.findItem(R.id.action_send_location).isVisible = swOnline.isChecked
                if (swOnline.isChecked) {
                    updateRecycler()
                }
            }
            Const.DRIVERS_REF.document(driver.uid).addSnapshotListener { value, _ ->
                value?.let { doc ->
                    if (doc.exists()) {
                        val curDriver = doc.toObject(Driver::class.java)
                        if (curDriver.check) {
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

    private fun checkPermissions() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.use_coordinate_tracking))
        alertDialog.setMessage(getString(R.string.confirm_tracking))
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                startTracking()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSIONS
                )
            }
        }
        alertDialog.setNegativeButton(R.string.no) { _, _ -> }
        alertDialog.create()
        alertDialog.show()
    }

    private fun pickAddress() {
        val builder = PlacePicker.IntentBuilder()
        startActivityForResult(builder.build(this), REQUEST_PLACE_PICKER)
    }

    private fun startTracking() {
        val intent = Intent(this, DistanceTrackerService::class.java)
        intent.action = DistanceTrackerService.ACTION_START_TRACKING
        startService(intent)
    }

    private fun stopTracking() {
        val intent = Intent(this, DistanceTrackerService::class.java)
        intent.action = DistanceTrackerService.ACTION_STOP_TRACKING
        startService(intent)
    }

    companion object {
        const val REQUEST_SHOW_DELIVERY = 1
        const val REQUEST_LOCATION_PERMISSIONS = 2
        const val REQUEST_PLACE_PICKER = 3

        fun newIntent(context: Context): Intent {
            return Intent(context, DeliveryActivity::class.java)
        }
    }
}
