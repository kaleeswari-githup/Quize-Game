package com.real.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.real.quizgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding:ActivityMainBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.buttonStartQuiz.setOnClickListener {
            val intent = Intent(this@MainActivity,QuizActivity::class.java)
            startActivity(intent)
        }

        mainBinding.buttonSignout.setOnClickListener {
            auth.signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSigninClient = GoogleSignIn.getClient(this,gso)
            googleSigninClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Signout is successful", Toast.LENGTH_SHORT).show()
                }

            }
            val intent = Intent(this@MainActivity,LoginPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}