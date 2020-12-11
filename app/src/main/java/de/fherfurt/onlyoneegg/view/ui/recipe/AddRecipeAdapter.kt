package de.fherfurt.onlyoneegg.view.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemIngredientBinding
import de.fherfurt.onlyoneegg.model.Ingredient

class AddRecipeAdapter : ListAdapter<Ingredient, AddRecipeAdapter.ViewHolder>(IngredientDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Ingredient) {
            binding.ingredient
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemIngredientBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {

    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}