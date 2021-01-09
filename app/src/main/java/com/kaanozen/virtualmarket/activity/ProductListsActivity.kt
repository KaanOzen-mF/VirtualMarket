package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.recycle.ProductRecycleAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product_lists.*
import kotlinx.android.synthetic.main.activity_product_lists.home_bottom_image_view
import kotlinx.android.synthetic.main.activity_product_lists.shopping_cart_image_view
import kotlinx.android.synthetic.main.activity_product_lists.user_bottom_logo_image_view

class ProductListsActivity : BaseActivity(), ProductRecycleAdapter.OnItemClickListener,View.OnClickListener {

    private lateinit var productRecycleAdapter: ProductRecycleAdapter //Create the product recycle view

    private var categoryID = "" //Category info of the product list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_lists)

        categoryID = intent.extras!!.getString("categoryID")!! //Get which category items is going to be shown

        //Initialize recycle view
        recycleViewProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductListsActivity)
            productRecycleAdapter = ProductRecycleAdapter()
            adapter = productRecycleAdapter
        }

        productRecycleAdapter.submitItems(this.returnProductList(categoryID)!!)
        productRecycleAdapter.submitListener(this@ProductListsActivity)


        //Set event listeners of the bottom application navigation views
        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
        shopping_cart_image_view.setOnClickListener(this)

    }

    //Open product page when recycle view item is clicked
    //When recyclerview item is clicked, that items View.onClick() method will call this method
    override fun OnItemClick(position: Int, item : Product) {
        val intent = Intent(this, ProductPageActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }
}