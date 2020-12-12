package de.fherfurt.onlyoneegg.view.ui.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.R
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.databinding.FragmentDashboardBinding
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase

class DashboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false)

        val application = requireNotNull(this.activity).application

        //add repository to viewModel
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao
        val cookbookRepository=CookbookRepository(cookbookDao)
        val viewModelFactory = DashboardViewModelFactory(application, cookbookRepository)

        val dashboardViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(DashboardViewModel::class.java)

        binding.dashboardViewModel = dashboardViewModel
        binding.setLifecycleOwner(this)


        val adapter = DashboardAdapter()
        binding.cookbookList.adapter = adapter

        dashboardViewModel.cookbooks.observe(viewLifecycleOwner, {
            it?.let{
                adapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.cookbookList.layoutManager = manager
        return binding.root

    }
}