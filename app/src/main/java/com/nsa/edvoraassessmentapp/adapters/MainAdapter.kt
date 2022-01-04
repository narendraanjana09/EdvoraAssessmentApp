package com.nsa.edvoraassessmentapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nsa.edvoraassessmentapp.databinding.MainRvItemBinding
import com.nsa.edvoraassessmentapp.models.ProductModel

class MainAdapter(private var list:HashMap<String, List<ProductModel>>,private var context: Context)
    : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=MainRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list.get(list.keys.elementAt(position)),list.keys.elementAt(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: MainRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(homeDataModel: List<ProductModel>?, elementAt: String) {
            Log.e("TAG", "setData: "+ homeDataModel!!.size )
            binding.apply {
                productNameTv.text=elementAt
                productRecyclerView.adapter= homeDataModel?.let { ProductsAdapter(it,context) }
            }
        }


    }
}