package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*

class InventoryAddItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_add_item)

        val itemName = findViewById<EditText>(R.id.itemNameField)
        val itemQty = findViewById<EditText>(R.id.itemQtyField)
        val itemPrice = findViewById<EditText>(R.id.itemPriceField)

        val inventoryAddItemBack = findViewById<Button>(R.id.addItemBack)

        inventoryAddItemBack.setOnClickListener{
            val intent = Intent(this, InventoryMenu::class.java)
            startActivity(intent)
        }

        val addItem = findViewById<Button>(R.id.addItem)

        addItem.setOnClickListener{
            val inputQty = itemQty.text.toString()
            val intQty = Integer.parseInt(inputQty)
            val item = Item(itemName.text.toString(), intQty, itemPrice.text.toString())
            val db = DataBaseHandler(this)

            db.insertItem(item)
        }

        val clearItem = findViewById<Button>(R.id.clearItem)

        clearItem.setOnClickListener{
            itemName.text.clear()
            itemQty.text.clear()
            itemPrice.text.clear()
        }
    }
}