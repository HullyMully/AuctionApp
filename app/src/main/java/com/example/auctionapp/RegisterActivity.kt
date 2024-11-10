package com.example.auctionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private val adjectives =
        listOf("Rich", "Old", "Funny", "Young", "Brave", "Clever", "Quick", "Silent")
    private val animals = listOf("Owl", "Cat", "Dog", "Dolphin", "Tiger", "Bear", "Wolf", "Fox")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val auth = FirebaseAuth.getInstance()
        val database = Firebase.database

        val emailField: EditText = findViewById(R.id.emailField)
        val passwordField: EditText = findViewById(R.id.passwordField)
        val registerButton: Button = findViewById(R.id.registerButton)
        val loginButton: Button = findViewById(R.id.goToLoginButton)

        // Логика регистрации
        registerButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                            val nickname = generateNickname()
                            val user = auth.currentUser

                            if (user != null) {
                                val userId = user.uid
                                val userRef = database.getReference("users").child(userId)
                                userRef.child("nickname").setValue(nickname)
                            }

                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Вход успешен, переходим в главное меню
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Переход на экран входа
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun generateNickname(): String {
        val adjective = adjectives.random() // Выбираем случайное прилагательное
        val animal = animals.random() // Выбираем случайное животное
        return "$adjective $animal" // Возвращаем ник
    }
}
