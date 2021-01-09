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

    //Categories and products are stored in the BaseActivity instances

    private var categories = ArrayList<ProductCategory>()
    private var products = ArrayList<Product>()

    //SnackBar is a view object that is used for displaying messages

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        //If it is an error message use red but if it is not an error message use green

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

    //Function to return all categories for a given depth value

    fun returnCategoryList(parentID : String): ArrayList<ProductCategory>? {

        categories.clear() //Clear the list

        var queryRes = FirestoreClass().getCategories(parentID,this)

        if(queryRes.isSuccessful) { // Query is OK
            for (x in queryRes.result!!.documents)
                categories.add(x.toObject(ProductCategory::class.java)!!) // Get objects as categories

            return this.categories //Return them
        }
        else {
            showErrorSnackBar("Kategori verisi alınamadı", true)

            return null
        }
    }

    //Function to return all products for a given category

    fun returnProductList(categoryID : String ): ArrayList<Product>? {

        products.clear() //Clear list

        var queryRes = FirestoreClass().getProducts(categoryID,this)

        if(queryRes.isSuccessful) { //If query is OK
            for (x in queryRes.result!!.documents)
                products.add(x.toObject(Product::class.java)!!)

            return this.products
        }
        else {
            showErrorSnackBar("Kategori verisi alınamadı", true)

            return null
        }
    }

    //OnClick method for navigation bar at the bottom

    //User image, Shopping cart image, Home button

    //Navigation Handler

    override fun onClick(view: View?) { //Take action depending on the given view object
        when(view!!.id){
            R.id.home_bottom_image_view ->{
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("categoryID", "root")
                startActivity(intent)
                finish()
            }
            R.id.user_bottom_logo_image_view ->{ //Get user info from the database and open the profile page
                FirestoreClass().getUserDetails(this)
            }
            R.id.shopping_cart_image_view ->{ //Open the shopping cart page
                val intent = Intent(this,OrderListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //Take action when android system back button is pressed

    //To navigate back in the categories or product lists

    //Destroy activates to prevent memory leakage

    override fun onBackPressed() {
        super.onBackPressed()
        when(this){
            is ProductListsActivity -> {
                this.finish()
            }

            is OrderListActivity -> {
                this.finish()
            }
        }
    }
}