package com.emporiumz.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.emporiumz.app.models.Product
import com.emporiumz.databinding.ItemProductBinding

@Suppress("DEPRECATION")
class ProductAdapter(private val listener: OnAddToCartListener) : RecyclerView.Adapter<ProductAdapter.VH>() {
    private val items = mutableListOf<Product>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Product>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    interface OnAddToCartListener { fun onAddToCart(sku:String) }

    inner class VH(val b: ItemProductBinding) : RecyclerView.ViewHolder(b.root) {
        init { b.btnAdd.setOnClickListener { val pos=adapterPosition; if(pos>=0) listener.onAddToCart(items[pos].sku) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.b.tvName.text = p.name
        holder.b.tvPrice.text = "$${p.price}"
        val imgUrl = p.img_url ?: "http://10.0.2.2:4000/imagenes/Camisapolo.jpg"
        holder.b.iv.load(imgUrl) { crossfade(true) }
    }
    override fun getItemCount() = items.size
}