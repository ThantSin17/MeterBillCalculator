package com.stone.meterbill

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(context:Context) : RecyclerView.Adapter<Adapter.BillViewHolder>() {
var bills:ArrayList<Bill> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.item_bill,parent,false)
        return BillViewHolder(view)
    }


    fun setData(bills:ArrayList<Bill>){
        this.bills.clear()
        this.bills.addAll(bills)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return bills.size
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        var bill=bills[position]
        holder.bind(bill)
    }

    class BillViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        private var unit: TextView = itemView.findViewById<TextView>(R.id.txt_unit)
        private var price: TextView = itemView.findViewById<TextView>(R.id.txt_price)
        private var cost = itemView.findViewById<TextView>(R.id.txt_cost)
        fun bind(bill: Bill){
            unit.text=bill.unit
            price.text=bill.price.toString()
            cost.text=bill.cost.toString()
        }
    }

}