package com.kaanozen.virtualmarket.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Product

class ProductPageActivity : BaseActivity() {

    private lateinit var imgView : ImageView
    private lateinit var tvProductName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_page)

        imgView = findViewById<ImageView>(R.id.productImageView)
        tvProductName = findViewById<TextView>(R.id.productNameView)

        val product : Product = Product()

        val ONE_MEGABYTE: Long = 1024 * 1024
        FirestoreClass().getImageReference(product).getBytes(ONE_MEGABYTE).addOnSuccessListener {
            imgView.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
            tvProductName.text = product.name
        }.addOnFailureListener {
            Toast.makeText(this,"Can't Download Image File",Toast.LENGTH_SHORT).show()
        }
    }
}