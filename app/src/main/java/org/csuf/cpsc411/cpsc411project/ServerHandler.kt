package org.csuf.cpsc411.cpsc411project

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
                context.reportValidationResult(false, "Incorrect IP Address")
            }
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
                context.reportRegistrationResult(false, "Incorrect IP Address")
            }
        }
    }

    fun upDateLoginInfo(user: User, context: ChangeLoginInfo){ //fun registerUser(user: User, context: RegisterUser)
        val jsonStr = Json.encodeToString(user)

        println("In upDateLoginInfo")

        var connection = "http://$ipAddress:8888/Database/updateLoginInfo"

        val test = Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val registerGood = Json.decodeFromString<Boolean>(returnedResult)
                println("Web Service")
                context.confirmLoginInfoChange(registerGood)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
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

    fun getItemsFromServer(context: Inventory){

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

    fun editItem(item: Item, context: InventoryEditItem){ //fun registerUser(user: User, context: RegisterUser)
        val jsonStr = Json.encodeToString(item)

        println("In editItem")

        var connection = "http://$ipAddress:8888/Database/editItem"

        Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val addItemGood = Json.decodeFromString<Boolean>(returnedResult)

                if(!addItemGood){
                    println("Edit item was not good")
                }

                val newItem = Item(item.itemId, item.itemName, item.itemQty, item.itemPrice)

                context.editItemInLocalDB(newItem)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
        }

    }

    fun addTransaction(transaction: Transaction, context: MakeTransaction){ //fun registerUser(user: User, context: RegisterUser)
        val jsonStr = Json.encodeToString(transaction)

        println("In addTransaction")

        var connection = "http://$ipAddress:8888/Database/addTransaction"

        Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val transactionID = Json.decodeFromString<Int>(returnedResult)

                val transaction = Transaction(transactionID, transaction.itemSoldName, transaction.itemSoldQty, transaction.revenue, transaction.date)

                context.addTransactionToLocalDB(transaction)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
        }

    }

    fun checkServerLoginInfo(user: User, context: ChangeLoginInfo) {
        println("In Check login info")
        val jsonStr = Json.encodeToString(user)

        var connection = "http://$ipAddress:8888/Database/checkLoginInfo"

        //"http://192.168.0.63:8888/Database/login"
        val test = Fuel.post(connection).body(jsonStr).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : ${returnedResult}")
                val userID = Json.decodeFromString<Int>(returnedResult)
                println("Web Service")
                context.checkLoginInfo(userID)
            }
            else {
                Log.d("Web Service Log", "${error}")
                println("Web Service Log $error")
            }
        }

    }

    fun clearInventoryTable(context: InventoryClear){

        println("In clearInventoryTable")

        var connection = "http://$ipAddress:8888/Database/clearItemsTable"

        //"http://192.168.0.63:8888/Database/login"
        Fuel.post(connection).response(){ request, response, result ->
            //Option 1
            var (data, error) = result
            if(data != null) {
                val returnedResult = String(data!!)
                Log.d("Web Service Log", "Data returned from REST server : $returnedResult")
                //val clearGood = Json.decodeFromString<Boolean>(returnedResult)
                println("Web Service")

               // println("table clear was: $clearGood")

                context.clearlocalInventoryTable()
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