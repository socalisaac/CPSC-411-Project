package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wrongLogin = findViewById<TextView>(R.id.wrongLoginTextView)

        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{

            val userName = findViewById<EditText>(R.id.userNameField)
            val password = findViewById<EditText>(R.id.passwordField)

            if(userName.text.toString() == "Admin" &&  password.text.toString() == "1234"){
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
            else{
                wrongLogin.visibility = View.VISIBLE
            }
        }
    }



}
