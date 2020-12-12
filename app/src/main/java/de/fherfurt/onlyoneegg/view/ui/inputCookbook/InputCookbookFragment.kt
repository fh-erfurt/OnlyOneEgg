package de.fherfurt.onlyoneegg.view.ui.inputCookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentInputCookbookBinding
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.view.ui.dashboard.DashboardAdapter
import de.fherfurt.onlyoneegg.view.ui.dashboard.DashboardViewModel
import de.fherfurt.onlyoneegg.view.ui.dashboard.DashboardViewModelFactory

class InputCookbookFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentInputCookbookBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_input_cookbook, container, false)

        val application = requireNotNull(this.activity).application

        //add repository to viewModel
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao
        val cookbookRepository= CookbookRepository(cookbookDao)
        val viewModelFactory = InputCookbookViewModelFactory(application, cookbookRepository)

        val inputCookbookViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(InputCookbookViewModel::class.java)

        binding.inputCookbookViewModel = inputCookbookViewModel

        binding.setLifecycleOwner(this)

        return binding.root

    }
}