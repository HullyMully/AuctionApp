package com.example.auctionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()

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

            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")

            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
