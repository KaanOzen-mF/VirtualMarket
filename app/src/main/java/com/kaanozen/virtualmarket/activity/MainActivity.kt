package com.kaanozen.virtualmarket.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.MarketItem
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.model.ProductCategory
import com.kaanozen.virtualmarket.activity.recycle.ProductCategoryRecycleAdapter
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ProductCategoryRecycleAdapter.OnItemClickListener {

    private lateinit var categoryRecycleAdapter: ProductCategoryRecycleAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an instance of Android SharedPreferences
        val sharedPreferences =
            getSharedPreferences(Constants.SHOP_PREFERENCES, Context.MODE_PRIVATE)

        /*for (x in 0..7){

            var cat = ProductCategory()

            cat.name = "a"
            cat.imageURL = "a"
            cat.isLeaf = false
            cat.depth = 0

            FirestoreClass().addCategory(cat,this)
        }

        for (x in 0..13 )
        {
            var pro = Product()
            pro.name = "a"
            pro.imageURL = "a"
            pro.parentID = "a"
            pro.stock = 0
            pro.price = 0.0
            pro.information = "a"

            FirestoreClass().addProduct(pro,this)
        }*/

        recycleViewCategory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            categoryRecycleAdapter = ProductCategoryRecycleAdapter()
            adapter = categoryRecycleAdapter
        }

        categoryRecycleAdapter.submitItems(this.returnCategoryList())
        categoryRecycleAdapter.submitListener(this)
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