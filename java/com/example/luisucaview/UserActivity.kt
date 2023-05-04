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


class UserActivity : AppCompatActivity() {

    private var requestcode: Int = 123
    lateinit var relativeLayout: RelativeLayout
    lateinit var dateText: TextView
    private lateinit var binding: UserActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthTarget: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var mChart: PieChart
    private lateinit var lineData: LineData


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UserActivityBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar()?.hide();

        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        relativeLayout = findViewById(R.id.myLayout)
        val btn_click_me = findViewById(R.id.MedicineData) as Button
        // set on-click listener
        btn_click_me.setOnClickListener {
            startActivity(Intent(this@UserActivity, CarerActivity::class.java))
            finish()
        }

        val Daily_btn_click_me = findViewById(R.id.buttonDiagnose) as Button
        // set on-click listener
        Daily_btn_click_me.setOnClickListener {
            startActivity(Intent(this@UserActivity, DailyActivity::class.java))
            finish()
        }
        val map_btn_click = findViewById(R.id.MedicineData4) as Button
        // set on-click listener
        map_btn_click.setOnClickListener {
            startActivity(Intent(this@UserActivity, PointMapActivity::class.java))
            finish()
        }

        val data_btn_click = findViewById(R.id.buttonDataInp) as Button
        // set on-click listener
        data_btn_click.setOnClickListener {
            startActivity(Intent(this@UserActivity, DataActivity::class.java))
            finish()
        }

        val symptom_btn_click = findViewById(R.id.MedicineData2) as Button
        // set on-click listener
        symptom_btn_click.setOnClickListener {
            startActivity(Intent(this@UserActivity, SymptomActivity::class.java))
            finish()
        }



