package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.google.gson.reflect.TypeToken
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentCookbookBinding
import de.fherfurt.onlyoneegg.export.ExportIngredient
import de.fherfurt.onlyoneegg.export.ExportRecipe
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe
import de.fherfurt.onlyoneegg.model.StorageUtils
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.IngredientRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* Cookbook Recycle View implementation class
* is used to list all recipes of the certain cookbook
*
* */
class CookbookFragment : Fragment() {

    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // set the Fragment as only Portrait
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //initialize a cookbookId from nav args
        val args: CookbookFragmentArgs by navArgs()
        val cookbookId = args.cookbookId


        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCookbookBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cookbook, container, false
        )

        //instantiate all classes needed

        val application = requireNotNull(this.activity).application
        val ingredientDao = OOEDatabase.getInstance(application).ingredientDao
        val recipeDao = OOEDatabase.getInstance(application).recipeDao
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao

        val recipeRepository = RecipeRepository(recipeDao)
        val ingredientRepository = IngredientRepository(ingredientDao)
        val cookbookRepository = CookbookRepository(cookbookDao)


        val cookbookViewModel =
            CookbookViewModel(application, recipeRepository, cookbookRepository, cookbookId)

        // click listener to navigate to the addRecipeFragment with cookbookId as argument
        binding.addRecipe.setOnClickListener {

            val action =
                CookbookFragmentDirections.actionCookbookFragmentToAddRecipeFragment2(cookbookId)
            Navigation.findNavController(binding.root).navigate(action)

        }

        val adapter = CookbookAdapter()
        binding.recipeList.adapter = adapter


        // set options menu click listener
        binding.btnPopUpMenu.setOnClickListener {

            val popup = PopupMenu(this.context, binding.btnPopUpMenu)
            //Inflating the Popup using xml file

            // no export if no items
            if (cookbookViewModel.recipes.value?.size == 0) {
                popup.menuInflater.inflate(R.menu.options_menu_cookbook_no_export, popup.menu)
            } else {
                popup.menuInflater.inflate(R.menu.options_menu_cookbook, popup.menu)

            }
            // listeners for options button
            popup.setOnMenuItemClickListener {
                // click Listener to import a cookbook with the all Recipes inside
                if (it.itemId == R.id.action_import) {
                    import(cookbookRepository, cookbookId, recipeRepository, ingredientRepository)

                }
                // click Listener to export a cookbook with the Recipes inside
                else if (it.itemId == R.id.action_export) {
                    export(cookbookViewModel, cookbookId, cookbookRepository, ingredientRepository)

                }
                // click listener for removing of entire cookbook
                else {
                    remove(cookbookViewModel, cookbookId, recipeRepository, cookbookRepository)
                }
                true
            }

            popup.show()//showing popup menu

        }


        // click listener for removing selected recipes
        binding.remove.setOnClickListener {
            var ids = adapter.getAllSelectedIds()
            cookbookViewModel.removeAllSelectedRecipes(ids)
        }
        val cookbook = cookbookRepository.getCookbook(cookbookId)
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

    private fun setupTracker(adapter: CookbookAdapter, binding: FragmentCookbookBinding) {
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

    private fun addCallbacksToTracker(adapter: CookbookAdapter, binding: FragmentCookbookBinding) {
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
                        binding.btnPopUpMenu.visibility = View.INVISIBLE
                        binding.cookbookText.visibility = View.INVISIBLE
                    } else {
                        binding.remove.visibility = View.INVISIBLE
                        binding.numberOfItemsSelected.visibility = View.INVISIBLE
                        binding.btnPopUpMenu.visibility = View.VISIBLE
                        binding.cookbookText.visibility = View.VISIBLE
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

    fun import(
        cookbookRepository: CookbookRepository, cookbookId: Long,
        recipeRepository: RecipeRepository,
        ingredientRepository: IngredientRepository
    ) {
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
            
            if (recipeList != null) {

                if (!recipeList.isEmpty()) {

                    Log.d("Import", recipeList.isEmpty().toString())

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

                    Toast.makeText(
                        this.context, getString(R.string.imported),
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    Toast.makeText(
                        this.context, getString(R.string.Error),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }


        }


    }


    fun export(
        cookbookViewModel: CookbookViewModel, cookbookId: Long,
        cookbookRepository: CookbookRepository,
        ingredientRepository: IngredientRepository
    ) {
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

        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(recipeList)


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

    fun remove(
        cookbookViewModel: CookbookViewModel,
        cookbookId: Long,
        recipeRepository: RecipeRepository,
        cookbookRepository: CookbookRepository
    ) {
        cookbookViewModel.removeCookbook(
            cookbookId,
            recipeRepository,
            cookbookRepository
        );
        findNavController().navigate(R.id.action_cookbookFragment_to_dashboardFragment)
    }
}
