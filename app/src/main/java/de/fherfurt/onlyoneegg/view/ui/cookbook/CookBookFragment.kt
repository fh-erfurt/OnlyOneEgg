package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentCookbookBinding
import de.fherfurt.onlyoneegg.model.*

import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* Cookbook Recycle View implementation class
* is used to list all recipes of the certain cookbook
*
* */
class CookBookFragment : Fragment() {

    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        //initialize a cookbookId from nav args
        val args: CookBookFragmentArgs by navArgs()
        val cookbookId = args.cookbookId


        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCookbookBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cookbook, container, false
        )

        val application = requireNotNull(this.activity).application
        val ingredientDao = OOEDatabase.getInstance(application).ingredientDao
        val recipeDao = OOEDatabase.getInstance(application).recipeDao
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao

        val recipeRepository = RecipeRepository(recipeDao)
        val ingredientRepository = IngredientRepository(ingredientDao)
        val cookbookRepository = CookbookRepository(cookbookDao)


        val cookbookViewModel =CookBookViewModel(application, recipeRepository, cookbookRepository, cookbookId)

        // click listener to navigate to the addRecipeFragment with cookbookId as argument
        binding.addRecipe.setOnClickListener {

            val action =
                CookBookFragmentDirections.actionCookbookFragmentToAddRecipeFragment2(cookbookId)
            Navigation.findNavController(binding.root).navigate(action)

        }

        val adapter = CookBookAdapter()
        binding.recipeList.adapter = adapter

        // click listener for removing of entire cookbook
        binding.removeCookbook.setOnClickListener {
            cookbookViewModel.removeCookbook(cookbookId, recipeRepository, cookbookRepository);
            findNavController().navigate(R.id.action_cookbookFragment_to_dashboardFragment)
        }


        binding.exportRecipes.setOnClickListener {
            var recipes: List<Recipe> =
                cookbookViewModel.recipeRepository.getAllRecipesFromCertainCookbookList(cookbookId);
            var recipeList = ArrayList<ExportRecipe>()

            recipes.forEach {
                var ingredientList = ArrayList<ExportIngredient>()
                val ingredients: List<Ingredient> =
                    ingredientRepository.getAllIngredientsFromCertainRecipeList(it.id)
                ingredients.forEach {
                    val exportIngredient: ExportIngredient = ExportIngredient(it)
                    ingredientList.add(exportIngredient)
                }
                val exportRecipe: ExportRecipe = ExportRecipe(it, ingredientList)
                recipeList.add(exportRecipe)
            }
            println(recipeList)

            val jsonObject: String = Gson().toJson(recipeList)
            val parser = JsonParser()
            val json: JsonObject = parser.parse(jsonObject).getAsJsonObject()

            val gson = GsonBuilder().setPrettyPrinting().create()
            val prettyJson = gson.toJson(json)


            if (StorageUtils.isExternalStorageWritable()) {

                context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.let {
                    this.context?.let { it1 ->
                        StorageUtils.setTextInStorage(
                            it,
                            it1,

                            cookbookRepository.getCookbook().name + "RecipeList.json",
                            cookbookRepository.getCookbook().name,
                            prettyJson
                        )

                    }
                }

            } else {

                Toast.makeText(
                    this.context, getString(R.string.external_storage_impossible_create_file),
                    Toast.LENGTH_LONG
                ).show()

            }
        }


        binding.importRecipes.setOnClickListener {
            var gson = Gson()
            val recipe = Recipe()

            // to import a Json
            if (StorageUtils.isExternalStorageReadable()) {
                // EXTERNAL

                var recipeList: String? = this.context?.let {
                    context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.let { it1 ->
                        StorageUtils.getTextFromStorage(
                            it1,
                            it,
                            cookbookRepository.getCookbook().name + "RecipeList.json",
                            cookbookRepository.getCookbook().name
                        )
                    }
                }
                val arrayRecipe = object : TypeToken<Array<ExportRecipe>>() {}.type

                var recipes: Array<ExportRecipe> = gson.fromJson(recipeList, arrayRecipe)
                recipes.forEachIndexed { idx, rec ->
                    recipe.name = rec.name
                    recipe.cookbookId = cookbookId
                    recipe.cooktime = rec.cooktime
                    recipe.description = rec.description
                    recipe.difficulty = rec.difficulty
                    val recipeId = recipeRepository.insert(recipe)

                    rec.ingredient.forEach {
                        val ingredient = Ingredient()
                        ingredient.measurement = it.measurement
                        ingredient.name = it.name
                        ingredient.recipeId = recipeId
                        ingredient.value = it.value

                        ingredientRepository.insert(ingredient)
                    }

                }

            } else {
                println("The file was not found")
            }
        }




        // click listener for removing selected recipes
        binding.remove.setOnClickListener {
            var ids = adapter.getAllSelectedIds()
            cookbookViewModel.removeAllSelectedRecipes(ids)
        }
        val cookbook = cookbookRepository.getCookbook()
        binding.cookbookText.text = cookbook.name


        binding.cookbookViewModel = cookbookViewModel
        /** binding.addRecipe.setOnClickListener { findNavController().navigate(R.id.action_cookbookFragment_to_inputrecipeFragment) } **/
        binding.setLifecycleOwner(this)

        cookbookViewModel.recipes.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recipeList.layoutManager = manager

        setupTracker(adapter, binding)

        return binding.root



    }

    private fun setupTracker(adapter: CookBookAdapter, binding: FragmentCookbookBinding) {
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.recipeList,
            StableIdKeyProvider(binding.recipeList),
            CookbookItemDetailsLookup(binding.recipeList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        addCallbacksToTracker(adapter, binding)
        adapter.tracker = tracker
    }

    private fun addCallbacksToTracker(adapter: CookBookAdapter, binding: FragmentCookbookBinding) {
        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val items = tracker?.selection!!.size()

                    // show or hide number of items and trash icon
                    if (items > 0) {
                        binding.remove.visibility = View.VISIBLE
                        binding.numberOfItemsSelected.visibility = View.VISIBLE
                        binding.numberOfItemsSelected.text = items.toString()
                    } else {
                        binding.remove.visibility = View.INVISIBLE
                        binding.numberOfItemsSelected.visibility = View.INVISIBLE
                    }

                    //reset selected list
                    adapter.positionsSelected = mutableListOf()
                    // save all new selected positions
                    tracker?.selection?.forEach { id ->
                        adapter.positionsSelected.add(id.toInt())
                    }
                }

                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    if (!tracker!!.hasSelection())
                        adapter.notifyDataSetChanged()
                    super.onItemStateChanged(key, !selected)
                }

                override fun onSelectionRefresh() {
                    super.onSelectionRefresh()
                    tracker!!.clearSelection()
                    adapter.notifyDataSetChanged()
                }
            }
        )
    }
}
