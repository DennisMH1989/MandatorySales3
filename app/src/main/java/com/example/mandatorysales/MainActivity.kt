package com.example.mandatorysales

import android.os.Bundle
import android.text.InputType
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mandatorysales.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.mandatorysales.models.SalesItem
import com.example.mandatorysales.models.SalesViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val salesViewModel: SalesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        binding.fab.setOnClickListener { view ->
            showDialog()

        }
        salesViewModel.updateMessageLiveData.observe(this) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    Firebase.auth.signOut()
                    Snackbar.make(binding.root, "Signed out", Snackbar.LENGTH_LONG).show()
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.popBackStack(R.id.LoginFragment, false)
                } else {
                    Snackbar.make(binding.root, "Cannot sign out", Snackbar.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add SalesItem")

        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL

        val descriptionInputField = EditText(this)
        descriptionInputField.hint = "Description"
        descriptionInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(descriptionInputField)

        val priceInputField = EditText(this)
        priceInputField.hint = "Price"
        priceInputField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        layout.addView(priceInputField)

        val sellerPhoneInputField = EditText(this)
        sellerPhoneInputField.hint = "phone numbar"
        sellerPhoneInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(sellerPhoneInputField)

        val pictureUrlInputField = EditText(this)
        pictureUrlInputField.hint = "pictureUrl"
        pictureUrlInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(pictureUrlInputField)

        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, which ->
            val description = descriptionInputField.text.toString().trim()
            val priceStr = priceInputField.text.toString().trim()
            val sellerPhone = sellerPhoneInputField.text.toString().trim()
            val pictureUrl = pictureUrlInputField.text.toString().trim()
            when {
                description.isEmpty() ->
                    Snackbar.make(binding.root, "No description", Snackbar.LENGTH_LONG).show()
                description.isEmpty() -> Snackbar.make(binding.root, "No description", Snackbar.LENGTH_LONG)
                    .show()
                priceStr.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No price",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                else -> {
                    val salesItem = SalesItem(description, priceStr.toInt(), Firebase.auth.currentUser?.email?:"Cat@gmail.com", sellerPhone, System.currentTimeMillis()/1000, pictureUrl)
                    salesViewModel.add(salesItem)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }


}