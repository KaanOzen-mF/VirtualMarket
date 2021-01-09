package com.kaanozen.virtualmarket.activity.recycle

import android.app.ActivityManager
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.MainActivity
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.utilies.Constants
import io.grpc.Context
import kotlinx.android.synthetic.main.activity_product_recycle_unit_view.view.*

class ProductRecycleAdapter:  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<Product> = ArrayList()
    private lateinit var listener : ProductRecycleAdapter.OnItemClickListener

    fun submitItems(products : ArrayList<Product>) {
        this.items = products
    }

    fun submitListener(listener_: ProductRecycleAdapter.OnItemClickListener) {
        this.listener = listener_
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatalogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_product_recycle_unit_view, parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ProductRecycleAdapter.CatalogViewHolder -> {holder.bind(items[position])}
        }
    }

    inner class CatalogViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        private val product_image: ImageView = itemView.product_image
        private val product_title: TextView = itemView.product_title

        fun bind(product: Product){
            val ONE_MEGABYTE: Long = 1024 * 1024
            Firebase.storage.reference.child(Constants.IMAGE_STORAGE_PRODUCT_PATH).child(product.id + ".png").getBytes(ONE_MEGABYTE).addOnSuccessListener { arr ->
                Glide.with(itemView)
                    .load(arr)
                    .into(product_image)
            }.addOnFailureListener { product_image.setImageResource(R.drawable.water)}

            product_title.text = product.name
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.OnItemClick(adapterPosition, items[adapterPosition])
        }
    }

    interface OnItemClickListener
    {
        fun OnItemClick(position: Int, item : Product)
    }
}

