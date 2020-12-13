
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentAddrecipeBinding
import de.fherfurt.onlyoneegg.model.Measurement
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeAdapter
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeViewModel
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddrecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_addrecipe, container, false
        )

        val application = requireNotNull(this.activity).application

        // database stuff
        val recipeDao = OOEDatabase.getInstance(application).recipeDao;
        val recipeRepository = RecipeRepository(recipeDao)
        val ingredientDao = OOEDatabase.getInstance(application).ingredientDao;
        val ingredientRepository = IngredientRepository(ingredientDao)


        val viewModelFactory = AddRecipeViewModelFactory(application)

        val addRecipeViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AddRecipeViewModel::class.java)

        binding.addRecipeViewModel = addRecipeViewModel
        binding.setLifecycleOwner(this)

        val adapter = AddRecipeAdapter()
        binding.ingredientList.adapter = adapter

        // Helper function for hiding the keyboard
        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // Set the SpinnerAdapter to our measurement enum
        binding.measurementSpinner.adapter = ArrayAdapter<Measurement>(this.requireContext(), android.R.layout.simple_list_item_1, Measurement.values())

        // ClickListener for recipe saving
        binding.saveRecipeButton.setOnClickListener {
            // save recipe in database
            var recipe = Recipe()
            recipe.name = binding.editRecipeNameText.toString()
            recipe.description = binding.editRecipeDescriptionText.toString()
            // cooktime
            // difficulty

            //recipeRepository.insert(recipe)
            // save all ingredients in the database with a reference to the recipe id

            hideKeyboard(this.requireContext(), it)
        }

        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager

        return binding.root
    }
}