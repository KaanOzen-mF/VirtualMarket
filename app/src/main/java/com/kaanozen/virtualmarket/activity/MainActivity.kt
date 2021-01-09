package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.ProductCategory
import com.kaanozen.virtualmarket.activity.recycle.ProductCategoryRecycleAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ProductCategoryRecycleAdapter.OnItemClickListener,View.OnClickListener {

    //Main activity shows the categories

    private lateinit var categoryRecycleAdapter: ProductCategoryRecycleAdapter //Create the category recycle view

    private var categoryID = "" //Category info of the sub-categories list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryID = intent.extras!!.getString("categoryID", "")

        //Initialize recycle view
        recycleViewCategory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            categoryRecycleAdapter = ProductCategoryRecycleAdapter()
            adapter = categoryRecycleAdapter
        }

        categoryRecycleAdapter.submitItems(this.returnCategoryList(categoryID)!!)
        categoryRecycleAdapter.submitListener(this)

        //Set event listeners of the bottom application navigation views
        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
        shopping_cart_image_view.setOnClickListener(this)
    }

    //Navigate between categories depending on the properties of the selected category
    //If it is a leaf category, show its products
    //If it is a inner node category, show its sub categories
    //When recyclerview item is clicked, that items View.onClick() method will call this method
    override fun OnItemClick(position: Int, item : ProductCategory) {

        if(item.isLeaf){ //Is clicked category is leaf node
            var intent = Intent(this,ProductListsActivity::class.java)
            intent.putExtra("categoryID", item.id)
            startActivity(intent)
        }
        else
        {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("categoryID", item.id)
            startActivity(intent)
        }
    }
}