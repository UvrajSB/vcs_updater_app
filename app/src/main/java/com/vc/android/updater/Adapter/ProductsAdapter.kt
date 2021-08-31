package com.vc.android.updater.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synnapps.carouselview.CarouselView
import com.vc.android.updater.DataClass.ProductInfo
import com.vc.android.updater.R

class ProductsAdapter: RecyclerView.Adapter<ProductsViewHolder>() {
    var ProductsArray : ArrayList<ProductInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_rv_item,parent,false)
        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        var CurrentProduct = ProductsArray[position]
        holder.name.text =CurrentProduct.Name
        holder.code.text = CurrentProduct.Code
        holder.stock.text = CurrentProduct.Stock
        holder.desc.text = CurrentProduct.Desc

       holder.carousel.setImageListener { position, imageView ->
           Glide.with(holder.itemView.context).load(CurrentProduct.imageArray!![position].toUri()).into(imageView)
       }
        holder.carousel.pageCount = CurrentProduct.imageArray!!.size

    }

    override fun getItemCount(): Int {
        return ProductsArray.size
    }

    fun updateUsers(Users: ArrayList<ProductInfo>) {
        ProductsArray.clear()
        ProductsArray.addAll(Users)
        notifyDataSetChanged()

    }
}
class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name = itemView.findViewById<TextView>(R.id.product_name)
    val code = itemView.findViewById<TextView>(R.id.product_code)
    val stock = itemView.findViewById<TextView>(R.id.product_stock)
    val desc= itemView.findViewById<TextView>(R.id.desc)
    val carousel = itemView.findViewById<CarouselView>(R.id.carouselViewItem)

}