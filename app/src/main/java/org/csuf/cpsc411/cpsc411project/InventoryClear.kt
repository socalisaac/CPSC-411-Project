package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


class InventoryClear : AppCompatActivity() {

    fun clearlocalInventoryTable(){
        val db = DataBaseHandler(this)
        db.clearInventoryTable()

        val intent = Intent(this, Inventory::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_clear)
        val inventoryClearBack = findViewById<Button>(R.id.inventoryClearBack)

        inventoryClearBack.setOnClickListener{
            val intent = Intent(this, InventoryMenu::class.java)
            startActivity(intent)
        }

        val confirmClear = findViewById<Button>(R.id.confirmClear)

        confirmClear.setOnClickListener{
            var serverDB = ServerHandler()
            serverDB.clearInventoryTable(this)
        }
    }
}