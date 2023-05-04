package com.example.luisucaview


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.luisucaview.databinding.CarerActivityBinding
import com.google.firebase.auth.FirebaseAuth

class CarerActivity : AppCompatActivity(){

    private var requestcode: Int = 123
    lateinit var relativeLayout: RelativeLayout
    lateinit var dateText: TextView
    private lateinit var binding: CarerActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthTarget: FirebaseAuth


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CarerActivityBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar()?.hide();
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        setContentView(R.layout.carer_activity)
        relativeLayout = findViewById(R.id.myLayout2)


    }

    fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@CarerActivity, UserActivity::class.java))
        finish()

    }

}