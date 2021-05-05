package org.csuf.cpsc411.cpsc411project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompatSideChannelService
import androidx.core.content.ContextCompat.startActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.logging.Handler
import kotlin.math.log

class ServerHandler(): AppCompatActivity() {

    var callBackReceived = false
    var login: Boolean = false

    fun checkServerLogin(user: User, context : MainActivity): Boolean{

        println("In Check login")
        val jsonStr = Json.encodeToString(user)

        var login: Boolean = false

        println("About to send the post request. ")
        Fuel.post("http://192.168.0.63:8888/Database/login").body(jsonStr).response(){
                request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                this.login = Json.decodeFromString<Boolean>(returnedResult)
                println("Web Service")
                setCallBackReceived()

                val wrongLogin = findViewById<TextView>(R.id.wrongLoginTextView)
                val loginButton = findViewById<Button>(R.id.loginButton)

                context.reportValidationResult(this.login)
//                if(this.login){
//                    val intent = Intent(context, MainMenu::class.java)
//                    startActivity(intent)
//                }
//                else{
//                    wrongLogin.visibility = View.VISIBLE
//                } //userName.text.toString() == "Admin" &&  password.text.toString() == "1234"
//                loginButton.text = "Login"
//                loginButton.isEnabled = true
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
                setCallBackReceived()
            }
        }

        return login


    }

    private fun resetCallBackReceived(){
        println("callBackReceived set to false")
        callBackReceived = false
    }

    private fun setCallBackReceived(){
        println("callBackReceived set to true")
        callBackReceived = true
    }

    fun waitForCallBackReceived(){
        while(!callBackReceived){
            println("Here")
        }
    }
}