package de.fherfurt.onlyoneegg.view.ui.dashboard


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentDashboardBinding
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* implementation class of dashboard fragment
*
* used for display all cookbooks of the application
* has connections to cookbookFragment and inputCookbookFragment
* */
class DashboardFragment : Fragment() {

    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // set the Fragment as only Portrait
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false
        )

        val application = requireNotNull(this.activity).application

        //add repository to viewModel
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao
        val recipeDao = OOEDatabase.getInstance(application).recipeDao

        val cookbookRepository = CookbookRepository(cookbookDao)
        val recipeRepository = RecipeRepository(recipeDao)

        // create view model for dashboard
        val dashboardViewModel =
            DashboardViewModel(application, cookbookRepository, recipeRepository)

        binding.dashboardViewModel = dashboardViewModel
        binding.setLifecycleOwner(this)

        // create recycle view for dashboard
        val adapter = DashboardAdapter()

        //click listener to navigate to addCookbookFragment
        binding.addCookbook.setOnClickListener { findNavController().navigate(R.id.action_dashboardFragment_to_inputCookbookFragment) }
        // click listener on remove all selected items
        binding.remove.setOnClickListener {
            val ids = adapter.getAllSelectedIds()
            dashboardViewModel.removeAllSelectedCookbooks(ids)
        }
        binding.cookbookList.adapter = adapter

        // using observer design pattern to track all cookbooks of the database
        dashboardViewModel.cookbooks.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        // create layout manager for dashboard recycle view
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.cookbookList.layoutManager = manager
        // set up multiple selection tracker
        setupTracker(adapter, binding)

        return binding.root
    }

    /*
    * Creates a tracker which tracks all selected items of dashboard recycle view
    *
    * */
    private fun setupTracker(adapter: DashboardAdapter, binding: FragmentDashboardBinding) {
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.cookbookList,
            StableIdKeyProvider(binding.cookbookList),
            MyItemDetailsLookup(binding.cookbookList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        addCallbacksToTracker(adapter, binding)

        adapter.tracker = tracker
    }

    /*
    * attaches callbacks to the dashboard tracker
    * */
    private fun addCallbacksToTracker(
        adapter: DashboardAdapter,
        binding: FragmentDashboardBinding
    ) {
        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val items = tracker?.selection!!.size()
                    // show or hide number of items and trash icon
                    if (items > 0) {
                        binding.remove.visibility = View.VISIBLE
                        binding.numberOfItemsSelected.visibility = View.VISIBLE
                        binding.numberOfItemsSelected.text = items.toString()
                    } else {
                        binding.remove.visibility = View.INVISIBLE
                        binding.numberOfItemsSelected.visibility = View.INVISIBLE
                    }
                    //reset selected list
                    adapter.positionsSelected = mutableListOf()
                    // save all new selected positions
                    tracker?.selection?.forEach { id ->
                        adapter.positionsSelected.add(id.toInt())
                    }
                }

                // updates selected list if items are deleted
                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    if (!tracker!!.hasSelection())
                        adapter.notifyDataSetChanged()
                    super.onItemStateChanged(key, !selected)

                }

                // removes all selected items from tracker selection and notifies the adapter
                override fun onSelectionRefresh() {
                    super.onSelectionRefresh()
                    tracker!!.clearSelection()
                    adapter.notifyDataSetChanged()
                }
            })
    }
}