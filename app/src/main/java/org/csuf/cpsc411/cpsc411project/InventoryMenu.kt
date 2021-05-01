package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class InventoryMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_menu)

        val inventoryMenuBack = findViewById<Button>(R.id.inventoryMenuBack)

        inventoryMenuBack.setOnClickListener{
            val intent = Intent(this, Inventory::class.java)
            startActivity(intent)
        }

        val menuAddItem = findViewById<Button>(R.id.menuAddItem)

        menuAddItem.setOnClickListener{
            val intent = Intent(this, InventoryAddItem::class.java)
            startActivity(intent)
        }
    }
}