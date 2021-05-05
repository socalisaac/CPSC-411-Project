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



class DataBaseHandler(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    companion object {
        private val DATABASE_VERSION = 1.0
        private const val DATABASE_NAME = "CPSC411BD"

        const val USER_TABLE_NAME = "Users"
        const val COL_USER_ID = "UserID"
        const val COL_USERNAME = "UserName"
        const val COL_PASSWORD = "Password"

        const val INVENTORY_TABLE_NAME = "Inventory"
        const val COL_ITEM_ID = "ItemID"
        const val COL_ITEM_NAME = "ItemName"
        const val COL_ITEM_QTY = "ItemQty"
        const val COL_ITEM_PRICE = "ItemPrice"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        createUsersTable(db)
        createInventoryTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $INVENTORY_TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(user : User): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_USERNAME, user.userName)
        cv.put(COL_PASSWORD, user.password)
        val result = db.insert(USER_TABLE_NAME, null, cv)
        return if(result == (-1).toLong()) {
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun checkLogin(user: User) : Boolean {
        val db = this.readableDatabase
        val query = "Select * From $USER_TABLE_NAME " +
                "Where $COL_USERNAME = '${user.userName}' " +
                "And $COL_PASSWORD = '${user.password}' "

        val result = db.rawQuery(query, null)

        return result.moveToFirst()
    }

    // Adds new item into inventory table
    fun insertItem(item: Item): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_ITEM_QTY, item.itemQty)
        cv.put(COL_ITEM_PRICE, item.itemPrice)
        val result = db.insert(INVENTORY_TABLE_NAME, null, cv)
        return if(result == (-1).toLong()) {
            Toast.makeText(context, "Failed to add ${item.itemName}", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, "Successfully added ${item.itemName}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun updateItem(item: Item): Boolean {
        if(!checkEntryExists(INVENTORY_TABLE_NAME, COL_ITEM_ID, item.itemId.toString())){
            Toast.makeText(context, "Update failed error: Item not found", Toast.LENGTH_SHORT).show()
            return false
        }
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_ITEM_QTY, item.itemQty)
        cv.put(COL_ITEM_PRICE, item.itemPrice)
        val whereClause = "$COL_ITEM_ID = ${item.itemId}"
        val result = db.update(INVENTORY_TABLE_NAME, cv, whereClause, null)
        return if(result <= 0) {
            Toast.makeText(context, "Failed to update ${item.itemName}", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, "Successfully updated ${item.itemName}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun checkEntryExists(table: String, column: String, value: String): Boolean {
        val cursor = getEntry(table, column, value)
        if(cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun getEntry(table: String, column: String, value: String): Cursor {
        val db = this.readableDatabase
        val query = "Select * from $table Where $column = $value"
        return db.rawQuery(query, null)
    }

    // Function to get cursor object pointing to top of inventory table data
    fun getAllInventoryData(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(" Select * From $INVENTORY_TABLE_NAME", null)
    }

    fun clearInventoryTable(){
        val db = this.writableDatabase
        db?.execSQL("DROP TABLE IF EXISTS $INVENTORY_TABLE_NAME")
        createInventoryTable(db)
    }

    private fun createUsersTable(db: SQLiteDatabase?){
        val createUserTable = "CREATE TABLE $USER_TABLE_NAME " +
                "(" +
                "$COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_USERNAME VARCHAR(256), " +
                "$COL_PASSWORD VARCHAR(256)" +
                ")"

        db?.execSQL(createUserTable)
    }

    private fun createInventoryTable(db: SQLiteDatabase?){
        val createInventoryTable = "CREATE TABLE $INVENTORY_TABLE_NAME " +
                "(" +
                "$COL_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_ITEM_NAME VARCHAR(256), " +
                "$COL_ITEM_QTY INTEGER, " +
                "$COL_ITEM_PRICE INTEGER " +
                ")"

        db?.execSQL(createInventoryTable)
        Toast.makeText(context, "Inventory table cleared", Toast.LENGTH_SHORT).show()
    }
}

