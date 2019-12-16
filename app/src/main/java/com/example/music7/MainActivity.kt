package com.example.music7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.login
import kotlinx.android.synthetic.main.activity_register.*


class MainActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()



   // private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //auth = FirebaseAuth.getInstance()
//Login button logic
        login.setOnClickListener{

            var email = email.text.toString()
            var password = password.text.toString()
            signin(email,password)
            startActivity(Intent(this, Register::class.java))
            finish()
        }
//Register button logic
        reg.setOnClickListener(){

            var intent= Intent(this,Register::class.java)
            startActivity(intent)

        }
    }
//logic  invoking the firebase authentication
    private fun signin(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){

                Toast.makeText(applicationContext,"user logged in succesfully",Toast.LENGTH_LONG).show()
                var intent= Intent(this,music::class.java)
                startActivity(intent)
            }
        }


    }

}
