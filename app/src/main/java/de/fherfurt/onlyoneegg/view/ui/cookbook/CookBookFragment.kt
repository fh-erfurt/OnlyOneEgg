package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentCookbookBinding
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository


class CookBookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //initialize a cookbookId from nav args
        val args: CookBookFragmentArgs by navArgs()
        val cookbookId = args.cookbookId

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCookbookBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cookbook, container, false
        )

        val application = requireNotNull(this.activity).application

        val recipeDao = OOEDatabase.getInstance(application).recipeDao
        val recipeRepository = RecipeRepository(recipeDao)
        val viewModelFactory = CookBookViewModelFactory(application, recipeRepository, cookbookId)

        val cookbookViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CookBookViewModel::class.java)

        binding.addRecipe.setOnClickListener { findNavController().navigate(R.id.action_cookbookFragment_to_addRecipeFragment2) }


        binding.cookbookViewModel = cookbookViewModel
        /** binding.addRecipe.setOnClickListener { findNavController().navigate(R.id.action_cookbookFragment_to_inputrecipeFragment) } **/
        binding.setLifecycleOwner(this)

        val adapter = CookBookAdapter()
        binding.recipeList.adapter = adapter

        cookbookViewModel.recipes.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recipeList.layoutManager = manager
        context?.toast(cookbookId.toString())
        return binding.root


    }
    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
