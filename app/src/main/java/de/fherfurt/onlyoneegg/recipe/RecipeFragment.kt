package de.fherfurt.onlyoneegg.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.dashboard.DashboardViewModel
import de.fherfurt.onlyoneegg.dashboard.DashboardViewModelFactory
import de.fherfurt.onlyoneegg.databinding.FragmentDashboardBinding
import de.fherfurt.onlyoneegg.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding : FragmentRecipeBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_recipe, container, false)


        val application = requireNotNull(this.activity).application


        val viewModelFactory = RecipeViewModelFactory(application)

        val recipeViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(RecipeViewModel::class.java)

        binding.recipeViewModel = recipeViewModel
        binding.setLifecycleOwner(this)
        return binding.root


    }

}