package com.example.luisucaview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.luisucaview.R
import com.anychart.AnyChartView
import com.anychart.AnyChart
import com.anychart.graphics.vector.SolidFill
import com.anychart.enums.SelectionMode
import com.anychart.chart.common.dataentry.DataEntry
import java.util.ArrayList
import com.example.luisucaview.PointMapActivity.CustomDataEntry
import com.example.luisucaview.databinding.DailyActivityBinding

class PointMapActivity : AppCompatActivity() {

    override fun onBackPressed() {
        startActivity(Intent(this@PointMapActivity, UserActivity::class.java))
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

        setContentView(R.layout.pointmap_activity)

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
        val map = AnyChart.map()

        map.credits().enabled(true)

        map.credits()
            .url("https://opendata.socrata.com/dataset/Airport-Codes-mapped-to-Latitude-Longitude-in-the-/rxrh-4cxm")
            .text("Data source: https://opendata.socrata.com")
            .logoSrc("https://opendata.socrata.com/stylesheets/images/common/favicon.ico")
        map.unboundRegions()
            .enabled(true)
            .fill(SolidFill("#E1E1E1", 1))
            .stroke("#D2D2D2")
        map.geoData("anychart.maps.united_kingdom")
        map.title()
            .enabled(true)
            .useHtml(true)
            .padding(0, 0, 10, 0)
            .text(
                "Locations you have updated your Blood Pressure<br/><span style=\"color:#929292; font-size: 12px;\">" +
                        "According to opendata.socrata.com<br/>Cities and names were collected from Wikipedia.org</span>"
            )

        println(map)
        val series = map.marker(data)

        series.tooltip()
            .useHtml(true)
            .padding(8, 13, 10, 13)
            .title(false)
            .separator(false)
            .fontSize(14)
            .format(
                """function() {
            return '<span>' + this.getData('name') + '</span><br/>' +
              '<span style="font-size: 12px; color: #E1E1E1">City: ' +
              this.getData('city') + '</span>';
          }"""
            )
        series.size(5)
            .labels(false)
        series.stroke("2 #E1E1E1")
            .fill("#1976d2", 1)
        series.selectionMode(SelectionMode.NONE)
        anyChartView.addScript("file:///android_asset/united_kingdom.js")
        anyChartView.addScript("file:///android_asset/proj4.js")
        map.background().fill("@drawable/main_header_selector");
        anyChartView.setChart(map)



    }




    private val data: List<DataEntry>
        private get() {
            val data: MutableList<DataEntry> = ArrayList()
            data.add(
                CustomDataEntry(
                    "PSL",
                    "Perth",
                    "Perth City",
                    56.3950,
                    -3.4308
                )
            )
            data.add(CustomDataEntry("CDF", "Cardiff", "Cardiff City Centre", 51.4837, -3.1681))
            data.add(CustomDataEntry("CDF", "Cardiff", "Cardiff Catheys", 51.4837, -3.1681))
            data.add(CustomDataEntry("BRS", "Salisbury", "Ramsbury", 51.4427, -1.6017 ))
            data.add(CustomDataEntry("MCH", "Manchester", "Manchester", 53.4808, -2.2426 ))
            data.add(CustomDataEntry("LDS", "Leeds", "Leeds", 53.8008, -2.2426 ))


            data.add(
                CustomDataEntry(
                    "LDA",
                    "London",
                    "London",
                    51.5072,
                    0.1276
                )
            )
            return data
        }

    internal inner class CustomDataEntry(
        id: String?,
        city: String?,
        name: String?,
        latitude: Double?,
        longitude: Double?
    ) : DataEntry() {
        init {
            setValue("id", id)
            setValue("city", city)
            setValue("name", name)
            setValue("lat", latitude)
            setValue("long", longitude)
        }
    }
}