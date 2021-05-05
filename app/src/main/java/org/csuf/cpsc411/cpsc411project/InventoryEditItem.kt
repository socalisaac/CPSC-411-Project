package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import java.text.NumberFormat

class InventoryEditItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_edit_item)

        val itemID = intent.getStringExtra(EXTRA_ITEM_ID)
        val itemNameField = findViewById<EditText>(R.id.editNameField)
        val itemQtyField = findViewById<EditText>(R.id.editQtyField)
        val itemPriceField = findViewById<EditText>(R.id.editPriceField)

        val db = DataBaseHandler(this)
        val cursor = itemID?.let { db.getEntry(DataBaseHandler.INVENTORY_TABLE_NAME, DataBaseHandler.COL_ITEM_ID, it) }

        var itemName: String = "Default_Name"
        var itemQty: String = "0"
        var itemPrice: String = "0"

        if(cursor!!.moveToFirst() && cursor.count >= 1) {
            itemName = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_NAME))
            itemQty = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_QTY))
            itemPrice = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_PRICE))
            Toast.makeText(this, "Editing $itemName", Toast.LENGTH_SHORT).show()
        }

        itemNameField.hint = itemName
        itemQtyField.hint = itemQty

        val parsedPriceString = itemPrice.toDouble()
        val formattedPriceString = NumberFormat.getCurrencyInstance().format((parsedPriceString / 100))
        itemPriceField.hint = formattedPriceString

        itemPriceField.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            var current: String = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    itemPriceField.removeTextChangedListener(this)

                    val cleanString: String = s.replace("""[$,.]""".toRegex(), "")

                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                    current = formatted
                    itemPriceField.setText(formatted)
                    itemPriceField.setSelection(formatted.length)

                    itemPriceField.addTextChangedListener(this)
                }
            }
        })

        val saveChanges = findViewById<Button>(R.id.saveChanges)
        saveChanges.setOnClickListener{
            var itemNameString = itemNameField.text.toString()
            var noChange = true
            if(itemNameString.trim().isEmpty())
                itemNameString = itemName
            else
                noChange = false
            var itemQtyString = itemQtyField.text.toString()
            if(itemQtyString.trim().isEmpty())
                itemQtyString = itemQty
            else
                noChange = false
            val priceCleanString: String = itemPriceField.text.toString().replace("""[$,.]""".toRegex(), "")
            var itemPriceString = priceCleanString
            if(itemPriceString.trim().isEmpty())
                itemPriceString = itemPrice
            else
                noChange = false

            if(noChange){
                Toast.makeText(this, "No changes have been made", Toast.LENGTH_SHORT).show()
            }
            else {
                val item = Item(itemID.toInt(), itemNameString, itemQtyString.toInt(), itemPriceString.toInt())
                val db = DataBaseHandler(this)
                db.updateItem(item)
            }
        }

        val undoChanges = findViewById<Button>(R.id.undoChanges)
        undoChanges.setOnClickListener{
            itemNameField.hint = itemName
            itemQtyField.hint = itemQty
            itemPriceField.hint = formattedPriceString

            Toast.makeText(this, "Changes undone", Toast.LENGTH_SHORT).show()
        }

        val inventoryEditItemBack = findViewById<Button>(R.id.editItemBack)
        inventoryEditItemBack.setOnClickListener{
            val intent = Intent(this, Inventory::class.java)
            startActivity(intent)
        }
    }


}