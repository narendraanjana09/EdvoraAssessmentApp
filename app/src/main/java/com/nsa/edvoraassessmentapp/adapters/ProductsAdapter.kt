package com.nsa.edvoraassessmentapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nsa.edvoraassessmentapp.R
import com.nsa.edvoraassessmentapp.databinding.MainRvItemBinding
import com.nsa.edvoraassessmentapp.databinding.ProductRvItemBinding
import com.nsa.edvoraassessmentapp.models.HomeDataModel
import com.nsa.edvoraassessmentapp.models.ProductModel

class ProductsAdapter(private var list: List<ProductModel>, private var context: Context)
    : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=ProductRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(productModel: ProductModel) {
            binding.apply {
               productNameTV.text=productModel.product_name
                brandNameTV.text=productModel.brand_name
                priceTV.text="$ "+productModel.price

                dateTV.text=GetDate.getDate(productModel.date)
                locationTV.text=productModel.address.city+", "+productModel.address.state
                descriptionTV.text=productModel.discription


                Glide.with(itemView)
                    .load(productModel.image)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

            }
        }


    }
}