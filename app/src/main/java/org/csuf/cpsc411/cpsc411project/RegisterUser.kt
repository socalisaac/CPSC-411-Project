package org.csuf.cpsc411.cpsc411project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class RegisterUser : AppCompatActivity() {

    fun reportRegistrationResult(res :Boolean) {
        val registerButton = findViewById<Button>(R.id.registerButton)
        val usernameMessage = findViewById<TextView>(R.id.usernameTakenMessage)

        if(res){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            usernameMessage.visibility = View.GONE
        }
        else
        {
            usernameMessage.visibility = View.VISIBLE
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val registerSubmitButton = findViewById<Button>(R.id.registerSubmitButton)

        val ipAddressInput2 = findViewById<EditText>(R.id.ipAddressFeild2)

        if(ipAddress != "")
            ipAddressInput2.setText(ipAddress)

        registerSubmitButton.setOnClickListener{

            val userName = findViewById<EditText>(R.id.registerUserName)
            val password = findViewById<EditText>(R.id.registerPassword)

            val user = User(userName.text.toString(), password.text.toString())

            var serverDB = ServerHandler()

            serverDB.registerUser(user, this)

        }
    }
}