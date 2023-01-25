package com.real.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.real.quizgame.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    lateinit var signupBinding:ActivitySignupBinding

    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.buttonSignup.setOnClickListener {

          var email = signupBinding.editTextEmailSignup.text.toString()
          var password = signupBinding.editTextPasswordSignup.text.toString()
            signupWithFirebase(email,password)
        }
    }

    fun signupWithFirebase(email:String,password:String){

        signupBinding.progressBarSignup.visibility = View.VISIBLE
        signupBinding.buttonSignup.isClickable = false

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Signup is successful", Toast.LENGTH_SHORT).show()
                signupBinding.progressBarSignup.visibility = View.INVISIBLE
                signupBinding.buttonSignup.isClickable = true
                finish()
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}