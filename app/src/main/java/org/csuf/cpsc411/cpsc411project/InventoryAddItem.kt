package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import java.text.NumberFormat

class InventoryAddItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_add_item)

        val itemName = findViewById<EditText>(R.id.itemNameField)
        val itemQty = findViewById<EditText>(R.id.itemQtyField)
        val itemPrice = findViewById<EditText>(R.id.itemPriceField)

        itemPrice.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            var current: String = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    itemPrice.removeTextChangedListener(this)

                    val cleanString: String = s.replace("""[$,.]""".toRegex(), "")

                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                    current = formatted
                    itemPrice.setText(formatted)
                    itemPrice.setSelection(formatted.length)

                    itemPrice.addTextChangedListener(this)
                }
            }
        })

        val inventoryAddItemBack = findViewById<Button>(R.id.addItemBack)

        inventoryAddItemBack.setOnClickListener{
            val intent = Intent(this, InventoryMenu::class.java)
            startActivity(intent)
        }

        val addItem = findViewById<Button>(R.id.addItem)


        //TODO: Convert itemPrice back from its currency format to an integer for storage
        addItem.setOnClickListener{
            val item = Item(itemName.text.toString(), itemQty.text.toString().toInt(), itemPrice.text.toString().toFloat())
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