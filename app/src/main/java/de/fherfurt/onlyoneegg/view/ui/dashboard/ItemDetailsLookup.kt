package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

/*
* This is the class that will provide the selection library the information about the items
* associated with the users selection related to the dashboard fragment.
*  That selection is based on a MotionEvent that is mapped to the ViewHolders.
*
* */
class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as DashboardAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}