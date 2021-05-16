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

        const val TRANSACTION_TABLE_NAME = "Transactions"
        const val COL_TRANSACTION_ID = "TransactionID"
        const val COL_ITEM_SOLD_NAME = "ItemSoldName"
        const val COL_ITEM_SOLD_QTY = "ItemSoldQty"
        const val COL_REVENUE = "Revenue"
        const val COL_DATE_OF_TRANSACTION = "DateOfTransaction"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        createUsersTable(db)
        createInventoryTable(db)
        createTransactionTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $INVENTORY_TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(user : User): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_USERNAME, user.username)
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
                "Where $COL_USERNAME = '${user.username}' " +
                "And $COL_PASSWORD = '${user.password}' "

        val result = db.rawQuery(query, null)

        return result.moveToFirst()
    }

    // Adds new item into inventory table
    fun insertItem(item: Item): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_ID, item.itemId)
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


    // Adds new item into inventory table
    fun insertItemWithoutToast(item: Item): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_ID, item.itemId)
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_ITEM_QTY, item.itemQty)
        cv.put(COL_ITEM_PRICE, item.itemPrice)
        val result = db.insert(INVENTORY_TABLE_NAME, null, cv)
        return result != (-1).toLong()
    }


    fun updateItem(item: Item): Boolean {
//        if(!checkItemExists(item)){
//            Toast.makeText(context, "Update failed error: Item not found", Toast.LENGTH_SHORT).show()
//            return false
//        }
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

    fun getItem(cursor: Cursor): Item {
        val id = cursor.getString(cursor.getColumnIndex(COL_ITEM_ID)).toInt()
        val name = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME))
        val qty = cursor.getString(cursor.getColumnIndex(COL_ITEM_QTY)).toInt()
        val price = cursor.getString(cursor.getColumnIndex(COL_ITEM_PRICE)).toInt()
        return Item(id, name, qty, price)
    }

    fun checkItemExists(item: Item): Boolean {
        val db = this.readableDatabase
        val query = "Select * From $INVENTORY_TABLE_NAME " +
                "Where $COL_ITEM_NAME = '${item.itemName}' AND " +
                "$COL_ITEM_ID != '${item.itemId}'"
        val result = db.rawQuery(query, null)
        return result.moveToFirst()
    }

    fun getEntry(table: String, column: String, value: String): Cursor {
        val db = this.readableDatabase
        val query = "Select * from $table Where $column = '$value'"
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
        createInventoryTable(db, "Inventory table cleared")
    }
    fun refreshInventoryTable(){
        val db = this.writableDatabase
        db?.execSQL("DROP TABLE IF EXISTS $INVENTORY_TABLE_NAME")
        createInventoryTable(db, "Inventory table refreshed")
    }

    fun addTransaction(transaction: Transaction): Item {
        //val db = this.writableDatabase
        /**
        val cv = ContentValues()
        cv.put(COL_TRANSACTION_ID, transaction.id)
        cv.put(COL_ITEM_SOLD_NAME, transaction.itemSoldName)
        cv.put(COL_ITEM_SOLD_QTY, transaction.itemSoldQty)
        cv.put(COL_REVENUE, transaction.revenue)
        **/

        val cursor = transaction.itemSoldName.let { getEntry(INVENTORY_TABLE_NAME, COL_ITEM_NAME, transaction.itemSoldName) }
        if(cursor.moveToFirst() && cursor.count >= 1) {
            val itemID = cursor.getInt(cursor.getColumnIndex(COL_ITEM_ID))
            val itemName = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME))
            val itemQty = cursor.getInt(cursor.getColumnIndex(COL_ITEM_QTY))
            val itemPrice = cursor.getInt(cursor.getColumnIndex(COL_ITEM_PRICE))
            val item = Item(itemID, itemName, itemQty, itemPrice)
            item.itemQty -= transaction.itemSoldQty
            updateItem(item)

            return item
        }
        else{
            return Item(-1, "Name", 0, 0)
        }
        /**
        val result = db.insert(TRANSACTION_TABLE_NAME, null, cv)
        return if(result == (-1).toLong()) {
            Toast.makeText(context, "Failed to add transaction", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, "Successfully added transaction", Toast.LENGTH_SHORT).show()
            true
        }
        **/
    }


    private fun createUsersTable(db: SQLiteDatabase?){
        val createUserTable = "CREATE TABLE $USER_TABLE_NAME " +
                "(" +
                "$COL_USER_ID INTEGER PRIMARY KEY, " +
                "$COL_USERNAME VARCHAR(256), " +
                "$COL_PASSWORD VARCHAR(256)" +
                ")"

        db?.execSQL(createUserTable)
    }

    private fun createInventoryTable(db: SQLiteDatabase?, message: String){
        val createInventoryTable = "CREATE TABLE $INVENTORY_TABLE_NAME " +
                "(" +
                "$COL_ITEM_ID INTEGER PRIMARY KEY, " +
                "$COL_ITEM_NAME VARCHAR(256), " +
                "$COL_ITEM_QTY INTEGER, " +
                "$COL_ITEM_PRICE INTEGER " +
                ")"

        db?.execSQL(createInventoryTable)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun createInventoryTable(db: SQLiteDatabase?){
        val createInventoryTable = "CREATE TABLE $INVENTORY_TABLE_NAME " +
                "(" +
                "$COL_ITEM_ID INTEGER PRIMARY KEY, " +
                "$COL_ITEM_NAME VARCHAR(256), " +
                "$COL_ITEM_QTY INTEGER, " +
                "$COL_ITEM_PRICE INTEGER " +
                ")"

        db?.execSQL(createInventoryTable)
    }

    private fun createTransactionTable(db: SQLiteDatabase?){
        val createTransactionTable = "CREATE TABLE $TRANSACTION_TABLE_NAME " +
                "(" +
                "$COL_TRANSACTION_ID INTEGER PRIMARY KEY, " +
                "$COL_ITEM_SOLD_NAME VARCHAR(256), " +
                "$COL_ITEM_SOLD_QTY INTEGER, " +
                "$COL_REVENUE INTEGER, " +
                "$COL_DATE_OF_TRANSACTION INTEGER " +
                ")"

        db?.execSQL(createTransactionTable)
        Toast.makeText(context, "Transaction table created", Toast.LENGTH_SHORT).show()
    }
}

