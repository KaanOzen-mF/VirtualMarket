package com.kaanozen.virtualmarket.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

        val product : Product = intent.extras!!.getParcelable<Product>("item")!!

        val ONE_MEGABYTE: Long = 1024 * 1024
        Firebase.storage.reference.child("banada.jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener { arr ->
            Glide.with(this)
                    .load(arr)
                    .into(imgView)
        }.addOnFailureListener { imgView.setImageResource(R.drawable.mainfood)}
    }
}