package com.example.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.CardViewBinding

class MyAdapter() : RecyclerView.Adapter<MyAdapter.CartViewHolder>() {


    private val DifferUtilCallBack = object : DiffUtil.ItemCallback<Meals>() {
        override fun areItemsTheSame(oldItem: Meals, newItem: Meals): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meals, newItem: Meals): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, DifferUtilCallBack)


    class CartViewHolder(var binding:CardViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val meals = differ.currentList[position]
        holder.binding.name.text = meals.name
        holder.binding.mail.text = meals.mail
        holder.binding.password.text = meals.password

    }


    fun setData(list: List<Meals>){
        differ.submitList(list)
    }
    fun addItem(meals: Meals){
        val updateList = differ.currentList.toMutableList()
        updateList.add(meals)
        setData(updateList)
    }
    fun deleteItem(meals: Meals){
        val updateList = differ.currentList.toMutableList()
        updateList.remove(meals)
        setData(updateList)
    }
    fun updateItem(meals: Meals){
        val updateList = differ.currentList.toMutableList()
        val index = updateList.indexOf(meals)

        if (index != -1){
            updateList[index] = meals
            setData(updateList)
        }
    }
    fun returnGetItem(position: Int,meals: Meals){
        val updateList = differ.currentList.toMutableList()
        updateList.add(position,meals)
        setData(updateList)
    }
}