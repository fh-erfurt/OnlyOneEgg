package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentCookbookBinding
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* Cookbook Recycle View implementation class
* is used to list all recipes of the certain cookbook
*
* */
class CookBookFragment : Fragment() {
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //initialize a cookbookId from nav args
        val args: CookBookFragmentArgs by navArgs()
        val cookbookId = args.cookbookId


        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCookbookBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cookbook, container, false
        )

        val application = requireNotNull(this.activity).application

        val recipeDao = OOEDatabase.getInstance(application).recipeDao
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao

        val recipeRepository = RecipeRepository(recipeDao)
        val cookbookRepository = CookbookRepository(cookbookDao)
        val viewModelFactory =
            CookBookViewModelFactory(application, recipeRepository, cookbookRepository, cookbookId)

        val cookbookViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CookBookViewModel::class.java)
        // click listener to navigate to the addRecipeFragment with cookbookId as argument
        binding.addRecipe.setOnClickListener {

            val action =
                CookBookFragmentDirections.actionCookbookFragmentToAddRecipeFragment2(cookbookId)
            Navigation.findNavController(binding.root).navigate(action)

        }

        val adapter = CookBookAdapter()
        binding.recipeList.adapter = adapter

        // click listener for removing of entire cookbook
        binding.removeCookbook.setOnClickListener {
            cookbookViewModel.removeCookbook(cookbookId, recipeRepository, cookbookRepository);
            findNavController().navigate(R.id.action_cookbookFragment_to_dashboardFragment)
        }

        // click listener for removing selected recipes
        binding.remove.setOnClickListener {
            var ids = adapter.getAllSelectedIds()
            cookbookViewModel.removeAllSelectedRecipes(ids)
        }

        binding.cookbookViewModel = cookbookViewModel
        /** binding.addRecipe.setOnClickListener { findNavController().navigate(R.id.action_cookbookFragment_to_inputrecipeFragment) } **/
        binding.setLifecycleOwner(this)

        cookbookViewModel.recipes.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recipeList.layoutManager = manager

        setupTracker(adapter, binding)

        return binding.root
    }

    private fun setupTracker(adapter: CookBookAdapter, binding: FragmentCookbookBinding) {
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.recipeList,
            StableIdKeyProvider(binding.recipeList),
            CookbookItemDetailsLookup(binding.recipeList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        addCallbacksToTracker(adapter, binding)
        adapter.tracker = tracker
    }

    private fun addCallbacksToTracker(adapter: CookBookAdapter, binding: FragmentCookbookBinding) {
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

                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    if (!tracker!!.hasSelection())
                        adapter.notifyDataSetChanged()
                    super.onItemStateChanged(key, !selected)
                }

                override fun onSelectionRefresh() {
                    super.onSelectionRefresh()
                    tracker!!.clearSelection()
                    adapter.notifyDataSetChanged()
                }
            }
        )
    }
}
