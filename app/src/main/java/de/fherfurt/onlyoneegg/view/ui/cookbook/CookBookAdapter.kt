package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemCookbookBinding
import de.fherfurt.onlyoneegg.model.Recipe


class CookBookAdapter : ListAdapter<Recipe, CookBookAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    //wenn neues kind objekt notwendig ist ind keins anderes verfuegbar ist
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemCookbookBinding ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Recipe) {
            binding.recipe = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCookbookBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }


    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }


}