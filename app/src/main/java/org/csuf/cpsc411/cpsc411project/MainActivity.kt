package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    /*
        This function is called when the post message from the server is received.
        It it takes in a Boolean if its true then Login was good, if its false either IP address is
        wrong or login is bad.
        The message will print what the error is.
     */
    fun reportValidationResult(res :Boolean, message : String) {
        val loginButton = findViewById<Button>(R.id.loginButton)
        val wrongLogin = findViewById<TextView>(R.id.wrongLoginTextView)

        if(res){
            wrongLogin.visibility = View.INVISIBLE

            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
        else{
            wrongLogin.text = message
            wrongLogin.visibility = View.VISIBLE
        } //userName.text.toString() == "Admin" &&  password.text.toString() == "1234"

        loginButton.text = "Login"
        loginButton.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val ipAddressInput = findViewById<EditText>(R.id.ipAddressFeild)

        if(ipAddress != "")
            ipAddressInput.setText(ipAddress)


        loginButton.setOnClickListener{

            val userName = findViewById<EditText>(R.id.userNameField)
            val password = findViewById<EditText>(R.id.passwordField)


            ipAddress = ipAddressInput.text.toString()

            loginButton.text = "Validating"
            loginButton.isEnabled = false

            var user = User(userName.text.toString(), password.text.toString())

            var serverDB = ServerHandler()

            serverDB.checkServerLogin(user, this)
        }

        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener{
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

    }



}
