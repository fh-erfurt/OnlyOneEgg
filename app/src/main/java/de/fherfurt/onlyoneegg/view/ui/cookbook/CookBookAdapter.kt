package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import de.fherfurt.onlyoneegg.databinding.ItemRecipeBinding
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

    class ViewHolder private constructor(val binding: ItemRecipeBinding ) : RecyclerView.ViewHolder(binding.root){


        fun bind(item: Recipe) {
            binding.recipe = item
            binding.executePendingBindings()
        }
        init{
            binding.root.setOnClickListener{ v: View ->
                val position : Long? = binding.recipe?.id
                if(position != null) {
                    val action = CookBookFragmentDirections.actionCookbookFragmentToRecipeFragment(position)
                        findNavController( binding.root).navigate(action)
                }
                //Toast.makeText(itemView.context,"you clicked # ${position }",Toast.LENGTH_LONG).show()
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

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }


}