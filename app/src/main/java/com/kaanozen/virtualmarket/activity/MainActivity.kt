package com.kaanozen.virtualmarket.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.model.ProductCategory
import com.kaanozen.virtualmarket.activity.recycle.ProductCategoryRecycleAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ProductCategoryRecycleAdapter.OnItemClickListener,View.OnClickListener {

    private lateinit var categoryRecycleAdapter: ProductCategoryRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleViewCategory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            categoryRecycleAdapter = ProductCategoryRecycleAdapter()
            adapter = categoryRecycleAdapter
        }

        categoryRecycleAdapter.submitItems(this.returnCategoryList())
        categoryRecycleAdapter.submitListener(this)

        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
        shopping_cart_image_view.setOnClickListener(this)
    }

    override fun OnItemClick(position: Int, item : ProductCategory) {

        if(item.isLeaf){
            var intent = Intent(this,ProductListsActivity::class.java)
            intent.putExtra("categoryID", item.id)
            startActivity(intent)
        }
        else
        {
            BaseActivity.depth+=1
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}