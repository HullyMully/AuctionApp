package com.example.auctionapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<AuctionProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.productsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter

        loadProductsFromDatabase()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.menu_main
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_main -> {
                    // Остаемся на MainActivity
                    true
                }
                R.id.menu_add_product -> {
                    // Переход на AddproductActivity
                    val intent = Intent(this, AddProductActivity::class.java)
                    startActivity(intent)
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

        val logoutButton: Button = findViewById(R.id.logoutButton)

        // Проверяем, есть ли авторизованный пользователь
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Если пользователь не авторизован, перенаправляем на LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Закрываем MainActivity, чтобы не вернуться сюда без входа
        } else {
            // Если пользователь авторизован, отображаем его email
            val userEmail = currentUser.email
            // Найди TextView с id emailTextView и установи почту пользователя
            findViewById<TextView>(R.id.emailTextView).text = "Logged in as: $userEmail"
        }

        // Логика выхода из аккаунта
        logoutButton.setOnClickListener {
            val database = Firebase.database

            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadProductsFromDatabase() {
        val database = FirebaseDatabase.getInstance().getReference("products")

        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productSnapshot in snapshot.children) {
                    Log.d("FirebaseData", productSnapshot.value.toString())
                    val product = productSnapshot.getValue(AuctionProduct::class.java)
                    Log.d("FirebaseData", product.toString())
                    product?.let { productList.add(it) }
                }
                productAdapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
