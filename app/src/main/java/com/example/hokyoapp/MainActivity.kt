package com.example.hokyoapp

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import com.example.hokyoapp.Constants.CALL_US_NUMBER
import com.example.hokyoapp.Constants.CUSTOM_TAB_PACKAGE_NAME
import com.example.hokyoapp.Constants.HOKYO_PACKAGE_NAME
import com.example.hokyoapp.Constants.HOKYO_URL
import com.example.hokyoapp.Constants.TVFDM_URL
import com.example.hokyoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mCustomTabsServiceConnection: CustomTabsServiceConnection
    private var mClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, customTabsClient: CustomTabsClient) {
                mClient = customTabsClient
                mClient?.warmup(0L)
                mCustomTabsSession = mClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mClient = null
            }
        }

        CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection)
    }

    override fun onResume() {
        super.onResume()

        // after clicking the hokyo button
        // hokyo website open
        binding.btnHokyo.setOnClickListener {
            loadCustomTabForSite(HOKYO_URL)
        }

        // after clicking the TVFDM button
        // TVFDM website open
        binding.btnTvfdm.setOnClickListener {
            loadCustomTabForSite(TVFDM_URL)
        }

        // after clicking the call us button
        // dialer open with the number
        binding.btnCallUs.setOnClickListener {
            val phoneNumber = Uri.parse("tel:$CALL_US_NUMBER")

            // Create the intent and set the data for the
            // intent as the phone number.
            val i = Intent(Intent.ACTION_DIAL, phoneNumber)

            try {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                startActivity(i)
            } catch (s: SecurityException) {
                // show() method display the toast with
                // exception message.
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnContactUs.setOnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        }

        // On clicking the button it will automatically open the HOKYO App
        // if it's already there in the user's phone or else it will redirect to Plays-tore and download the HOKYO App
        binding.btnDownloadHokyo.setOnClickListener {
            startNewActivity()
        }
    }

    private fun loadCustomTabForSite(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }


    private fun startNewActivity() {
        var intent = packageManager.getLaunchIntentForPackage(HOKYO_PACKAGE_NAME)
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            // Bring user to the market or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=$HOKYO_PACKAGE_NAME")
            startActivity(intent)
        }
    }
}