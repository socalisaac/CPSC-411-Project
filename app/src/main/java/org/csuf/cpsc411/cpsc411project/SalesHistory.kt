package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SalesHistory : AppCompatActivity() {

    private lateinit var salesHistoryTableLayout: TableLayout
    private var salesHistoryList = mutableListOf<Transaction>()
    var sortedAscID = true
    var sortedAscName = false
    var sortedAscQty = false
    var sortedAscRevenue = false
    var sortedAscDate = false

    fun syncListWithServerDB(list: MutableList<Transaction>){
        salesHistoryList.clear()
        list.forEach {
            salesHistoryList.add(it)
        }
        Toast.makeText(this, "Sales History refreshed", Toast.LENGTH_SHORT).show()
        refreshTable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_history)

        salesHistoryTableLayout = findViewById(R.id.salesHistoryTableLayout)
        val salesHistoryClear = findViewById<Button>(R.id.salesHistoryClear)
        val salesHistorySync = findViewById<Button>(R.id.syncButton)
        val salesHistoryBack = findViewById<Button>(R.id.salesHistoryBack)
        val sortIDText = findViewById<TextView>(R.id.headerID)
        val sortNameText = findViewById<TextView>(R.id.headerName)
        val sortQtyText = findViewById<TextView>(R.id.headerQtySold)
        val sortRevenueText = findViewById<TextView>(R.id.headerRevenue)
        val sortDateText = findViewById<TextView>(R.id.headerDate)


        val serverDB = ServerHandler()
        serverDB.getTransactionFromServer(this)

        salesHistoryClear.setOnClickListener{
            val intent = Intent(this, SalesHistoryClear::class.java)
            startActivity(intent)
        }

        salesHistoryBack.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        salesHistorySync.setOnClickListener{
            serverDB.getTransactionFromServer(this)
        }

        sortIDText.setOnClickListener {
            if (sortedAscID) {
                salesHistoryList.sortByDescending { it.id }
                Toast.makeText(this, "Desc ID", Toast.LENGTH_SHORT).show()
                sortedAscID = !sortedAscID
            } else {
                salesHistoryList.sortBy { it.id }
                Toast.makeText(this, "Asc ID", Toast.LENGTH_SHORT).show()
                sortedAscID = !sortedAscID
            }
            refreshTable()
        }

        sortNameText.setOnClickListener {
            if (sortedAscName) {
                salesHistoryList.sortByDescending { it.itemSoldName }
                Toast.makeText(this, "Desc Name", Toast.LENGTH_SHORT).show()
                sortedAscName = !sortedAscName
            } else {
                salesHistoryList.sortBy { it.itemSoldName }
                Toast.makeText(this, "Asc Name", Toast.LENGTH_SHORT).show()
                sortedAscName = !sortedAscName
            }
            refreshTable()
        }

        sortQtyText.setOnClickListener {
            if (sortedAscQty) {
                salesHistoryList.sortByDescending { it.itemSoldQty }
                Toast.makeText(this, "Desc Qty", Toast.LENGTH_SHORT).show()
                sortedAscQty = !sortedAscQty
            } else {
                salesHistoryList.sortBy { it.itemSoldQty }
                Toast.makeText(this, "Asc Qty", Toast.LENGTH_SHORT).show()
                sortedAscQty = !sortedAscQty
            }
            refreshTable()
        }

        sortRevenueText.setOnClickListener {
            if (sortedAscRevenue) {
                salesHistoryList.sortByDescending { it.revenue }
                Toast.makeText(this, "Desc Revenue", Toast.LENGTH_SHORT).show()
                sortedAscRevenue = !sortedAscRevenue
            } else {
                salesHistoryList.sortBy { it.revenue }
                Toast.makeText(this, "Asc Revenue", Toast.LENGTH_SHORT).show()
                sortedAscRevenue = !sortedAscRevenue
            }
            refreshTable()
        }

        sortDateText.setOnClickListener {
            if (sortedAscDate) {
                salesHistoryList.sortByDescending { it.date }
                Toast.makeText(this, "Desc Date", Toast.LENGTH_SHORT).show()
                sortedAscDate = !sortedAscDate
            } else {
                salesHistoryList.sortBy { it.date }
                Toast.makeText(this, "Asc Date", Toast.LENGTH_SHORT).show()
                sortedAscDate = !sortedAscDate
            }
            refreshTable()
        }

    }

    private fun refreshTable() {
        salesHistoryTableLayout.removeAllViews()

        var isAlternateRow = false
        salesHistoryList.forEach {
            val newTableRow = TableRow(this)
            if (isAlternateRow) {
                newTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.material_on_primary_emphasis_high_type))
                isAlternateRow = !isAlternateRow
            } else {
                newTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.material_on_primary_emphasis_medium))
                isAlternateRow = !isAlternateRow
            }

            val textID = TextView(this)
            textID.text = it.id.toString()
            textID.setPadding(5, 10, 5, 10)
            newTableRow.addView(textID, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1F))

            val textName = TextView(this)
            textName.text = it.itemSoldName
            textName.setPadding(5, 10, 5, 10)
            newTableRow.addView(textName, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F))

            val textQty = TextView(this)
            textQty.text = it.itemSoldQty.toString()
            textQty.setPadding(5, 10, 5, 10)
            newTableRow.addView(textQty, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.15F))

            val textPrice = TextView(this)
            val priceString = it.revenue.toString()
            val priceDouble = priceString.toDouble()
            val priceFormatted = NumberFormat.getCurrencyInstance().format((priceDouble / 100))
            textPrice.text = priceFormatted
            textPrice.setPadding(5, 10, 5, 10)
            newTableRow.addView(textPrice, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25F))

            val textDate = TextView(this)
            textDate.text = getDate(it.date, "MM/dd/yyyy HH:mm")
            textDate.setPadding(5, 10, 5, 10)
            newTableRow.addView(textDate, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25F))

            salesHistoryTableLayout . addView (newTableRow, TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT))
        }
    }

    private fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}