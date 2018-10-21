package com.example.bogdan.feastfordriver.activity.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.activity.delivery.DeliveryActivity
import com.example.bogdan.feastfordriver.activity.base.BaseActivity
import com.example.bogdan.feastfordriver.util.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.content_verification_certificate.*
import java.io.ByteArrayOutputStream

class VerificationCertificateActivity : BaseActivity() {

    private var photo: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_certificate)
        setSupportActionBar(findViewById(R.id.toolbar))

        initViews()
    }

    private fun initViews() {
        btnSelectPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE)
        }
        btnSignUp.setOnClickListener {
            showProgress()
            val photoRef = FirebaseStorage.getInstance()
                .getReference("certificate-images/" + FirebaseAuth.getInstance().currentUser?.uid)
            val byteStream = ByteArrayOutputStream()
            photo?.compress(Bitmap.CompressFormat.JPEG, 100, byteStream)
            photoRef.putBytes(byteStream.toByteArray()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Const.DRIVERS_REF.document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .update(
                            "capacityOfCar", etCapacity.text.toString().toLong(),
                            "certificateUrl", task.result.downloadUrl.toString()
                        ).addOnSuccessListener { _ ->
                            hideProgress()
                            showToast(getString(R.string.user_signed_up_successfully))
                            startActivity(DeliveryActivity.newIntent(this))
                            finish()
                        }
                } else {
                    hideProgress()
                    showToast(task.exception.toString())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    photo = BitmapFactory.decodeStream(contentResolver.openInputStream(data?.data))
                    ivCertificate.setImageBitmap(photo)
                }
            }
        }

    }

    companion object {
        const val REQUEST_PICK_IMAGE = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, VerificationCertificateActivity::class.java)
        }
    }
}