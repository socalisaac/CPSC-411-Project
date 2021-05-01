package org.csuf.cpsc411.cpsc411project

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/*
Erick:

To delete the database for testing purposes ->

Android Studio > View > Tool Windows > Device File Explorer
    make sure the emulator you're using is selected
file location: data > data > org.csuf.cpsc411.cpsc411project > databases

delete all the files you can (i think you cant delete the file just named CPSC411BD idk why)
    database should be cleared

can maybe implement a clear database button for debugging
 */

const val DataBaseName = "CPSC411BD"

const val UserTableName = "Users"
const val ColUserName = "UserName"
const val ColPassword = "Password"
const val ColUserId = "UserID"

const val InventoryTableName = "Inventory"
const val ColItemID = "ItemID"
const val ColItemName = "ItemName"
const val ColItemQty = "ItemQty"
const val ColItemPrice = "ItemPrice"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DataBaseName, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $UserTableName " +
                "("
                "$ColUserId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ColUserName VARCHAR(256), " +
                "$ColPassword VARCHAR(256)" +
                ")"

        val createInventoryTable = "CREATE TABLE $InventoryTableName " +
                "("
                "$ColItemID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ColItemName VARCHAR(256), " +
                "$ColItemQty INTEGER, " +
                "$ColItemPrice VARCHAR(256) " +
                ")"

        db?.execSQL(createUserTable)
        db?.execSQL(createInventoryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertUser(user : User): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ColUserName, user.userName)
        cv.put(ColPassword, user.password)
        val result = db.insert(UserTableName, null, cv)
        return if(result == -1.toLong()) {
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun checkLogin(user: User) : Boolean{
        val db = this.readableDatabase
        val query = "Select * From $UserTableName " +
                "Where $ColUserName = '${user.userName}' " +
                "And $ColPassword = '${user.password}' "
        val result = db.rawQuery(query, null)

        return result.moveToFirst()
    }

    // Adds new item into inventory table
    fun insertItem(item: Item): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ColItemName, item.itemName)
        cv.put(ColItemQty, item.itemQty)
        cv.put(ColItemPrice, item.itemPrice)
        val result = db.insert(InventoryTableName, null, cv)
        return if(result == -1.toLong()) {
            Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, "Successfully added item", Toast.LENGTH_SHORT).show()
            true
        }
    }

    // Function to get cursor object pointing to top of inventory table data
    fun getInventoryData(): Cursor {
        val db = this.readableDatabase
        val query = " Select * From $InventoryTableName"
        return db.rawQuery(query, null)
    }
}