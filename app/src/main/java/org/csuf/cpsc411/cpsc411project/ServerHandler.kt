package org.csuf.cpsc411.cpsc411project

import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

var ipAddress = ""

class ServerHandler(): AppCompatActivity() {

    var callBackReceived = false
    var login: Boolean = false

    fun checkServerLogin(user: User, context: MainActivity){

        println("In Check login")
        val jsonStr = Json.encodeToString(user)

        var login: Boolean = false

        var connection = "http://$ipAddress:8888/Database/login"

        //"http://192.168.0.63:8888/Database/login"
        val test = Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                this.login = Json.decodeFromString<Boolean>(returnedResult)
                println("Web Service")
//                setCallBackReceived()
                context.reportValidationResult(this.login, "Incorrect login")
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
//                setCallBackReceived()
            }
        }

        if(!test.isDone){
            context.reportValidationResult(false, "Incorrect IP Address")
        }


    }

    fun registerUser(user: User, context: RegisterUser){ //fun registerUser(user: User, context: RegisterUser)
        val jsonStr = Json.encodeToString(user)

        println("In register")

        var connection = "http://$ipAddress:8888/Database/register"

        val test = Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val registerGood = Json.decodeFromString<Boolean>(returnedResult)
                println("Web Service")
                context.reportRegistrationResult(registerGood, "Username Already Taken")
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
        }

        if(!test.isDone){
            context.reportRegistrationResult(false, "Incorrect IP Address")
        }

    }

    fun getItemsFromServer(context: SyncTables){

        println("In Check login")

        var connection = "http://$ipAddress:8888/Database/getItemsTable"

        //"http://192.168.0.63:8888/Database/login"
        Fuel.post(connection).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : $returnedResult")
                val list = Json.decodeFromString<MutableList<Item>>(returnedResult)
                println("Web Service")

                println("Got a list with size: ${list.size}")

                context.syncWithLocalDB(list)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")

            }
        }

    }

    fun addItem(item: Item, context: InventoryAddItem){ //fun registerUser(user: User, context: RegisterUser)
        val jsonStr = Json.encodeToString(item)

        println("In addItem")

        var connection = "http://$ipAddress:8888/Database/addItem"

        Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val addItemGood = Json.decodeFromString<Int>(returnedResult)

                val newItem = Item(addItemGood, item.itemName, item.itemQty, item.itemPrice)

                context.addItemToLocalDB(newItem)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
        }

    }

    fun upDateItem(item: Item, context: InventoryAddItem){ //fun registerUser(user: User, context: RegisterUser)
        val jsonStr = Json.encodeToString(item)

        println("In upDateItem")

        var connection = "http://$ipAddress:8888/Database/addItem"

        Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val addItemGood = Json.decodeFromString<Int>(returnedResult)

                val newItem = Item(addItemGood, item.itemName, item.itemQty, item.itemPrice)

                context.addItemToLocalDB(newItem)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
        }

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