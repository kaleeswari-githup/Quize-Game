package com.real.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.real.quizgame.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var forgetPasswordBinding:ActivityForgetPasswordBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgetPasswordBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = forgetPasswordBinding.root
        setContentView(view)

        forgetPasswordBinding.buttonSendSMS.setOnClickListener {
            var forgetPasswordEmail = forgetPasswordBinding.editTextEmailForgotPassword.text.toString()

            auth.sendPasswordResetEmail(forgetPasswordEmail).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext,
                        "Reset password Message sended", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}