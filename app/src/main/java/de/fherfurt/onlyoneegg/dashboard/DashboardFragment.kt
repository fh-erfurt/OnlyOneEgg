package de.fherfurt.onlyoneegg.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import de.fherfurt.onlyoneegg.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false)

        val application = requireNotNull(this.activity).application


        val viewModelFactory = DashboardViewModelFactory(application)

        val dashboardViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(DashboardViewModel::class.java)

        binding.dashboardViewModel = dashboardViewModel
        binding.setLifecycleOwner(this)


        val adapter = DashboardAdapter()
        binding.cookbookList.adapter = adapter

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.cookbookList.layoutManager = manager
        return binding.root



    }
}