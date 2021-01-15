import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentAddrecipeBinding
import de.fherfurt.onlyoneegg.model.Difficulty
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Measurement
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeAdapter
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeViewModel
import de.fherfurt.onlyoneegg.view.ui.recipe.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //initialize a cookbookId from nav args
        val args: AddRecipeFragmentArgs by navArgs()
        val cookbookId = args.cookbookId

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddrecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_addrecipe, container, false
        )
        // Get a reference to the application
        val application = requireNotNull(this.activity).application

        // Setup ViewModel
        val viewModelFactory = AddRecipeViewModelFactory(application)
        val addRecipeViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AddRecipeViewModel::class.java)

        binding.addRecipeViewModel = addRecipeViewModel
        binding.setLifecycleOwner(this)

        // Setup the recycler View for further usage
        val ingredients = ArrayList<Ingredient>()
        val adapter = AddRecipeAdapter(ingredients)
        binding.ingredientList.adapter = adapter
        binding.ingredientList.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        // ClickListener for adding a ingredient
        binding.addIngredientButton.setOnClickListener {
            val ingredient = Ingredient(id = 0)

            when {
                binding.editIngredientNameText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a ingredient name", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.editIngredientAmountText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a ingredient amount", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    ingredient.name = binding.editIngredientNameText.text.toString()
                    binding.editIngredientNameText.text.clear()

                    ingredient.value = binding.editIngredientAmountText.text.toString().toLong()
                    binding.editIngredientAmountText.text.clear()

                    ingredient.measurement =
                        Measurement.valueOf(binding.measurementSpinner.selectedItem.toString())

                    ingredients.add(ingredient)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        // Helper function for hiding the keyboard
        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // Set the SpinnerAdapter to our measurement enum
        binding.measurementSpinner.adapter = ArrayAdapter<Measurement>(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            Measurement.values()
        )

        // Set the SpinnerAdapter to our difficulty enum
        binding.difficultySpinner.adapter = ArrayAdapter<Difficulty>(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            Difficulty.values()
        )

        // ClickListener for recipe saving
        binding.saveRecipeButton.setOnClickListener {
            val recipe = Recipe()
            recipe.cookbookId = cookbookId

            when {
                binding.editRecipeNameText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a recipe name", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.editRecipeCooktime.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a recipe cooktime", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.editRecipeDescriptionText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a recipe description", Toast.LENGTH_SHORT)
                        .show()
                }
                ingredients.isEmpty() -> {
                    Toast.makeText(it.context, "Please add atleast one ingredient", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    recipe.name = binding.editRecipeNameText.text.toString()
                    recipe.cooktime = binding.editRecipeCooktime.text.toString().toFloat()
                    recipe.description = binding.editRecipeDescriptionText.text.toString()

                    recipe.difficulty =
                        Difficulty.valueOf(binding.difficultySpinner.selectedItem.toString())

                    // Write the recipe into the database and save its id
                    val recipeId = addRecipeViewModel.insertRecipe(recipe)

                    // Set recipeId in all ingredients to the actual id
                    for (ingredient in ingredients) {
                        ingredient.recipeId = recipeId
                    }
                    // Save all ingredients in the database
                    addRecipeViewModel.insertIngredientList(ingredients)

                    hideKeyboard(this.requireContext(), it)
                    // show a popup that the insertion worked
                    Toast.makeText(application.applicationContext, "Added Recipe", Toast.LENGTH_SHORT)
                        .show()
                    // navigate back to the cookbook
                    val action =
                        AddRecipeFragmentDirections.actionAddRecipeFragmentToCookbookFragment(cookbookId)
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        }

        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager

        return binding.root
    }

}