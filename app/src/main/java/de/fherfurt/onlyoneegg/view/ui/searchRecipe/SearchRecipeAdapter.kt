package de.fherfurt.onlyoneegg.view.ui.searchRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemRecipeBinding
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.view.ui.cookbook.RecipeDiffCallback

class SearchRecipeAdapter(val recipeList: ArrayList<Recipe>) :
    ListAdapter<Recipe, SearchRecipeAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recipeList[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = recipeList.size

    class ViewHolder private constructor(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe) {
            binding.recipe = item
            binding.executePendingBindings()
        }

        init {
            binding.root.setOnClickListener { v: View ->
                val position: Long? = binding.recipe?.id
                if (position != null) {
                    val action =
                        SearchRecipeFragmentDirections.actionSearchRecipeFragmentToRecipeFragment(
                            position
                        )
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}