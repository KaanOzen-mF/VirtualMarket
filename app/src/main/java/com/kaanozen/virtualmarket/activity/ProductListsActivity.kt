package com.kaanozen.virtualmarket.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.recycle.ProductCategoryRecycleAdapter
import com.kaanozen.virtualmarket.activity.recycle.ProductRecycleAdapter
import kotlinx.android.synthetic.main.activity_product_lists.*

class ProductListsActivity : BaseActivity(), ProductRecycleAdapter.OnItemClickListener,View.OnClickListener {

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

        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
    }

    override fun OnItemClick(position: Int, item : Product) {
        val intent = Intent(this, ProductPageActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        if( view != null){

            when(view.id){
                R.id.home_bottom_image_view ->{
                    BaseActivity.depth = 0
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.user_bottom_logo_image_view ->{
                    val intent = Intent(this,UserProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}