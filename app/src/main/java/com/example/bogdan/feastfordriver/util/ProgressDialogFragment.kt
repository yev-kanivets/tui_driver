package com.example.bogdan.feastfordriver.util

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.bogdan.feastfordriver.R
import kotlinx.android.synthetic.main.view_progress_dialog.*

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(activity, R.layout.view_progress_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvProgressMessage.text = arguments?.getString(EXTRA_MESSAGE)
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    companion object {

        private const val EXTRA_MESSAGE = "message"

        fun newInstance(message: String): ProgressDialogFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_MESSAGE, message)

            val fragment = ProgressDialogFragment()
            fragment.arguments = bundle

            return fragment
        }

    }

}

