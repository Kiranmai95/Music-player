package com.example.musicplayerv5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        regBtn.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val emailTxt = findViewById<View>(R.id.emailTxt) as EditText
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        if (emailTxt.text.toString().isEmpty()) {
            emailTxt.error = "Please enter email"
            emailTxt.requestFocus()

            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt.text.toString()).matches()) {
            emailTxt.error = "Please enter valid email"
            emailTxt.requestFocus()
            return
        }

        if (passwordTxt.text.toString().isEmpty()) {
            passwordTxt.error = "Please enter password"
            passwordTxt.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                  startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {

                    Toast.makeText(baseContext, "Signup failed try after some time",
                        Toast.LENGTH_SHORT).show()

                }

                // ...
            }


    }
}
