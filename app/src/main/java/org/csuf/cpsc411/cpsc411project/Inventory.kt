package org.csuf.cpsc411.cpsc411project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat
import java.text.NumberFormat

const val EXTRA_ITEM_ID = "org.csuf.cpsc411.cpsc411project.ITEMID"

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

        refreshTable()
    }

    // Should be called each time the page is reopened to refresh/update the list
    override fun onResume() {
        super.onResume()

        refreshTable()
    }

    // Clears table and refills it with new info after printing the header row first
    private fun refreshTable(){
        val invTableLayout = findViewById<TableLayout>(R.id.inventoryTableLayout)
        invTableLayout.removeAllViews()

        val invHeadRow = createHeaderRow()
        invTableLayout.addView(invHeadRow, TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT))

        val db = DataBaseHandler(this)
        val cursor = db.getAllInventoryData()
        var isAlternateRow = false
        if(cursor!!.moveToFirst() && cursor.count >= 1) {
            do {
                val newTableRow = TableRow(this)
                if(isAlternateRow){
                    newTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.material_on_primary_emphasis_high_type))
                    isAlternateRow = !isAlternateRow
                }
                else{
                    newTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.material_on_primary_emphasis_medium))
                    isAlternateRow = !isAlternateRow
                }

                val textID = TextView(this)
                textID.text = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_ID))
                textID.setPadding(5, 10, 5, 10)
                newTableRow.addView(textID, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1F))

                val textName = TextView(this)
                textName.text = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_NAME))
                textName.setPadding(5, 10, 5, 10)
                newTableRow.addView(textName, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F))

                val textQty = TextView(this)
                textQty.text = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_QTY))
                textQty.setPadding(5, 10, 5, 10)
                newTableRow.addView(textQty, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.15F))

                val textPrice = TextView(this)
                val priceString = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_PRICE))
                val priceDouble = priceString.toDouble()
                val priceFormatted = NumberFormat.getCurrencyInstance().format((priceDouble / 100))
                textPrice.text = priceFormatted
                textPrice.setPadding(5, 10, 5, 10)
                newTableRow.addView(textPrice, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25F))

                newTableRow.setOnLongClickListener{
                    val intent = Intent(this, InventoryEditItem::class.java).apply {
                        putExtra(EXTRA_ITEM_ID, textID.text.toString())
                    }
                    startActivity(intent)

                    return@setOnLongClickListener true
                }

                invTableLayout.addView(newTableRow, TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT))

            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createHeaderRow(): TableRow {
        val invHeadRow = TableRow(this)
        invHeadRow.gravity = Gravity.CENTER
        invHeadRow.weightSum = 1.0F
        invHeadRow.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))

        val lblID = TextView(this)
        lblID.text = "ID"
        lblID.setPadding(5, 5, 5, 5)
        invHeadRow.addView(lblID, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1F))

        val lblName = TextView(this)
        lblName.text = "Name"
        lblName.setPadding(5, 5, 5, 5)
        invHeadRow.addView(lblName, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F))

        val lblQty = TextView(this)
        lblQty.text = "Qty"
        lblQty.setPadding(5, 5, 5, 5)
        invHeadRow.addView(lblQty, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.15F))

        val lblPrice = TextView(this)
        lblPrice.text = "Price (per item)"
        lblPrice.setPadding(5, 5, 5, 5)
        invHeadRow.addView(lblPrice, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25F))

        return invHeadRow
    }

}