package com.example.hokyoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hokyoapp.databinding.ActivityContactUsBinding


class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        // Get input text
        val email = "info@tvfdm.in"

        binding.sendEmailBtn.setOnClickListener {

            // if the subject is empty then show a message
            if (binding.subjectTextField.editText?.text.toString().isEmpty()) {
                Toast.makeText(this, "Please add Subject", Toast.LENGTH_SHORT).show()

                // if the message is empty then show a message
            } else if (binding.messageTextField.editText?.text.toString().isEmpty()) {
                Toast.makeText(this, "Please add some Message", Toast.LENGTH_SHORT).show()

                // otherwise email to info@tvfdm.in with the subject and message
            } else {
                val mail = "mailto:" + email +
                        "?&subject=" + Uri.encode(binding.subjectTextField.editText?.text.toString()) +
                        "&body=" + Uri.encode(binding.messageTextField.editText?.text.toString())
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(mail)
                try {
                    startActivity(Intent.createChooser(intent, "Send Email.."))
                } catch (e: Exception) {
                    Toast.makeText(this, "Exception: " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}