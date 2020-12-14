package de.fherfurt.onlyoneegg.view.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemIngredientBinding
import de.fherfurt.onlyoneegg.model.Ingredient

class AddRecipeAdapter(val ingredientList: ArrayList<Ingredient>) : ListAdapter<Ingredient, AddRecipeAdapter.ViewHolder>(IngredientDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = ingredientList[position]
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = ingredientList.size

    class ViewHolder private constructor(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Ingredient) {
            binding.ingredient = item
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