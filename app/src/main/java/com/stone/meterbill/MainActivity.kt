package com.stone.meterbill

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var editText: EditText
    lateinit var btn: Button
    lateinit var before: ConstraintLayout
    lateinit var after: ConstraintLayout
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var recyclerView: RecyclerView
    lateinit var totalPrice: TextView
    var list: ArrayList<Bill> = arrayListOf()
    lateinit var adapter: Adapter
    lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null

    var bills: ArrayList<Bill> = arrayListOf(
        Bill("1 to 30", 35, 1050.0), Bill("31 to 50", 50, 1000.0),
        Bill("51 to 75", 70, 1750.0), Bill("76 to 100", 90, 2250.0), Bill("101 to 150", 25, 5500.0),
        Bill("151 to 200", 120, 6000.0), Bill("over 201", 125, 125.0)
    )
    var otherBills:ArrayList<Bill> = arrayListOf(
        Bill("1 to 500", 125, 62500.0), Bill("501 to 5000", 135, 607500.0),
        Bill("5001 to 10000", 145, 725000.0), Bill("10001 to 20000", 155, 1550000.0), Bill("20001 to 50000", 167, 5010000.0),
        Bill("50001 to 100000", 175, 8750000.0), Bill("over 100001", 180, 180.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        currentLanguage = intent.getStringExtra(currentLang).toString()

        //bind view
        btn = findViewById(R.id.btn_calculate)
        editText = findViewById(R.id.edit_unit)
        radioGroup = findViewById(R.id.radioGroup)
        before = findViewById(R.id.layout_before)
        after = findViewById(R.id.layout_after)
        recyclerView = findViewById(R.id.rc_bill)
        totalPrice = findViewById(R.id.txt_totalprice)

        //bind adapter
        adapter = Adapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter



        btn.setOnClickListener {
            var radio = radioGroup.checkedRadioButtonId
            radioButton = findViewById(radio)
            var unit: Double = editText.text.toString().toDouble()
            calculate(radio, unit)
            updateUI()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.unicode -> setLocal("en")
            R.id.zawgyi ->setLocal("my")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLocal(localeName: String) {
        if (localeName!=currentLanguage){
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this@MainActivity, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
        }
    }

    private fun calculate(radio: Int, unit: Double) {
        if (radio == R.id.other) {
            checkOtherUnit(unit)
        } else {
            checkHomeUnit(unit)
        }
    }

    private fun checkHomeUnit(unit: Double) //var units=unit
    {
        if (unit > 100) {
            when {
                unit > 200 -> changeHomeUnit(6, unit - 200)
                unit > 150 -> changeHomeUnit(5, unit - 150)
                else -> changeHomeUnit(4, unit - 100)
            }
        } else {
            when {
                unit > 75 -> changeHomeUnit(3, unit - 75)
                unit > 50 -> changeHomeUnit(2, unit - 50)
                unit > 30 -> changeHomeUnit(1, unit - 30)
                unit > 1 -> changeHomeUnit(0, unit)
                else -> changeHomeUnit(-1, 0.0)
            }
        }
    }
    private fun checkOtherUnit(unit: Double) //var units=unit
    {
        if (unit > 10000) {
            when {
                unit > 100000-> changeOtherUnit(6, unit - 100000)
                unit > 50000 -> changeOtherUnit(5, unit - 50000)
                unit > 20000 -> changeOtherUnit(4,unit-20000)
                else -> changeOtherUnit(3, unit - 10000)
            }
        } else {
            when {
                unit > 5000 -> changeOtherUnit(2, unit - 5000)
                unit > 500 -> changeOtherUnit(1, unit - 500)
                unit > 1 -> changeOtherUnit(0, unit)
                else -> changeOtherUnit(-1, 0.0)
            }
        }
    }
    @SuppressLint("WrongConstant")
    fun updateUI() {
        if (!after.isVisible) {
            before.visibility = GONE
            after.visibility = VISIBLE
        } else {
            before.visibility = VISIBLE
            after.visibility = GONE
        }
    }

    private fun changeHomeUnit(i: Int, unit: Double) {
        var bill = bills[i]
        bill.cost = (bill.price * unit)
        if (i==6 && bill.cost.toString().length>9){
            val formatter: NumberFormat = DecimalFormat("0.#######E0")
            bill.cost=formatter.format(bill.cost).toDouble()
        }
        bills[i] = bill
        showData(i,bills)
    }
    private fun changeOtherUnit(i: Int, unit: Double){
        var bill = otherBills[i]
        bill.cost = (bill.price * unit)
        if (i==6 && bill.cost.toString().length>9){
            val formatter: NumberFormat = DecimalFormat("0.#######E0")
            bill.cost=formatter.format(bill.cost).toDouble()
        }

        otherBills[i] = bill
        showData(i,otherBills)
    }

    private fun showData(i: Int,dataList:ArrayList<Bill>) {
        list.clear()
        var total = 0.0
        if (i != -1) {
            for (k in 0..i) {
                list.add(dataList[k])
                total += dataList[k].cost
            }
        }

        if (total.toString().length>10) {
            val formatter: NumberFormat = DecimalFormat("0.########E0")
            totalPrice.text = formatter.format(total)
        }else{
            totalPrice.text = total.toString()
        }
        Toast.makeText(this,""+changeToMyanmerNumber(total).toString().length ,Toast.LENGTH_LONG).show()
        adapter.setData(list)
    }
    private fun changeToMyanmerNumber(str:Double): String{
        var string=str.toString()
        return string.replace("0","၀").replace("1","၁").replace("2","၂").replace("3","၃")
            .replace("4","၄").replace("5","၅").replace("6","၆").replace("7","၇")
            .replace("8","၈").replace("9","၉").replace(".",".")
    }

    override fun onBackPressed() {
        if (after.isVisible) {
            updateUI()
        }else {
            super.onBackPressed()
        }
    }

}