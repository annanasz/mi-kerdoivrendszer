package com.bme.surveysystemsupportedbyai.core

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText

class Utils {
    companion object {

        fun showMessage(
            context: Context,
            message: String?
        ) = makeText(context, message, LENGTH_LONG).show()
    }
}