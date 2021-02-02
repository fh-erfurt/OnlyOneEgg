package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemCookbookBinding
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.view.ui.cookbook.CookBookFragmentDirections
/*
* Dashboard Recycle View implementation class
* is used to list all cookbooks of the application
*
* */
class DashboardAdapter : ListAdapter<Cookbook, DashboardAdapter.ViewHolder>(CookbookDiffCallback()) {

    var tracker: SelectionTracker<Long>? = null

    var positionsSelected: MutableList<Int> = mutableListOf()

    init {
        setHasStableIds(true)
    }
    fun getAllSelectedIds():MutableList<Long>{
        var ids: MutableList<Long> = mutableListOf()

        positionsSelected.forEach { position->
            ids.add(getItem(position).id)
        }
        positionsSelected=mutableListOf()
        return ids
    }
    override fun getItemId(position: Int): Long = position.toLong()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        tracker?.let {
            holder.bind(item, it.isSelected(position.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemCookbookBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Cookbook, isActivated: Boolean = false) {
            binding.cookbook = item
            itemView.isActivated = isActivated
            binding.executePendingBindings()
        }

        // set click listener to each cookbook
        // item as navigation component to list of recipes
        init{
            binding.root.setOnClickListener{ v: View ->
                val position : Long? = binding.cookbook?.id
                if(position != null) {
                    val action = DashboardFragmentDirections.actionDashboardFragmentToCookbookFragment(position)
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCookbookBinding.inflate(layoutInflater, parent, false)
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


class CookbookDiffCallback : DiffUtil.ItemCallback<Cookbook>() {

    override fun areItemsTheSame(oldItem: Cookbook, newItem: Cookbook): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Cookbook, newItem: Cookbook): Boolean {
        return oldItem == newItem
    }


}