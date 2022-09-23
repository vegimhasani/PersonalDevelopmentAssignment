package com.vegimhasani.bux.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vegimhasani.bux.R
import com.vegimhasani.bux.databinding.ProductRowBinding
import com.vegimhasani.bux.utils.Products

class ProductsAdapter(clickListener: (String) -> Unit) : RecyclerView.Adapter<ProductsAdapter.PriceViewHolder>() {

    val itemClickListener = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        return PriceViewHolder.newInstance(parent).listen { pos, _ ->
            val product = Products.values()[pos]
            itemClickListener.invoke(product.id)
        }
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val product = Products.values()[position]
        holder.update(product)
    }

    override fun getItemCount(): Int {
        return Products.values().size
    }


    class PriceViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ProductRowBinding.bind(itemView)

        fun update(products: Products) {
            binding.name.text = products.name
            binding.friendlyName.text = products.friendlyName
        }

        companion object {
            fun newInstance(parent: ViewGroup): PriceViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.product_row, parent, false)
                return PriceViewHolder(view)
            }
        }
    }
}

private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}
