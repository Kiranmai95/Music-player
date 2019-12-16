package com.example.music7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

   var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        signup.setOnClickListener {

            var email = email1.text.toString()
            var password = password1.text.toString()
            createaccount(email,password)
        }
    }
//logic in crating an user account in firebase
    private fun createaccount(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    
                    Toast.makeText(applicationContext,"user loggedin successfully", Toast.LENGTH_LONG).show()
                    var intent= Intent(this,music::class.java)
                    startActivity(intent)


                } else {

                    Toast.makeText(baseContext, "Signup failed try after some time",
                        Toast.LENGTH_SHORT).show()

                }

                // ...
            }
        
    }

}

