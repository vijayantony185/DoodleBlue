package com.example.doodleblue.Receiver

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.doodleblue.Model.SaveContact
import com.example.doodleblue.R

class CallReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                super.onCallStateChanged(state, incomingNumber)
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        var model = SaveContact.getCurrentContact(context)
                        if (model != null) {
                            model.forEach {
                                if (it.number.equals(incomingNumber)) {
                                    Toast.makeText(context, "saved number", Toast.LENGTH_SHORT)
                                        .show()

                                    val builder: AlertDialog.Builder =
                                        AlertDialog.Builder(context.applicationContext)
                                    val inflater = LayoutInflater.from(context)
                                    val dialogView: View =
                                        inflater.inflate(R.layout.alert_contact, null)

                                    var tv_incoming_name =
                                        dialogView.findViewById<TextView>(R.id.tv_incoming_name)
                                    var tv_incoming_number =
                                        dialogView.findViewById<TextView>(R.id.tv_incoming_number)
                                    var img_close =
                                        dialogView.findViewById<ImageView>(R.id.img_close)

                                    tv_incoming_name.setText(it.name.toString())
                                    tv_incoming_number.setText(it.number.toString())

                                    builder.setView(dialogView)
                                    val alert: AlertDialog = builder.create()

                                    img_close.setOnClickListener {
                                        alert.cancel()
                                    }

                                    alert.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        alert.getWindow()!!
                                            .setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                                    } else {
                                        alert.getWindow()!!
                                            .setType(WindowManager.LayoutParams.TYPE_PHONE)
                                    }

                                    alert.setCanceledOnTouchOutside(false)
                                    alert.show()
                                    val lp = WindowManager.LayoutParams()
                                    val window: Window = alert.getWindow()!!
                                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                                    window.setGravity(Gravity.CENTER)
                                    lp.copyFrom(window.getAttributes())
                                    lp.copyFrom(window.getAttributes())
                                    //This makes the dialog take up the full width
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                                    window.setAttributes(lp)
                                }
                            }
                        }
                    }
                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }
}