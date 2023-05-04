package com.example.luisucaview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.RGBToHSV

import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.anychart.*
import com.example.luisucaview.databinding.DailyActivityBinding
import com.example.luisucaview.databinding.UserActivityBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.firebase.FirebaseApp.getInstance
import com.google.firebase.analytics.FirebaseAnalytics.getInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class DataActivity : AppCompatActivity() {


    private var requestcode: Int = 123
    lateinit var relativeLayout: RelativeLayout
    lateinit var dateText: TextView
    private lateinit var binding: UserActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthTarget: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var mChart: PieChart
    private lateinit var lineData: LineData

    override fun onBackPressed() {
        startActivity(Intent(this@DataActivity, UserActivity::class.java))
        finish()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar()?.hide();

        setContentView(R.layout.data_activitiy)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val Daily_btn_click_me = findViewById(R.id.button) as Button
        // set on-click listener
        val SeekBar = findViewById(R.id.seekBar) as SeekBar
        val SeekBar1 = findViewById(R.id.seekBar1) as SeekBar
        val SeekBar2 = findViewById(R.id.seekBar2) as SeekBar
        val SeekBar3 = findViewById(R.id.seekBar3) as SeekBar
        val SeekBar4 = findViewById(R.id.seekBar4) as SeekBar


        Daily_btn_click_me.setOnClickListener {

            database =
                FirebaseDatabase.getInstance("https://luisucaview-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference()




            database.child("Daily Score").get().addOnSuccessListener {
                //initializing database
                var stringvals = it.value.toString()
                stringvals =
                    stringvals.replace("DailyValues", "").replace("{Day1", "").replace("Day2", "")
                        .replace("Day3", "").replace("Day4", "").replace("Day5", "")
                stringvals = stringvals.replace("{BS", "").replace("BP", "").replace("MP", "")
                    .replace("CH", "").replace("DP", "").replace("}}", "")
                stringvals = stringvals.replace(" , ", "").replace(", ", "")
                var myvals = stringvals.split("=")

                val average_score =
                    ((SeekBar.getProgress() + SeekBar1.getProgress() + SeekBar2.getProgress() + SeekBar3.getProgress() + SeekBar4.getProgress()) / 5)
                var temp_score1 = 1
                var temp_score2 = 1
                var temp_score3 = 1
                var temp_score4 = 1


                temp_score1 = myvals[2].toInt()
                temp_score2 = myvals[3].toInt()
                temp_score3 = myvals[4].toInt()
                temp_score4 = myvals[5].toInt()

                database.child("Daily Score").child("Day5").setValue(average_score)
                database.child("Daily Score").child("Day4").setValue(temp_score4)
                database.child("Daily Score").child("Day3").setValue(temp_score3)
                database.child("Daily Score").child("Day2").setValue(temp_score2)
                database.child("Daily Score").child("Day1").setValue(temp_score1)

                database.child("Daily Score").child("DailyValues").child("BP").setValue(SeekBar.getProgress())
                database.child("Daily Score").child("DailyValues").child("BS").setValue(SeekBar4.getProgress())
                database.child("Daily Score").child("DailyValues").child("CH").setValue(SeekBar3.getProgress())
                database.child("Daily Score").child("DailyValues").child("DP").setValue(SeekBar2.getProgress())
                database.child("Daily Score").child("DailyValues").child("MP").setValue(SeekBar1.getProgress())


            }


            startActivity(Intent(this@DataActivity, UserActivity::class.java))
            finish()
        }


    }

    fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser

        val uid = firebaseAuth!!.uid
        val email = firebaseUser!!.email
        val pfp = firebaseUser!!.photoUrl

        DownloadImageFromInternet(findViewById(R.id.imageView6)).execute(pfp.toString())



        if (firebaseUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestcode && resultCode == RESULT_OK) {

            val imageView : ImageView = findViewById(R.id.image)
            var bitmap = data?.extras?.get("data") as Bitmap
            var cropped = Bitmap.createBitmap(bitmap, 8,40,100, 100);
            imageView.setImageBitmap(cropped)

        }
    }





    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            //
        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }

}