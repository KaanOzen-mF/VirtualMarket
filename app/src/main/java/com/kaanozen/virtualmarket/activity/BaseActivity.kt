package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.model.ProductCategory

open class BaseActivity : AppCompatActivity(), View.OnClickListener {

    private var categories = ArrayList<ProductCategory>()
    private var products = ArrayList<Product>()

    companion object {
        var depth : Int = 0
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun returnCategoryList(): ArrayList<ProductCategory> {

        categories.clear()

        var queryRes = FirestoreClass().getCategories(depth,this)

        for (x in queryRes.result!!.documents)
            categories.add(x.toObject(ProductCategory::class.java)!!)

        return this.categories
    }

    fun returnProductList(categoryID : String ): ArrayList<Product> {

        products.clear()

        var queryRes = FirestoreClass().getProducts(categoryID,this)

        for (x in queryRes.result!!.documents)
            products.add(x.toObject(Product::class.java)!!)

        return this.products
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.home_bottom_image_view ->{
                BaseActivity.depth = 0
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.user_bottom_logo_image_view ->{
                FirestoreClass().getUserDetails(this)
            }
            R.id.shopping_cart_image_view ->{
                val intent = Intent(this,OrderListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        when(this){

            is MainActivity -> {
                if(depth >= 1)
                    depth--
            }

            is ProductListsActivity -> {
                this.finish()
            }

            is OrderListActivity -> {
                this.finish()
            }
        }
    }
}