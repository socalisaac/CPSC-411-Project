package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class Inventory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val inventoryBack = findViewById<Button>(R.id.inventoryBack)

        inventoryBack.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        val inventoryMenu = findViewById<Button>(R.id.inventoryMenu)

        inventoryMenu.setOnClickListener{
            val intent = Intent(this, InventoryMenu::class.java)
            startActivity(intent)
        }

        populateTable()
    }

    // Should be called each time the page is reopened to refresh/update the list
    override fun onResume() {
        super.onResume()

        populateTable()
    }

    // Clears table and refills it with new info after printing the header row first
     private fun populateTable(){
         val invTableLayout = findViewById<TableLayout>(R.id.inventoryTableLayout)
         val invHeadRow = TableRow(this)
         val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)

         invTableLayout.removeAllViews()

         val label_id = TextView(this)
         label_id.text = "ID"
         label_id.setPadding(5, 5, 5, 5)
         invHeadRow.addView(label_id)

         val label_name = TextView(this)
         label_name.text = "Name"
         label_name.setPadding(5, 5, 5, 5)
         invHeadRow.addView(label_name)

         val label_qty = TextView(this)
         label_qty.text = "Qty"
         label_qty.setPadding(5, 5, 5, 5)
         invHeadRow.addView(label_qty)

         val label_price = TextView(this)
         label_price.text = "Price"
         label_price.setPadding(5, 5, 5, 5)
         invHeadRow.addView(label_price)


         invTableLayout.addView(invHeadRow, TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT))
     }
}