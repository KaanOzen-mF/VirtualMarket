package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.Product
import kotlinx.android.synthetic.main.activity_product_page.*

class ProductPageActivity : BaseActivity(),View.OnClickListener {

    private lateinit var imgView : ImageView
    private lateinit var tvProductName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        val product : Product = intent.extras!!.getParcelable<Product>("item")!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_page)

        imgView = findViewById<ImageView>(R.id.productImageView)
        tvProductName = findViewById<TextView>(R.id.productNameView)

        val ONE_MEGABYTE: Long = 1024 * 1024
        Firebase.storage.reference.child("product").child(product.id + ".png").getBytes(ONE_MEGABYTE).addOnSuccessListener { arr ->
            Glide.with(this)
                    .load(arr)
                    .into(imgView)
        }.addOnFailureListener { imgView.setImageResource(R.drawable.mainfood)}

        tvProductName.text = product.name

        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if( view != null){

            when(view.id){
                R.id.home_bottom_image_view ->{
                    BaseActivity.depth = 0
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.user_bottom_logo_image_view ->{
                    val intent = Intent(this,UserProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}