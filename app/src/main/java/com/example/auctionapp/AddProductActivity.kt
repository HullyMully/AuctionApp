package com.example.auctionapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddProductActivity : AppCompatActivity() {

    private lateinit var productImageView: ImageView
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        CloudinaryManager.init(this) // Инициализация Cloudinary

        val productNameEditText = findViewById<EditText>(R.id.productNameEditText)
        val productDescriptionEditText = findViewById<EditText>(R.id.productDescriptionEditText)
        val productPriceEditText = findViewById<EditText>(R.id.productPriceEditText)
        productImageView = findViewById(R.id.productImageView)
        val uploadImageButton = findViewById<Button>(R.id.uploadImageButton)
        val addButton = findViewById<Button>(R.id.addProductButton)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.menu_add_product
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_main -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_add_product -> {
                    // Переход на AddproductActivity
                    true
                }
                R.id.menu_profile -> {
                    // Переход на ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Загрузка изображения
        uploadImageButton.setOnClickListener {
            openFileChooser()
        }

        // Кнопка "Добавить товар"
        addButton.setOnClickListener {
            val name = productNameEditText.text.toString().trim()
            val description = productDescriptionEditText.text.toString().trim()
            val price = productPriceEditText.text.toString().trim()

            if (name.isEmpty() || description.isEmpty() || price.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                uploadImageToCloudinary(name, description, price)
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            productImageView.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToCloudinary(name: String, description: String, price: String) {
        selectedImageUri?.let { uri ->
            CloudinaryManager.uploadImage(uri, { imageUrl ->
                // Сохранение товара в Firebase
                saveProductToDatabase(name, description, price, imageUrl)
            }, { error ->
                Toast.makeText(this, "Ошибка загрузки изображения: $error", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun saveProductToDatabase(name: String, description: String, price: String, imageUrl: String) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return
        val database = Firebase.database
        val productsRef = database.getReference("products")

        val productId = productsRef.push().key ?: return
        val product = Product(productId, userId, name, description, price.toDouble(), imageUrl)

        productsRef.child(productId).setValue(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Товар успешно добавлен!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка при добавлении товара", Toast.LENGTH_SHORT).show()
            }
    }
}

data class Product(
    val productId: String,
    val userId: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)
