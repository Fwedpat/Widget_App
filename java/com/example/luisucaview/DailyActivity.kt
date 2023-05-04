package com.example.luisucaview

import android.annotation.SuppressLint
import android.content.Intent
import java.util.Arrays;
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.RGBToHSV
import android.os.AsyncTask

import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.os.Bundle;
import android.view.View

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import java.util.ArrayList;
import java.util.List;
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.anychart.*
import com.anychart.charts.Mekko
import com.example.luisucaview.databinding.DailyActivityBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import com.example.luisucaview.PointMapActivity.CustomDataEntry

import com.anychart.scales.OrdinalColor

import com.anychart.chart.common.dataentry.SingleValueDataSet
import com.anychart.charts.LinearGauge
import com.anychart.enums.*


class DailyActivity : AppCompatActivity()  {

    private var requestcode: Int = 123
    lateinit var relativeLayout: RelativeLayout
    lateinit var dateText: TextView
    private lateinit var binding: DailyActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthTarget: FirebaseAuth
    private lateinit var database: DatabaseReference



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DailyActivityBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar()?.hide();
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setContentView(R.layout.daily_activity)

        val webView = findViewById(R.id.WebView1) as WebView
        val websettings: WebSettings = webView.getSettings()
        websettings.setJavaScriptEnabled(true)
        websettings.setBuiltInZoomControls(true)
        websettings.setSupportZoom(true)
        websettings.setUseWideViewPort(true)
        webView.setWebChromeClient(WebChromeClient())
        webView.setInitialScale(1)

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                val dataset: IntArray = intArrayOf(10, 20, 30, 40, 50)
                val text = Arrays.toString(dataset)
                webView.loadUrl(
                    "javascript:initGraph(" +
                            text + ", " +
                            (webView.getHeight()) + ", "
                            + (webView.getWidth()) + ")"
                )
            }
        }
        webView.loadUrl("file:///android_asset/index.html");





        val anyChartView = findViewById(R.id.any_chart_view) as AnyChartView
        val bar3d: Cartesian3d = AnyChart.bar3d()
        bar3d.animation(true)
        bar3d.padding(10.0, 40.0, 5.0, 20.0)
        bar3d.yScale().minimum(0.0)
        bar3d.xAxis(0).labels()
            .rotation(-90.0)
            .padding(0.0, 0.0, 20.0, 0.0)
        bar3d.yAxis(0).labels().format("{%Value}{groupsSeparator: }")
        val data: MutableList<DataEntry> = ArrayList<DataEntry>()
        data.add(CustomDataEntry("Without CVD", 110, 154, 405, 238))
        data.add(CustomDataEntry("With CVD", 140, 180, 506, 140))
        val set = Set.instantiate()
        set.data(data)
        val bar1Data: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val bar2Data: Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        bar3d.bar(bar1Data)
            .name("AverageHR")
        bar3d.bar(bar2Data)
            .name("MaximumHR")
        bar3d.legend().enabled(true)
        bar3d.legend().fontSize(13.0)
        bar3d.legend().padding(0.0, 0.0, 20.0, 0.0)
        bar3d.interactivity().hoverMode(HoverMode.SINGLE)
        bar3d.tooltip()
            .positionMode(TooltipPositionMode.POINT)
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(0.0)
            .format("{%Value}")
        bar3d.zAspect("10%")
            .zPadding(20.0)
            .zAngle(45.0)
            .zDistribution(true)
        bar3d.background().fill("#404040");
        bar3d.background().fill("#404040");
        anyChartView.setChart(bar3d)
        newOne()

    }

    private inner class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?,
        value3: Number?,
        value4: Number?
    ) : ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
            setValue("value4", value4)
        }
    }





    override fun onBackPressed() {
        startActivity(Intent(this@DailyActivity, UserActivity::class.java))
        finish()

    }


    fun newOne(){


        val anyChartView2 = findViewById(R.id.any_chart_view2) as AnyChartView

        val linearGauge: LinearGauge = AnyChart.linear()

        linearGauge.data(SingleValueDataSet(arrayOf(5.3)))

        linearGauge.layout(Layout.HORIZONTAL)

        linearGauge.label(0)
            .position(Position.LEFT_CENTER)
            .anchor(Anchor.LEFT_CENTER)
            .offsetY("-50px")
            .offsetX("50px")
            .fontColor("black")
            .fontSize(17)
        linearGauge.label(0).text("Total Rainfall")

        linearGauge.label(1)
            .position(Position.LEFT_CENTER)
            .anchor(Anchor.LEFT_CENTER)
            .offsetY("40px")
            .offsetX("50px")
            .fontColor("#777777")
            .fontSize(17)
        linearGauge.label(1).text("Drought Hazard")

        linearGauge.label(2)
            .position(Position.RIGHT_CENTER)
            .anchor(Anchor.RIGHT_CENTER)
            .offsetY("40px")
            .offsetX("50px")
            .fontColor("#777777")
            .fontSize(17)
        linearGauge.label(2).text("Flood Hazard")

        val scaleBarColorScale = OrdinalColor.instantiate()
        scaleBarColorScale.ranges(
            arrayOf(
                "{ from: 0, to: 2, color: ['red 0.5'] }",
                "{ from: 2, to: 3, color: ['yellow 0.5'] }",
                "{ from: 3, to: 7, color: ['green 0.5'] }",
                "{ from: 7, to: 8, color: ['yellow 0.5'] }",
                "{ from: 8, to: 10, color: ['red 0.5'] }"
            )
        )

        linearGauge.scaleBar(0)
            .width("5%")
            .colorScale(scaleBarColorScale)

        linearGauge.marker(0)
            .type(MarkerType.TRIANGLE_DOWN)
            .color("red")
            .offset("-3.5%")
            .zIndex(10)

        linearGauge.scale()
            .minimum(0)
            .maximum(10)
//        linearGauge.scale().ticks

        //        linearGauge.scale().ticks
        linearGauge.axis(0)
            .minorTicks(false)
            .width("1%")
        linearGauge.axis(0)
            .offset("-1.5%")
            .orientation(Orientation.TOP)
            .labels("top")

        linearGauge.padding(0, 30, 0, 30)

        anyChartView2.setChart(linearGauge)
    }


    fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser

        val uid = firebaseAuth!!.uid
        val email = firebaseUser!!.email
        val pfp = firebaseUser!!.photoUrl


        if (firebaseUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

}