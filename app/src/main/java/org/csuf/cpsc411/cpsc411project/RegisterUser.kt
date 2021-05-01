package org.csuf.cpsc411.cpsc411project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val registerSubmitButton = findViewById<Button>(R.id.registerSubmitButton)

        registerSubmitButton.setOnClickListener{

            val userName = findViewById<EditText>(R.id.registerUserName)
            val password = findViewById<EditText>(R.id.registerPassword)

            val user = User(userName.text.toString(), password.text.toString())
            val db = DataBaseHandler(this)

            val registerSuccessful = db.insertUser(user)

            if(registerSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }
}