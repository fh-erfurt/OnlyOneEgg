package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.databinding.ItemCookbookBinding
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.view.ui.cookbook.CookBookFragmentDirections

class DashboardAdapter : ListAdapter<Cookbook, DashboardAdapter.ViewHolder>(CookbookDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    //wenn neues kind objekt notwendig ist ind keins anderes verfuegbar ist
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemCookbookBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Cookbook) {
            binding.cookbook = item
            binding.executePendingBindings()
        }
        init{
            binding.root.setOnClickListener{ v: View ->
                val position : Long? = binding.cookbook?.id
                if(position != null) {
                    val action = DashboardFragmentDirections.actionDashboardFragmentToCookbookFragment(position)
                    Navigation.findNavController(binding.root).navigate(action)
                }
                //Toast.makeText(itemView.context,"you clicked # ${position }",Toast.LENGTH_LONG).show()
            }
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


class CookbookDiffCallback : DiffUtil.ItemCallback<Cookbook>() {

    override fun areItemsTheSame(oldItem: Cookbook, newItem: Cookbook): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Cookbook, newItem: Cookbook): Boolean {
        return oldItem == newItem
    }


}