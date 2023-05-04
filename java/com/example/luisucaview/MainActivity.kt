package com.example.luisucaview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.example.luisucaview.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

class MainActivity : AppCompatActivity() {

    private var requestcode : Int = 123
    lateinit var relativeLayout : RelativeLayout
    lateinit var dateText : TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth : FirebaseAuth

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }


    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@MainActivity, UserActivity::class.java))
            finish()
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar()?.hide();


            setContentView(binding.root)
            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

            firebaseAuth = FirebaseAuth.getInstance()

            binding.googleSignInBtn.setOnClickListener {

                Log.d(TAG, "onCreate: begin Google SignIn")
                val intent = googleSignInClient.signInIntent
                startActivityForResult(intent, RC_SIGN_IN)

            }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuchWithGoogleAccount(account)

            }
            catch (e: Exception) {
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }


        if (requestCode == requestcode && resultCode == RESULT_OK) {

            val imageView : ImageView = findViewById(R.id.image)
            var bitmap = data?.extras?.get("data") as Bitmap
            var cropped = Bitmap.createBitmap(bitmap, 8,40,100, 100);


            imageView.setImageBitmap(cropped)
        }
    }

    private fun firebaseAuchWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener{  authResult ->

                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggenIn")

                val firebaseUser = firebaseAuth.currentUser

                val uid = firebaseAuth!!.uid
                val email = firebaseUser!!.email
                val pfp = firebaseUser!!.photoUrl

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                if (authResult.additionalUserInfo!!.isNewUser){

                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... $email")
                    Toast.makeText(this@MainActivity, "Account created... $email", Toast.LENGTH_SHORT).show()
                }
                else{

                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user... $email")
                    Toast.makeText(this@MainActivity, "LoggedIn... $email", Toast.LENGTH_SHORT).show()
                }



                startActivity(Intent(this@MainActivity, UserActivity::class.java))
                finish()


            }
            .addOnFailureListener { e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(this@MainActivity, "LoggedIn Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }


}