        database =
            FirebaseDatabase.getInstance("https://luisucaview-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference()
        database.child("Daily Score").child("Day5").get().addOnSuccessListener {
            val getScore = findViewById(R.id.ScoreButton) as Button
            getScore.text = ("Daily \nScore:\n" + it.value.toString())
        }



        println("Hello")

        val pieEntries = ArrayList<PieEntry>()
        val label = "type"
        database.child("Daily Score").get().addOnSuccessListener {
        //initializing database
            var stringvals =it.value.toString()
            stringvals = stringvals.replace("DailyValues", "").replace("{Day1","").replace("Day2","").replace("Day3","").replace("Day4","").replace("Day5","")
            stringvals = stringvals.replace("{BS", "").replace("BP","").replace("MP","").replace("CH","").replace("DP","").replace("}}","")
            stringvals = stringvals.replace(" , ", "").replace(", ","")
            var myvals = stringvals.split("=")
            println(myvals)
        val typeAmountMap: MutableMap<String, Int> = HashMap()
            typeAmountMap["Blood Pressure"] = myvals[11].toInt()
            typeAmountMap["Maximum Pressure"] = myvals[8].toInt()
            typeAmountMap["Blood Sugar"] = myvals[7].toInt()
            typeAmountMap["Daily Pain"] = myvals[10].toInt()
            typeAmountMap["Cholestoral"] = myvals[9].toInt()

            //initializing colors for the entries
            val colors = ArrayList<Int>()

            val connectionUrl = ("jdbc:sqlserver://yourserver.database.windows.net:1433;"
                + "database=MySQL;"
                + "user=yourusername@yourserver;"
                + "password=yourpassword;"
                + "encrypt=true;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;")

            colors.add(Color.parseColor("#304567"))
            colors.add(Color.parseColor("#309967"))
            colors.add(Color.parseColor("#476567"))
            colors.add(Color.parseColor("#890567"))
            colors.add(Color.parseColor("#a35567"))
            colors.add(Color.parseColor("#ff5f67"))
            colors.add(Color.parseColor("#3ca567"))

            //input data and fit data into pie chart entry
            for (type in typeAmountMap.keys) {
                pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
            }

            //collecting the entries with label name
            val pieDataSet = PieDataSet(pieEntries, label)
            //setting text size of the value
            pieDataSet.valueTextSize = 12f
            //providing color list for coloring different entries
            pieDataSet.colors = colors
            //grouping the data set from entry to chart
            val pieData = PieData(pieDataSet)
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true)
            val mpChartView = findViewById(R.id.linechart) as PieChart
            mpChartView.setData(pieData);
            mpChartView.setDrawHoleEnabled(true);
            mpChartView.setHoleColor(getColor(R.color.Transparent));
            mpChartView.getLegend().setEnabled(false);
            mpChartView.invalidate();

            val lineEntries = ArrayList<Entry>()



            lineEntries.add(Entry(10f,myvals[1].toFloat()))

            lineEntries.add(Entry(20f, myvals[2].toFloat()))

      //  database.child("Daily Score").child("Day3").get().addOnSuccessListener {
            lineEntries.add(Entry(30f, myvals[3].toFloat()))
      //  }
      //  database.child("Daily Score").child("Day4").get().addOnSuccessListener {
            lineEntries.add(Entry(40f, myvals[4].toFloat()))
     //   }
       // database.child("Daily Score").child("Day5").get().addOnSuccessListener {
            lineEntries.add(Entry(50f, myvals[5].toFloat()))
       // }
            lineEntries.add(Entry(60f,100f))

            val lineDataSet = LineDataSet(lineEntries, "Count")
            val mpChartViewline = findViewById(R.id.linechart2) as LineChart
            lineData = LineData(lineDataSet)
            mpChartViewline.data = lineData
            lineDataSet.color = Color.BLACK
            lineDataSet.valueTextColor = Color.BLUE
            if(lineEntries[lineEntries.size - 2].y < lineEntries[0].y){
                lineDataSet.fillColor =Color.parseColor("#FF0000")
            }
            lineDataSet.valueTextSize = 13f
            lineDataSet.setDrawFilled(true)
            mpChartViewline.getAxisLeft().setDrawGridLines(false);
            mpChartViewline.getXAxis().setDrawGridLines(false);
            mpChartViewline.setDrawGridBackground(false)
            mpChartViewline.getAxisLeft().setDrawAxisLine(false)
            mpChartViewline.setGridBackgroundColor(R.color.Transparent)

            mpChartViewline.setTouchEnabled(false);
            mpChartViewline.setClickable(false);
            mpChartViewline.setDoubleTapToZoomEnabled(false);
            mpChartViewline.setDoubleTapToZoomEnabled(false);

            mpChartViewline.setDrawBorders(false);
            mpChartViewline.setDrawGridBackground(false);

            mpChartViewline.getDescription().setEnabled(false);
            mpChartViewline.getLegend().setEnabled(false);

            mpChartViewline.getAxisLeft().setDrawGridLines(false);
            mpChartViewline.getAxisLeft().setDrawLabels(false);
            mpChartViewline.getAxisLeft().setDrawAxisLine(false);

            mpChartViewline.getXAxis().setDrawGridLines(false);
            mpChartViewline.getXAxis().setDrawLabels(false);
            mpChartViewline.getXAxis().setDrawAxisLine(false);

            mpChartViewline.getAxisRight().setDrawGridLines(false);
            mpChartViewline.getAxisRight().setDrawLabels(false);
            mpChartViewline.getAxisRight().setDrawAxisLine(false);
            mpChartViewline.invalidate()


        }



            relativeLayout.setOnTouchListener(object : OnSwipeTouchListener(this@UserActivity) {
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    Toast.makeText(
                        this@UserActivity, "Swipe Left gesture detected",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onSwipeUp() {
                    super.onSwipeUp()
                    takePicture()

                }

                override fun onSwipeDown() {
                    super.onSwipeDown()

                }
            })
    }

    fun takePicture() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestcode)
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


    fun checkCarer() {
        startActivity(Intent(this@UserActivity, CarerActivity::class.java))
        finish()


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









