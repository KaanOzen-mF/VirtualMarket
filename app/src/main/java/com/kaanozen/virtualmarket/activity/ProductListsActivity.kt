package com.kaanozen.virtualmarket.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.recycle.ProductCategoryRecycleAdapter
import com.kaanozen.virtualmarket.activity.recycle.ProductRecycleAdapter
import kotlinx.android.synthetic.main.activity_product_lists.*

class ProductListsActivity : BaseActivity(), ProductRecycleAdapter.OnItemClickListener {

    private lateinit var productRecycleAdapter: ProductRecycleAdapter

    private var categoryID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_lists)

        categoryID = intent.extras!!.getString("categoryID")!!

        recycleViewProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductListsActivity)
            productRecycleAdapter = ProductRecycleAdapter()
            adapter = productRecycleAdapter
        }

        productRecycleAdapter.submitItems(this.returnProductList(categoryID))
        productRecycleAdapter.submitListener(this@ProductListsActivity)
    }

    override fun OnItemClick(position: Int, item : Product) {
        val intent = Intent(this, ProductPageActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }
}