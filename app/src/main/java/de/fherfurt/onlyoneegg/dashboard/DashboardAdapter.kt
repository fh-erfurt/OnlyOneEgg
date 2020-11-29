package de.fherfurt.onlyoneegg.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemDashboardBinding

class DashboardAdapter : ListAdapter<Cookbook, DashboardAdapter.ViewHolder>(CookbookDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Cookbook) {
            binding.cookbook = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDashboardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class CookbookDiffCallback : DiffUtil.ItemCallback<Cookbook>() {

    override fun areItemsTheSame(oldItem: Cookbook, newItem: Cookbook): Boolean {
        return true//oldItem.nightId == newItem.nightId
    }


    override fun areContentsTheSame(oldItem: Cookbook, newItem: Cookbook): Boolean {
        return oldItem == newItem
    }


}