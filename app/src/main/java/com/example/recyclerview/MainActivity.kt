package com.example.recyclerview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MyAdapter

    val cartList = mutableListOf <Meals>(
        Meals("a1","b1","c1"),
        Meals("a2","b2","c2"),
        Meals("a3","b3","c3"),
        Meals("a4","b4","c4"),
        Meals("a5","b5","c5"),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



        adapter= MyAdapter(cartList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.save.setOnClickListener {
            val name = binding.name.text.toString()
            val mail = binding.mail.text.toString()
            val password = binding.password.text.toString()

            cartList.add(Meals(name,mail,password))
            adapter.notifyDataSetChanged()
        }
        binding.delete.setOnClickListener {
            val name = binding.name.text.toString()
            val mail = binding.mail.text.toString()
            val password = binding.password.text.toString()

            cartList.remove(Meals(name,mail,password))
            adapter.notifyDataSetChanged()
        }

    }

    private val itemTouchHelper: ItemTouchHelper by lazy {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Hareket ettirme işlemini burada ele al
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                Collections.swap(cartList, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Kaydırma işlemini burada ele al
                val position = viewHolder.adapterPosition
                showDeleteConfirmationDialog(position)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ACTION_STATE_DRAG){
                    viewHolder?.itemView?.apply {
                        alpha = 0.5f
                        scaleX=1.1f
                        scaleY = 1.1f
                    }
                }
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val background = ColorDrawable(Color.RED)
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.apply {
                    alpha = 1f
                    scaleX=1f
                    scaleY = 1f
                }
            }
        }
        ItemTouchHelper(itemTouchCallback)
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Alert Dialog")
            .setMessage("$position Silmek işlemini onaylıyor musunuz")
            .setCancelable(false)
            .setPositiveButton("Evet"){_,_ ->
                val silinenItem = cartList[position]
                cartList.removeAt(position)
                adapter.notifyItemRemoved(position)
                Snackbar.make(binding.root,"Silindi",Snackbar.LENGTH_LONG).apply {
                    setAction("Geri Al"){
                        cartList.add(position,silinenItem)
                        adapter.notifyItemInserted(position)
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