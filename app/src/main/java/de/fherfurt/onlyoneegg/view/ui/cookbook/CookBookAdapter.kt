package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemRecipeBinding
import de.fherfurt.onlyoneegg.model.Recipe

class CookBookAdapter : ListAdapter<Recipe, CookBookAdapter.ViewHolder>(RecipeDiffCallback()) {

    var tracker: SelectionTracker<Long>? = null
    var positionsSelected: MutableList<Int> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    fun getAllSelectedIds(): MutableList<Long> {
        var ids: MutableList<Long> = mutableListOf()

        positionsSelected.forEach { position ->
            ids.add(getItem(position).id)
        }
        positionsSelected = mutableListOf()
        return ids
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        tracker?.let {
            holder.bind(item, it.isSelected(position.toLong()))
        }
    }

    // If a new child is needed and there is no old one available for reuse
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Recipe, isActivated: Boolean = false) {
            binding.recipe = item
            itemView.isActivated = isActivated
            binding.executePendingBindings()
        }

        init {
            binding.root.setOnClickListener { v: View ->
                val position: Long? = binding.recipe?.id
                if (position != null) {
                    val action =
                        CookBookFragmentDirections.actionCookbookFragmentToRecipeFragment(position)
                    findNavController(binding.root).navigate(action)
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

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
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