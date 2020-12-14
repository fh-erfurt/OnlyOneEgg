package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentCookbookBinding


class CookBookFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding : FragmentCookbookBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_cookbook, container, false)


        val application = requireNotNull(this.activity).application


        val viewModelFactory = CookBookViewModelFactory(application)

        val cookbookViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(CookBookViewModel::class.java)

        binding.addRecipe.setOnClickListener { findNavController().navigate(R.id.action_cookbookFragment_to_addRecipeFragment2) }

        binding.cookbookViewModel = cookbookViewModel
        binding.setLifecycleOwner(this)
        return binding.root


    }

}
