package com.example.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.CardViewBinding

class MyAdapter(var cartList:List<Meals>) : RecyclerView.Adapter<MyAdapter.CartViewHolder>() {
    class CartViewHolder(var binding:CardViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

    }
}