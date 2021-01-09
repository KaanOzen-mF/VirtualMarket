package com.kaanozen.virtualmarket.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Product
import kotlinx.android.synthetic.main.activity_product_page.*
import kotlinx.android.synthetic.main.activity_product_page.home_bottom_image_view
import kotlinx.android.synthetic.main.activity_product_page.shopping_cart_image_view
import kotlinx.android.synthetic.main.activity_product_page.user_bottom_logo_image_view

class ProductPageActivity : BaseActivity(),View.OnClickListener {

    private lateinit var product : Product

    override fun onCreate(savedInstanceState: Bundle?) {

        product = intent.extras!!.getParcelable<Product>("item")!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_page)

        val ONE_MEGABYTE: Long = 1024 * 1024
        Firebase.storage.reference.child("product").child(product.id + ".png").getBytes(ONE_MEGABYTE).addOnSuccessListener { arr ->
            Glide.with(this)
                .load(arr)
                .into(productImageView)
        }.addOnFailureListener { productImageView.setImageResource(R.drawable.mainfood)}

        numberpicker.maxValue = product.stock
        numberpicker.minValue = 1

        productNameView.text = product.name
        productInfoView.text = product.information
        productStockView.text = product.stock.toString()
        productPriceView.text = product.price.toString() + " TL"

        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
        shopping_cart_image_view.setOnClickListener(this)

    }

    override fun onClick(view: View?) {

        if(view!!.id == orderBut.id)
        {
            product.stock = product.stock - numberpicker.value
            FirestoreClass().addOrder(product,numberpicker.value,this)
            numberpicker.value = 1
            numberpicker.maxValue = product.stock
        }
        else
        {
            super.onClick(view)
        }
    }
}