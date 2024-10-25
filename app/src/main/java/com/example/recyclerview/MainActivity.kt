package com.example.recyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MyAdapter

    val cartList = mutableListOf <Meals>(
        Meals(1,"a1","b1","c1"),
        Meals(2,"a2","b2","c2"),
        Meals(3,"a3","b3","c3"),
        Meals(4,"a4","b4","c4"),
        Meals(5,"a5","b5","c5"),

        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter= MyAdapter()
        binding.recyclerView.adapter = adapter
        adapter.differ.submitList(cartList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)



        binding.save.setOnClickListener {
            val max_id = adapter.differ.currentList.maxOfOrNull { it.id } ?: 0
            val id = max_id + 1
            val name = binding.name.text.toString()
            val mail = binding.mail.text.toString()
            val password = binding.password.text.toString()
            adapter.addItem(Meals(id,name,mail,password))
           Log.d("TAG", "onCreate: ${id}")
        }
        binding.delete.setOnClickListener {
          val id = binding.id.text.toString().toIntOrNull()
          val meals = adapter.differ.currentList.find { it.id == id }
            meals?.let {
                adapter.deleteItem(it)
            }
        }

    }

    private val itemTouchHelper : ItemTouchHelper by lazy {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                Collections.swap(cartList, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val kisi = adapter.differ.currentList[position]
                showDeleteConfirmationDialog(position,kisi)
            }

        }
        ItemTouchHelper(itemTouchCallback)
    }
    private fun showDeleteConfirmationDialog(position: Int, meals: Meals) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Alert Dialog")
            .setMessage(" Silmek işlemini onaylıyor musunuz")
            .setCancelable(false)
            .setPositiveButton("Evet"){_,_ ->
                adapter.deleteItem(meals)

                Snackbar.make(binding.root,"Silindi",Snackbar.LENGTH_LONG).apply {
                    setAction("Geri Al"){
                        adapter.returnGetItem( position,meals)
                    }

                }.show()
            }
            .setNegativeButton("Hayır"){_,_ ->
                adapter.notifyItemChanged(position)
            }
            .create()
        alertDialog.show()

    }
}