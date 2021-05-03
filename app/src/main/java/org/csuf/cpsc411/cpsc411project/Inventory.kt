package org.csuf.cpsc411.cpsc411project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*

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
        val cursor = db.getInventoryData()
        if(cursor!!.moveToFirst() && cursor!!.count >= 1) {
            do {
                val newTableRow = TableRow(this)

                val textID = TextView(this)
                textID.text = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_ID))
                textID.setPadding(5, 5, 5, 5)
                newTableRow.addView(textID, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1F))

                val textName = TextView(this)
                textName.text = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_NAME))
                textName.setPadding(5, 5, 5, 5)
                newTableRow.addView(textName, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F))

                val textQty = TextView(this)
                textQty.text = cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_QTY))
                textQty.setPadding(5, 5, 5, 5)
                newTableRow.addView(textQty, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.15F))

                val textPrice = TextView(this)
                textPrice.text = "$${cursor.getString(cursor.getColumnIndex(DataBaseHandler.COL_ITEM_PRICE))}"
                textPrice.setPadding(5, 5, 5, 5)
                newTableRow.addView(textPrice, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25F))

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