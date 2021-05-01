package org.csuf.cpsc411.cpsc411project

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DataBaseName = "CPSC411BD"
val UserTableName = "Users"
val ColUserName = "UserName"
val ColPassword = "Password"
val ColUserId = "UserID"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DataBaseName, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE " + UserTableName + " (" +
                ColUserId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColUserName + " VARCHAR(256), " +
                ColPassword + " VARCHAR(256)) ";

        db?.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertUser(user : User): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ColUserName, user.userName)
        cv.put(ColPassword, user.password)
        var result = db.insert(UserTableName, null, cv)
        if(result == -1.toLong()) {
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show()
            return false
        }
        else {
            Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
            return true
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
}