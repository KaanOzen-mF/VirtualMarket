package com.kaanozen.virtualmarket.activity

import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.ProductCategory

open class BaseActivity : AppCompatActivity() {

    private var categories = ArrayList<ProductCategory>()

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

        var queryRes = FirestoreClass().getCategories(depth,this)

        for (x in queryRes.result!!.documents)
            categories.add(x.toObject(ProductCategory::class.java)!!)

        return this.categories
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(depth >= 1)
            depth--

        Toast.makeText(this, depth.toString(), Toast.LENGTH_SHORT).show()
    }
}