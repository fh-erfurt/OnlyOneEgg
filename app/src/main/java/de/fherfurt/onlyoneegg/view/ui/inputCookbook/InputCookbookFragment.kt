package de.fherfurt.onlyoneegg.view.ui.inputCookbook

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentInputCookbookBinding
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase

class InputCookbookFragment : Fragment() {

    lateinit var cookbookNameEdit: EditText



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentInputCookbookBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_input_cookbook, container, false)

        val application = requireNotNull(this.activity).application

        //add repository to viewModel
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao
        val cookbookRepository = CookbookRepository(cookbookDao)
        val viewModelFactory = InputCookbookViewModelFactory(application, cookbookRepository)
        // create viewModel
        val inputCookbookViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(InputCookbookViewModel::class.java)
        binding.inputCookbookViewModel = inputCookbookViewModel
        // bind input field with the variable
        cookbookNameEdit = binding.cookbookName

        // add click listener to insert new cookbook
        binding.doneAddCookbook.setOnClickListener {
            binding.apply {
                var cookbook = Cookbook()
                if(cookbookNameEdit.text.toString().isEmpty()){
                    cookbook.name="Cookbook "+inputCookbookViewModel.getLastCookbookId()+1;
                }
                else{
                    cookbook.name = cookbookNameEdit.text.toString()
                }

                inputCookbookViewModel.insertCookbook(cookbook)
                hideKeyboard()
                findNavController().navigate(R.id.action_inputCookbookFragment_to_dashboardFragment)
            }


        }
        binding.setLifecycleOwner(this)

        return binding.root

    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

}