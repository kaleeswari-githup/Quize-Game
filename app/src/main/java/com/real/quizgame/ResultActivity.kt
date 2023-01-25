package com.real.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.real.quizgame.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding:ActivityResultBinding

    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    val retriveReference:DatabaseReference = database.reference.child("Scores")



    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    var users = auth.currentUser

    var correctAns = ""
    var wrongAns = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        val view= resultBinding.root
        setContentView(view)
        retriveReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users?.let {
                    val userID = it.uid
                    correctAns = snapshot.child(userID).child("Correct").value.toString()
                    wrongAns = snapshot.child(userID).child("Wrong").value.toString()

                    resultBinding.textViewScoreCorrect.text = correctAns
                    resultBinding.textViewScoreWrong.text = wrongAns
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        resultBinding.buttonPlayAgaing.setOnClickListener {
            val intent = Intent(this@ResultActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        resultBinding.buttonExit.setOnClickListener {
            finish()
        }
    }
}