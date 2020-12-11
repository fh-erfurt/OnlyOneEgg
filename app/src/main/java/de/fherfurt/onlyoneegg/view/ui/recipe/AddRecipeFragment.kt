
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentAddrecipeBinding
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeAdapter
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeViewModel
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddrecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_addrecipe, container, false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = AddRecipeViewModelFactory(application)

        val addRecipeViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AddRecipeViewModel::class.java)

        binding.addRecipeViewModel = addRecipeViewModel
        binding.setLifecycleOwner(this)

        val adapter = AddRecipeAdapter()
        binding.ingredientList.adapter = adapter


        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager

        return binding.root
    }
}