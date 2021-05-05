package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    fun reportValidationResult(res :Boolean) {
//        if (res) {
//            // display the text
//        }

        if(res){
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
        else{
            val wrongLogin = findViewById<TextView>(R.id.wrongLoginTextView)
            wrongLogin.visibility = View.VISIBLE
        } //userName.text.toString() == "Admin" &&  password.text.toString() == "1234"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wrongLogin = findViewById<TextView>(R.id.wrongLoginTextView)

        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{

            val userName = findViewById<EditText>(R.id.userNameField)
            val password = findViewById<EditText>(R.id.passwordField)

            loginButton.text = "Validating"
            loginButton.isEnabled = false


            val context = this

            var user = User(userName.text.toString(), password.text.toString())

            var serverDB = ServerHandler()

            var db = DataBaseHandler(context) //db.checkLogin(user)

            println("Calling Server fun")
            serverDB.checkServerLogin(user, this)

           // var testVal = serverDB.callBackReceived
//            var cont = true
//            while(cont)
//            {
//                if (serverDB.callBackReceived) {
//                    println("response comes back.")
//                    cont = false
//                }
//                //println("Here $testVal")
//            }

            if(serverDB.login){
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
            else{
                wrongLogin.visibility = View.VISIBLE
            } //userName.text.toString() == "Admin" &&  password.text.toString() == "1234"
        }

        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener{
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

    }



}
