package com.example.auctionapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val auth = FirebaseAuth.getInstance()
        val database = Firebase.database

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.menu_profile
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_main -> {
                    //Переходим на main
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                //Остаёмься тут
                R.id.menu_profile -> {
                    true
                }
                else -> false
            }
        }

        // Найдем TextView для отображения ника и установим его текст
        val user = auth.currentUser
        if(user != null) {
            val userId = user.uid
            val nicknameRef = database.getReference("users").child(userId).child("nickname")
            nicknameRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val nickname = snapshot.getValue<String>()
                    val nicknameTextView = findViewById<TextView>(R.id.nicknameTextView)
                    nicknameTextView.text = "@$nickname"

                    val profileImageView = findViewById<ImageView>(R.id.profile_icon)
                    if (nickname != null) {
                        val drawableRes = getAnimalDrawable(nickname)
                        profileImageView.setImageResource(drawableRes)
                    }
                    Log.d(TAG, "NICKNAME IS : " + nickname)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }

            })

        }
    }
    fun getAnimalDrawable(nickname: String): Int {
        return when {
            nickname.contains("Cat", ignoreCase = true) -> R.drawable.animal_cat
            nickname.contains("Dog", ignoreCase = true) -> R.drawable.animal_dog
            nickname.contains("Dolphin", ignoreCase = true) -> R.drawable.animal_dolphin
            nickname.contains("Tiger", ignoreCase = true) -> R.drawable.animal_tiger
            nickname.contains("Bear", ignoreCase = true) -> R.drawable.animal_bear
            nickname.contains("Wolf", ignoreCase = true) -> R.drawable.animal_wolf
            nickname.contains("Fox", ignoreCase = true) -> R.drawable.animal_fox
            nickname.contains("Owl", ignoreCase = true) -> R.drawable.animal_owl
            else -> R.drawable.person_24px // На случай, если животное не найдено
        }
    }

}
