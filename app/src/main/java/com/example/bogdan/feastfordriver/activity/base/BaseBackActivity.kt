package com.example.bogdan.feastfordriver.activity.base

import android.view.MenuItem
import com.example.bogdan.feastfordriver.R

abstract class BaseBackActivity : BaseActivity() {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected open fun initViews() {
        initToolbar()
    }

    protected open fun initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}