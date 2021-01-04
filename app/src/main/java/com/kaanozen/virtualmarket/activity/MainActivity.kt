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

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        recycleViewCategory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            categoryRecycleAdapter = ProductCategoryRecycleAdapter()
            adapter = categoryRecycleAdapter
        }

        categoryRecycleAdapter.submitItems(this.returnCategoryList())
        categoryRecycleAdapter.submitListener(this)
    }

    override fun OnItemClick(position: Int, item : ProductCategory) {

        if(item.isLeaf && false){ TODO("THE FALSE NEEDS TO BE DELETED TO SHOW PRODUCT LIST OF THAT CATEGORY")
            var intent = Intent(this,ProductPageActivity::class.java)
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