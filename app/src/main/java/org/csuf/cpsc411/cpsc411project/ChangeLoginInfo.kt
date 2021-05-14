package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class ChangeLoginInfo : AppCompatActivity() {

    private var userIdNumber = -1

    fun checkLoginInfo(id : Int){

        val userNameField2 = findViewById<TextView>(R.id.userNameField2)
        val passwordField2 = findViewById<TextView>(R.id.passwordField2)

        val checkInfoButton = findViewById<Button>(R.id.checkInfoButton)

        val wrongLoginInfo = findViewById<TextView>(R.id.wrongLoginInfo)
        val newUsername = findViewById<TextView>(R.id.newUsername)
        val newPassword = findViewById<TextView>(R.id.newPassword)
        val updateInfoButton = findViewById<Button>(R.id.updateInfoButton)

        val enterNewInfoMessage = findViewById<TextView>(R.id.enterNewInfoMessage)

        if(id != -1) {
            userIdNumber = id
            newUsername.visibility = View.VISIBLE
            newPassword.visibility = View.VISIBLE
            updateInfoButton.visibility = View.VISIBLE
            enterNewInfoMessage.visibility = View.VISIBLE

            val changeInfoBackButton = findViewById<Button>(R.id.changeInfoBackButton)
            changeInfoBackButton.text = "Cancel Update"

            wrongLoginInfo.visibility = View.INVISIBLE

            userNameField2.isEnabled = false
            passwordField2.isEnabled = false
            checkInfoButton.isEnabled = false
        }
        else {
            wrongLoginInfo.visibility = View.VISIBLE
        }
    }

    fun confirmLoginInfoChange(check : Boolean){

        val userNameField2 = findViewById<TextView>(R.id.userNameField2)
        val passwordField2 = findViewById<TextView>(R.id.passwordField2)

        val checkInfoButton = findViewById<Button>(R.id.checkInfoButton)

        val newUsername = findViewById<TextView>(R.id.newUsername)
        val newPassword = findViewById<TextView>(R.id.newPassword)
        val updateInfoButton = findViewById<Button>(R.id.updateInfoButton)

        val enterNewInfoMessage = findViewById<TextView>(R.id.enterNewInfoMessage)
        val usernameTakenMessage = findViewById<TextView>(R.id.userNameTaken)

        if(check){
            newUsername.visibility = View.INVISIBLE
            newPassword.visibility = View.INVISIBLE
            updateInfoButton.visibility = View.INVISIBLE
            enterNewInfoMessage.visibility = View.INVISIBLE

            usernameTakenMessage.visibility = View.INVISIBLE

            userNameField2.text = null
            passwordField2.text = null
            newPassword.text = null
            newUsername.text = null

            userNameField2.isEnabled = true
            passwordField2.isEnabled = true
            checkInfoButton.isEnabled = true

            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)

        }
        else{
            usernameTakenMessage.visibility = View.VISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_login_info)

        val userNameField2 = findViewById<TextView>(R.id.userNameField2)
        val passwordField2 = findViewById<TextView>(R.id.passwordField2)

        val checkInfoButton = findViewById<Button>(R.id.checkInfoButton)

        val newUsername = findViewById<TextView>(R.id.newUsername)
        val newPassword = findViewById<TextView>(R.id.newPassword)
        val updateInfoButton = findViewById<Button>(R.id.updateInfoButton)

        val enterNewInfoMessage = findViewById<TextView>(R.id.enterNewInfoMessage)
        val usernameTakenMessage = findViewById<TextView>(R.id.userNameTaken)

        val changeInfoBackButton = findViewById<Button>(R.id.changeInfoBackButton)
        changeInfoBackButton.setText("Back to menu")

        newUsername.visibility = View.INVISIBLE
        newPassword.visibility = View.INVISIBLE
        updateInfoButton.visibility = View.INVISIBLE
        enterNewInfoMessage.visibility = View.INVISIBLE

        usernameTakenMessage.visibility = View.INVISIBLE

        userNameField2.isEnabled = true
        passwordField2.isEnabled = true
        checkInfoButton.isEnabled = true



        changeInfoBackButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            finish()
        }

        checkInfoButton.setOnClickListener{

            var user = User(userNameField2.text.toString(), passwordField2.text.toString())

            var serverDB = ServerHandler()

            serverDB.checkServerLoginInfo(user, this)
        }



        updateInfoButton.setOnClickListener{

            var user = User(userIdNumber, newUsername.text.toString(), newPassword.text.toString())

            var serverDB = ServerHandler()

            serverDB.upDateLoginInfo(user, this)

        }

    }

}