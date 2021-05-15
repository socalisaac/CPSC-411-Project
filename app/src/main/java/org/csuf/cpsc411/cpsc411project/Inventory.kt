package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import java.text.NumberFormat

const val EXTRA_ITEM_ID = "org.csuf.cpsc411.cpsc411project.ITEMID"

class Inventory : AppCompatActivity() {


    fun syncWithLocalDB(list : MutableList<Item>)
    {
        val db = DataBaseHandler(this)
        db.refreshInventoryTable()

        println("cleared and about to add items")

        list.forEach{
            db.insertItemWithoutToast(it)
        }

        this.refreshTable()
        this.refreshList()
    }

    private lateinit var invTableLayout: TableLayout
    private val itemList = mutableListOf<Item>()
    var sortedAscID = true
    var sortedAscName = false
    var sortedAscQty = false
    var sortedAscPrice = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        invTableLayout = findViewById(R.id.inventoryTableLayout)

        refreshTable()

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

        val syncButton = findViewById<Button>(R.id.syncButton)
        syncButton.setOnClickListener{
            val intent = Intent(this, SyncTables::class.java)
            startActivity(intent)
        }

        val sortIDText = findViewById<TextView>(R.id.headerID)
        sortIDText.setOnClickListener(View.OnClickListener{
            if(sortedAscID) {
                itemList.sortByDescending { it.itemId }
                Toast.makeText(this, "Desc ID", Toast.LENGTH_SHORT).show()
                sortedAscID = !sortedAscID
            }
            else {
                itemList.sortBy { it.itemId }
                Toast.makeText(this, "Asc ID", Toast.LENGTH_SHORT).show()
                sortedAscID = !sortedAscID
            }
            refreshTable()
        })

        val sortNameText = findViewById<TextView>(R.id.headerName)
        sortNameText.setOnClickListener(View.OnClickListener{
            if(sortedAscName) {
                itemList.sortByDescending { it.itemName }
                Toast.makeText(this, "Desc Name", Toast.LENGTH_SHORT).show()
                sortedAscName = !sortedAscName
            }
            else {
                itemList.sortBy { it.itemName }
                Toast.makeText(this, "Asc Name", Toast.LENGTH_SHORT).show()
                sortedAscName = !sortedAscName
            }
            refreshTable()
        })

        val sortQtyText = findViewById<TextView>(R.id.headerQty)
        sortQtyText.setOnClickListener(View.OnClickListener{
            if(sortedAscQty) {
                itemList.sortByDescending { it.itemQty }
                Toast.makeText(this, "Desc Qty", Toast.LENGTH_SHORT).show()
                sortedAscQty = !sortedAscQty
            }
            else {
                itemList.sortBy { it.itemQty }
                Toast.makeText(this, "Asc Qty", Toast.LENGTH_SHORT).show()
                sortedAscQty = !sortedAscQty
            }
            refreshTable()
        })

        val sortPriceText = findViewById<TextView>(R.id.headerPrice)
        sortPriceText.setOnClickListener(View.OnClickListener{
            if(sortedAscPrice) {
                itemList.sortByDescending { it.itemPrice }
                Toast.makeText(this, "Desc Price", Toast.LENGTH_SHORT).show()
                sortedAscPrice = !sortedAscPrice
            }
            else {
                itemList.sortBy { it.itemPrice }
                Toast.makeText(this, "Asc Price", Toast.LENGTH_SHORT).show()
                sortedAscPrice = !sortedAscPrice
            }
            refreshTable()
        })
    }

    // Should be called each time the page is reopened to refresh/update the list
    override fun onResume() {
        super.onResume()


        println("Syncing Tables")
        var serverDB = ServerHandler()
        serverDB.getItemsFromServer(this)
    }

    private fun refreshList() {
        val db = DataBaseHandler(this)
        val cursor = db.getAllInventoryData()

        itemList.clear()

        if (cursor!!.moveToFirst() && cursor.count >= 1) {
            do {
                itemList.add(db.getItem(cursor))
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    // Clears table and refills it with new info after printing the header row first
    private fun refreshTable(){
        invTableLayout.removeAllViews()

        var isAlternateRow = false
        itemList.forEach{
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
            textID.text = it.itemId.toString()
            textID.setPadding(5, 10, 5, 10)
            newTableRow.addView(textID, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1F))

            val textName = TextView(this)
            textName.text = it.itemName
            textName.setPadding(5, 10, 5, 10)
            newTableRow.addView(textName, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F))

            val textQty = TextView(this)
            textQty.text = it.itemQty.toString()
            textQty.setPadding(5, 10, 5, 10)
            newTableRow.addView(textQty, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.15F))

            val textPrice = TextView(this)
            val priceString = it.itemPrice.toString()
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
        }
    }
}