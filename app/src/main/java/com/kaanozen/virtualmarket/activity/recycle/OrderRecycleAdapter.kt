package com.kaanozen.virtualmarket.activity.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Order
import kotlinx.android.synthetic.main.activity_order_recycle_unit_view.view.*

class OrderRecycleAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<Order> = ArrayList()
    private lateinit var listener : OrderRecycleAdapter.OnItemClickListener

    fun submitItems(orders : ArrayList<Order>)
    {
        this.items = orders
    }

    fun submitListener(listener_: OrderRecycleAdapter.OnItemClickListener)
    {
        this.listener = listener_
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_order_recycle_unit_view, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is OrderRecycleAdapter.OrderViewHolder -> {holder.bind(items[position])}
        }
    }

    inner class OrderViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        private val order_image: ImageView = itemView.order_image
        private val order_title: TextView = itemView.order_title
        private val order_cost : TextView = itemView.order_cost
        private val order_quantity : TextView = itemView.order_quantity
        private val cancel_button : Button = itemView.ordercancelBut


        fun bind(order: Order){
            val ONE_MEGABYTE: Long = 1024 * 1024
            Firebase.storage.reference.child("product").child(order.productID + ".png").getBytes(ONE_MEGABYTE).addOnSuccessListener { arr ->
                Glide.with(itemView)
                        .load(arr)
                        .into(order_image)
            }.addOnFailureListener { order_image.setImageResource(R.drawable.water)}

            order_title.text = order.productName
            order_cost.text = "Adet: " + order.cost.toString()
            order_quantity.text = "Tutar: " + order.quantitiy.toString()
        }

        init {
            cancel_button.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.OnItemClick(adapterPosition, items[adapterPosition])
        }
    }

    interface OnItemClickListener
    {
        fun OnItemClick(position: Int, item : Order)
    }
}