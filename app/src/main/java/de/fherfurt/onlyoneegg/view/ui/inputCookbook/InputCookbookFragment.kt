package de.fherfurt.onlyoneegg.view.ui.inputCookbook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.databinding.FragmentInputCookbookBinding
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class InputCookbookFragment : Fragment() {

    lateinit var cookbookNameEdit: EditText

    lateinit var binding: FragmentInputCookbookBinding

    lateinit var uri: String

    var addPhotoClicked=false;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_input_cookbook, container, false
        )

        val application = requireNotNull(this.activity).application

        //add repository to viewModel
        val cookbookDao = OOEDatabase.getInstance(application).cookbookDao
        val recipeDao = OOEDatabase.getInstance(application).recipeDao

        val cookbookRepository = CookbookRepository(cookbookDao)
        val viewModelFactory = InputCookbookViewModelFactory(application, cookbookRepository)
        // create viewModel
        val inputCookbookViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(InputCookbookViewModel::class.java)
        binding.inputCookbookViewModel = inputCookbookViewModel
        // bind input field with the variable
        cookbookNameEdit = binding.cookbookName

        // add click listener to insert new cookbook
        binding.doneAddCookbook.setOnClickListener {
            binding.apply {
                var cookbook = Cookbook()
                if (cookbookNameEdit.text.toString().isEmpty()) {
                    cookbook.name = "Cookbook " + inputCookbookViewModel.getLastCookbookId() + 1;
                } else {
                    cookbook.name = cookbookNameEdit.text.toString()
                }
                if(addPhotoClicked){
                    val uri = saveImageToInternalStorage(binding.imageCookbook.drawable)
                    cookbook.uri = uri.toString()
                }
                else{
                    cookbook.uri = ""
                }
                inputCookbookViewModel.insertCookbook(cookbook)
                hideKeyboard()
                findNavController().navigate(R.id.action_inputCookbookFragment_to_dashboardFragment)
            }
        }
        binding.setLifecycleOwner(this)


        //Add picture
        // Button Click

        binding.addPicture.setOnClickListener {

            // check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(it.context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                // system OS is <= Marshmallow
                pickImageFromGallery();
            }
        }
        return binding.root
    }
    // Method to save an image to internal storage
    private fun saveImageToInternalStorage(drawable: Drawable): Uri {
        // Get the bitmap from drawable object
        addPhotoClicked=true
        val bitmap = (drawable as BitmapDrawable).bitmap

        // Get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission Code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.imageCookbook.setImageURI(data?.data)
            uri = data?.data.toString()
        }
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

}