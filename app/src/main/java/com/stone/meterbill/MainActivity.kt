package com.stone.meterbill

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

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
    private var isShow: Boolean = false

    var bills: ArrayList<Bill> = arrayListOf(
        Bill("1 to 30", 35, 1050.0), Bill("31 to 50", 50, 1000.0),
        Bill("51 to 75", 70, 1750.0), Bill("76 to 100", 90, 2250.0), Bill("101 to 150", 25, 5500.0),
        Bill("151 to 200", 120, 6000.0), Bill("over 201", 125, 125.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        btn = findViewById(R.id.btn_calculate)
        editText = findViewById(R.id.edit_unit)
        radioGroup = findViewById(R.id.radioGroup)
        before = findViewById(R.id.layout_before)
        after = findViewById(R.id.layout_after)
        recyclerView = findViewById(R.id.rc_bill)
        totalPrice = findViewById(R.id.txt_totalprice)
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

    private fun calculate(radio: Int, unit: Double) {
        if (radio == R.id.other) {

        } else {
            checkHomeUnit(unit)
        }
    }

    private fun checkHomeUnit(unit: Double) //var units=unit
    {
        if (unit > 100) {
            when {
                unit > 200 -> changeUnit(6, unit - 200)
                unit > 150 -> changeUnit(5, unit - 150)
                else -> changeUnit(4, unit - 100)
            }
        } else {
            when {
                unit > 75 -> changeUnit(3, unit - 75)
                unit > 50 -> changeUnit(2, unit - 50)
                unit > 30 -> changeUnit(1, unit - 30)
                unit > 1 -> changeUnit(0, unit)
                else -> changeUnit(-1, 0.0)
            }
        }
    }

    //            for (i in 1..7){
//                if (units>200){
//                    temp=units-200
//                    addUnit(Bill("over 201",125,temp*125))
//                    units=temp
//                }else if (units>150)
//            }
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

    private fun changeUnit(i: Int, unit: Double) {
        list.clear()
        var bill = bills[i]
        bill.cost = (bill.price * unit)
        if (i==6 && bill.cost.toString().length>9){
            val formatter: NumberFormat = DecimalFormat("0.#######E0")
            bill.cost=formatter.format(bill.cost).toDouble()
        }
        bills[i] = bill
        showData(i)


    }

    private fun showData(i: Int) {
        var total = 0.0
        if (i != -1) {
            for (k in 0..i) {
                list.add(bills[k])
                total += bills[k].cost
            }
        }

        if (total.toString().length>10) {
            val formatter: NumberFormat = DecimalFormat("0.########E0")
            totalPrice.text = formatter.format(total)
        }else{
            totalPrice.text = total.toString()
        }
        Toast.makeText(this,""+total.toString().length ,Toast.LENGTH_LONG).show()
        adapter.setData(list)
    }

    override fun onBackPressed() {
        if (after.isVisible) {
            updateUI()
        }else {
            super.onBackPressed()
        }
    }

}