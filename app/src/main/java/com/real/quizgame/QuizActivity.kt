package com.real.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.real.quizgame.databinding.ActivityQuizBinding
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding : ActivityQuizBinding

    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference:DatabaseReference = database.reference.child("Quiestions")

    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    var users = auth.currentUser

    val scoreRef = database.reference

    var questions = HashSet<Int>()
    var quiestion = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionsCount = 0
    var questionNumber = 0
    var userAnswer = ""

    var userCorrect = 0
    var userWrong = 0

    lateinit var timer:CountDownTimer
    private var totalTime = 25000L
    var timerContinue = false
    var leftTime = totalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        do {
          val number =  Random.nextInt(1,11)
            Log.d("number",number.toString())
            questions.add(number)
        }while (questions.size < 5)

        Log.d("number of question",questions.toString())

        gameLogic()

        quizBinding.buttonFinish.setOnClickListener {
            sendScore()
        }

        quizBinding.buttonNext.setOnClickListener {
            resetTimer()
            gameLogic()
        }

        quizBinding.textViewA.setOnClickListener {
            userAnswer = "a"
            pauseTimer()
           if(userAnswer == correctAnswer){
               quizBinding.textViewA.setBackgroundColor(Color.GREEN)
               userCorrect++
               quizBinding.textviewCorrect.text= userCorrect.toString()
           }else{
               quizBinding.textViewA.setBackgroundColor(Color.RED)
               userWrong++
               quizBinding.textViewWrong.text = userWrong.toString()
               findAnswer()
           }
            disableClickable()
        }
        quizBinding.textViewB.setOnClickListener {
            userAnswer ="b"
            pauseTimer()
            if(userAnswer == correctAnswer){
                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textviewCorrect.text= userCorrect.toString()
            }else{
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }

        quizBinding.textViewC.setOnClickListener {
            userAnswer = "c"
            pauseTimer()
            if(userAnswer == correctAnswer){
                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textviewCorrect.text= userCorrect.toString()
            }else{
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }

        quizBinding.textViewD.setOnClickListener {
           userAnswer = "d"
            pauseTimer()
            if(userAnswer == correctAnswer){
                quizBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textviewCorrect.text= userCorrect.toString()
            }else{
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
    }

    fun gameLogic(){
        startTimer()
        restoreOptions()
        myReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionsCount = snapshot.childrenCount.toInt()

                if(questionNumber < questions.size){
                    quiestion = snapshot.child(questions.elementAt(questionNumber).toString()).child("q").value.toString()
                    answerA = snapshot.child(questions.elementAt(questionNumber).toString()).child("a").value.toString()
                    answerB = snapshot.child(questions.elementAt(questionNumber).toString()).child("b").value.toString()
                    answerC = snapshot.child(questions.elementAt(questionNumber).toString()).child("c").value.toString()
                    answerD = snapshot.child(questions.elementAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questions.elementAt(questionNumber).toString()).child("answer").value.toString()

                    quizBinding.textViewQuiestion.text = quiestion
                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD

                    quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility= View.VISIBLE
                    quizBinding.linearLayoutQuestions.visibility = View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility = View.VISIBLE
                }else{
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("You answered all Questions.Do you want to see the Result")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow,position ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow,position ->
                        val intent  = Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()

                }

                questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    fun findAnswer(){
        when(correctAnswer){
            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableClickable(){
        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false
    }

    fun restoreOptions(){
        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewD.setBackgroundColor(Color.WHITE)

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true

    }

    private fun startTimer(){
        timer =object :CountDownTimer(leftTime,1000){
            override fun onTick(milliesUntilFinish: Long) {
                leftTime = milliesUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuiestion.text = "Sorry,your time is up next to continue"
                disableClickable()
                findAnswer()


            }

        }.start()
        timerContinue = true
    }

    fun updateCountDownText(){
        val remainingTime:Int = (leftTime/1000).toInt()
        quizBinding.textViewtime.text = remainingTime.toString()

    }

    fun pauseTimer(){
           timer.cancel()
           timerContinue = false
    }

    fun resetTimer(){
          pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }

    fun sendScore(){

        users?.let {
            val userUID = it.uid
            scoreRef.child("Scores").child(userUID)
                .child("Correct").setValue(userCorrect)
            scoreRef.child("Scores").child(userUID)
                .child("Wrong").setValue(userWrong).addOnCompleteListener {

                    Toast.makeText(applicationContext, "User scores is store successfully", Toast.LENGTH_SHORT).show() }
                    val intent = Intent(this@QuizActivity,ResultActivity::class.java)
            startActivity(intent)
        }
    }

}