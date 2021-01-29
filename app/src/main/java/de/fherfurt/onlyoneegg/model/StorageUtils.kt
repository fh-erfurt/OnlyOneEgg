package de.fherfurt.onlyoneegg.model

import android.content.Context
import android.os.Environment
import android.widget.Toast
import de.fherfurt.onlyoneegg.R
import java.io.File
import java.io.IOException

object StorageUtils {


    private fun createOrGetFile(
        destination: File,      // e.g., /storage/emulated/0/Android/data/
        fileName: String,       // e.g., tripBook.txt
        folderName: String
    )     // e.g., bookTrip
            : File {
        val folder = File(destination, folderName)
        // file path = /storage/emulated/0/Android/data/dh.fherfurt.onlyoneegg/files/documents/onlyoneei/
        return File(folder, fileName)
    }

    // ----------------------------------
    // READ & WRITE STORAGE
    // ----------------------------------
    private fun readFile(context: Context, file: File): String {

        val sb = StringBuilder()

        if (file.exists()) {
            try {
                val bufferedReader = file.bufferedReader();
                bufferedReader.useLines { lines ->
                    lines.forEach {
                        sb.append(it)
                        sb.append("\n")
                    }
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_happened),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return sb.toString()
    }

    // ---
    private fun writeFile(context: Context, text: String, file: File) {

        try {
            file.parentFile.mkdirs()
            file.bufferedWriter().use { out ->
                out.write(text)
            }
        } catch (e: IOException) {
            Toast.makeText(context, context.getString(R.string.error_happened), Toast.LENGTH_LONG)
                .show()
            return
        }

        Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_LONG).show()
    }

    fun getTextFromStorage(
        rootDestination: File,
        context: Context,
        fileName: String,
        folderName: String
    ): String? {
        val file = createOrGetFile(rootDestination, fileName, folderName)
        return readFile(context, file)
    }

    fun setTextInStorage(
        rootDestination: File,
        context: Context,
        fileName: String,
        folderName: String,
        text: String
    ) {
        val file = createOrGetFile(rootDestination, fileName, folderName)
        writeFile(context, text, file)
    }

    // ----------------------------------
    // EXTERNAL STORAGE
    // ----------------------------------

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state ||
                Environment.MEDIA_MOUNTED_READ_ONLY == state
    }
}