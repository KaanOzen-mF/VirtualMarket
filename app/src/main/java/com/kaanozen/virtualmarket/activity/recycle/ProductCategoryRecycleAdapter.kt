package com.kaanozen.virtualmarket.activity.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.model.ProductCategory
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_category_recycle_unit_view.*
import kotlinx.android.synthetic.main.activity_category_recycle_unit_view.view.*

class ProductCategoryRecycleAdapter() :  RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var items: ArrayList<ProductCategory> = ArrayList()
    private lateinit var listener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatalogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_category_recycle_unit_view, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CatalogViewHolder -> {holder.bind(items[position])}
        }
    }

    fun submitItems(productCategories : ArrayList<ProductCategory>)
    {
        this.items = productCategories
    }

    fun submitListener(listener_: OnItemClickListener)
    {
        this.listener = listener_
    }

    inner class CatalogViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        private val category_image: ImageView = itemView.category_image
        private val category_title: TextView = itemView.category_title

        fun bind(category: ProductCategory){

            val ONE_MEGABYTE: Long = 1024 * 1024
            Firebase.storage.reference.child(Constants.IMAGE_STORAGE_CATEGORY_PATH).child(category.id + ".png").getBytes(ONE_MEGABYTE).addOnSuccessListener { arr ->
                Glide.with(itemView)
                    .load(arr)
                    .into(category_image)
            }.addOnFailureListener { category_image.setImageResource(R.drawable.home_icon)}

            category_title.text = category.name
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
        fun OnItemClick(position: Int, item : ProductCategory)
    }
}