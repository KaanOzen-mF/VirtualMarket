package com.kaanozen.virtualmarket.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Order
import com.kaanozen.virtualmarket.activity.recycle.OrderRecycleAdapter
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class OrderListActivity : BaseActivity(), OrderRecycleAdapter.OnItemClickListener, View.OnClickListener {

    private lateinit var orderRecycleAdapter: OrderRecycleAdapter

    private var orders : ArrayList<Order> = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        recycleViewOrder.apply {
            layoutManager = LinearLayoutManager(this@OrderListActivity)
            orderRecycleAdapter = OrderRecycleAdapter()
            adapter = orderRecycleAdapter
        }

        orderRecycleAdapter.submitItems(this.returnOrderList())
        orderRecycleAdapter.submitListener(this)

        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
    }

    fun returnOrderList() : ArrayList<Order> {

        orders.clear()

        var queryRes = FirestoreClass().getOrders()

        for (x in queryRes.result!!.documents)
            orders.add(x.toObject(Order::class.java)!!)

        return this.orders
    }

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
                Toast.makeText(this@OrderListActivity, "İşlem Başarısız", Toast.LENGTH_SHORT).show()
            }
        }
    }
}