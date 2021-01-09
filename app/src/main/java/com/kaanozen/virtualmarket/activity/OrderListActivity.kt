package com.kaanozen.virtualmarket.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Order
import com.kaanozen.virtualmarket.activity.recycle.OrderRecycleAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class OrderListActivity : BaseActivity(), OrderRecycleAdapter.OnItemClickListener, View.OnClickListener {

    private lateinit var orderRecycleAdapter: OrderRecycleAdapter //Create the order recycle view

    private var orders : ArrayList<Order> = ArrayList<Order>() //Array to hold user orders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        //Initialize recycle view
        recycleViewOrder.apply {
            layoutManager = LinearLayoutManager(this@OrderListActivity)
            orderRecycleAdapter = OrderRecycleAdapter()
            adapter = orderRecycleAdapter
        }

        orderRecycleAdapter.submitItems(this.returnOrderList())
        orderRecycleAdapter.submitListener(this)

        //Set event listeners of the bottom application navigation views
        findViewById<ImageView>(R.id.home_bottom_image_view).setOnClickListener(this)
        findViewById<ImageView>(R.id.user_bottom_logo_image_view).setOnClickListener(this)
        findViewById<ImageView>(R.id.shopping_cart_image_view).setOnClickListener(this)
    }

    //Function get list of orders for a given user
    private fun returnOrderList() : ArrayList<Order> {
        orders.clear() //Clear th list

        var queryRes = FirestoreClass().getOrders() //Get query result

        for (x in queryRes.result!!.documents) //For each order in the query result
            orders.add(x.toObject(Order::class.java)!!) //Add them to the order list

        return this.orders //return list
    }

    //Create a coroutine
    //Cancel an order
    //Notify the recycle view that order is cancelled
    override fun OnItemClick(position: Int, item: Order) {

        CoroutineScope(Main).launch {
            if(FirestoreClass().removeOrder(item))
            {
                orderRecycleAdapter.removeItem(position)
                orderRecycleAdapter.notifyItemRangeChanged(position,orderRecycleAdapter.itemCount)
                orderRecycleAdapter.notifyItemRemoved(position)
            }
            else
            {
                showErrorSnackBar("İşlem Başarısız",true)
            }
        }
    }
}