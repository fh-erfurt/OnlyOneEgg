package de.fherfurt.onlyoneegg.view.ui.inputRecipe

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
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentInputRecipeBinding
import de.fherfurt.onlyoneegg.model.Difficulty
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Measurement
import de.fherfurt.onlyoneegg.model.Recipe

/*
* Fragment for adding a new Recipe to the clicked cookbook
* Letting the user input all the information needed
* including all the ingredients
* */
class InputRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // set the Fragment as only Portrait
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        //initialize a cookbookId from nav args
        val args: InputRecipeFragmentArgs by navArgs()
        val cookbookId = args.cookbookId

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentInputRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_input_recipe, container, false
        )
        // Get a reference to the application
        val application = requireNotNull(this.activity).application

        // Setup ViewModel
        val inputRecipeViewModel = InputRecipeViewModel(application)
        binding.inputRecipeViewModel = inputRecipeViewModel
        binding.lifecycleOwner = this

        // Setup the recycler View for further usage
        val ingredients = ArrayList<Ingredient>()
        val adapter = InputRecipeAdapter(ingredients)
        binding.ingredientList.adapter = adapter
        binding.ingredientList.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        // ClickListener for adding a ingredient
        binding.addIngredientButton.setOnClickListener {
            val ingredient = Ingredient(id = 0)

            when {
                // No name was given
                binding.editIngredientNameText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a ingredient name", Toast.LENGTH_SHORT)
                        .show()
                }
                // No amount was given
                binding.editIngredientAmountText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(
                        it.context,
                        "Please input a ingredient amount",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> { // user has written everything -> save the ingredient and empty the fields for the next input
                    ingredient.name = binding.editIngredientNameText.text.toString()
                    binding.editIngredientNameText.text.clear()

                    ingredient.value = binding.editIngredientAmountText.text.toString().toLong()
                    binding.editIngredientAmountText.text.clear()

                    ingredient.measurement =
                        Measurement.valueOf(binding.measurementSpinner.selectedItem.toString())

                    ingredients.add(ingredient)
                    // update the recycler view of ingredients
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
                // No name was given
                binding.editRecipeNameText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a recipe name", Toast.LENGTH_SHORT)
                        .show()
                }
                // No cooktime was given
                binding.editRecipeCooktime.text.toString().trim().isEmpty() -> {
                    Toast.makeText(it.context, "Please input a recipe cooktime", Toast.LENGTH_SHORT)
                        .show()
                }
                // No description was given
                binding.editRecipeDescriptionText.text.toString().trim().isEmpty() -> {
                    Toast.makeText(
                        it.context,
                        "Please input a recipe description",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                // No ingredient was added
                ingredients.isEmpty() -> {
                    Toast.makeText(
                        it.context,
                        "Please add at least one ingredient",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> { // user has written everything -> save the whole recipe and redirect to the cookbook
                    recipe.name = binding.editRecipeNameText.text.toString()
                    recipe.cooktime = binding.editRecipeCooktime.text.toString().toFloat()
                    recipe.description = binding.editRecipeDescriptionText.text.toString()

                    recipe.difficulty =
                        Difficulty.valueOf(binding.difficultySpinner.selectedItem.toString())

                    // Write the recipe into the database and save its id
                    val recipeId = inputRecipeViewModel.insertRecipe(recipe)

                    // Set recipeId in all ingredients to the actual id
                    for (ingredient in ingredients) {
                        ingredient.recipeId = recipeId
                    }
                    // Save all ingredients in the database
                    inputRecipeViewModel.insertIngredientList(ingredients)

                    hideKeyboard(this.requireContext(), it)
                    // show a popup that the insertion worked
                    Toast.makeText(
                        application.applicationContext,
                        "Added Recipe",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    // navigate back to the cookbook
                    val action =
                        InputRecipeFragmentDirections.actionAddRecipeFragmentToCookbookFragment(
                            cookbookId
                        )
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        }

        // Set the Layout for the recyclerView
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.ingredientList.layoutManager = manager

        return binding.root
    }
}