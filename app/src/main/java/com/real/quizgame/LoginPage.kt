package com.real.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.real.quizgame.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {

    lateinit var loginBinding:ActivityLoginPageBinding

    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var googleSigninClient: GoogleSignInClient

    lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginPageBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        registerActivityForGoogleSignin()

        var textOfGoogleButton = loginBinding.buttonGoogleSignin.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 18F

        loginBinding.buttonSignin.setOnClickListener {

           val signinEmail = loginBinding.editTextEmailSignin.text.toString()
            val signinPassword = loginBinding.editTextPasswordSignin.text.toString()

            signinWithFirebase(signinEmail,signinPassword)
        }

        loginBinding.buttonGoogleSignin.setOnClickListener {

            signinGoogle()

        }

        loginBinding.signupText.setOnClickListener {
          val intent = Intent(this@LoginPage,SignupActivity::class.java)
            startActivity(intent)

        }

        loginBinding.forgotPasswordText.setOnClickListener {

               val intent = Intent(this@LoginPage,ForgetPasswordActivity::class.java)
               startActivity(intent)
        }
    }

    fun signinWithFirebase(signinEmail:String,signinPassword:String){
        auth.signInWithEmailAndPassword(signinEmail,signinPassword).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginPage,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onStart() {
        var user = auth.currentUser
        if(user !=null){
            Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginPage,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }

    private fun signinGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("831509827262-cmp506c1idmcu4abh55v75tnuersassm.apps.googleusercontent.com")
            .requestEmail().build()

        googleSigninClient = GoogleSignIn.getClient(this,gso)
        signin()
    }

    private fun signin(){
        val signinIntent:Intent = googleSigninClient.signInIntent
        activityResultLauncher.launch(signinIntent)
    }

    private fun registerActivityForGoogleSignin(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK && data != null){
                val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                firebaseSigninWithGoogle(task)
            }
        })
    }

    private fun firebaseSigninWithGoogle(task:Task<GoogleSignInAccount>){
        try {
            val account:GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome Quiz game", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch (e : ApiException){
            Toast.makeText(applicationContext, e . localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount){
        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if(task.isSuccessful){

            }else{

            }

        }
    }

}