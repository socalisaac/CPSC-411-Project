package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.*
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Reports : AppCompatActivity() {

    private lateinit var reportsTableLayout: TableLayout
    private var salesHistoryList = mutableListOf<Transaction>()
    private var reportsList = mutableListOf<ReportEntry>()

    fun syncListWithServerDB(list: MutableList<Transaction>){
        salesHistoryList.clear()
        list.forEach {
            salesHistoryList.add(it)
        }
        Toast.makeText(this, "Sales History refreshed", Toast.LENGTH_SHORT).show()
        fillReportsList()
        refreshTable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val reportsBack = findViewById<Button>(R.id.reportsBack)
        val syncButton = findViewById<Button>(R.id.syncButton)
        val sortNameText = findViewById<TextView>(R.id.headerName)
        val sortQtyText = findViewById<TextView>(R.id.headerQtySold)
        val sortRevenueText = findViewById<TextView>(R.id.headerRevenue)
        val reportsTableLayout = findViewById<TableLayout>(R.id.reportsTableLayout)

        val revenueTotalText = findViewById<TextView>(R.id.totalRevenueValue)
        val qtyTotalText = findViewById<TextView>(R.id.totalQtySoldValue)

        val serverDB = ServerHandler()
        serverDB.getTransactionFromServer(this)

        reportsBack.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

    }

    private fun refreshTable() {
        reportsTableLayout.removeAllViews()

        var isAlternateRow = false
        reportsList.forEach {
            val newTableRow = TableRow(this)
            if (isAlternateRow) {
                newTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.material_on_primary_emphasis_high_type))
                isAlternateRow = !isAlternateRow
            } else {
                newTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.material_on_primary_emphasis_medium))
                isAlternateRow = !isAlternateRow
            }

            val textName = TextView(this)
            textName.text = it.itemName
            textName.setPadding(5, 10, 5, 10)
            newTableRow.addView(textName, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F))

            val textQty = TextView(this)
            textQty.text = it.totalQty.toString()
            textQty.setPadding(5, 10, 5, 10)
            newTableRow.addView(textQty, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.15F))

            val textPrice = TextView(this)
            val priceString = it.totalRevenue.toString()
            val priceDouble = priceString.toDouble()
            val priceFormatted = NumberFormat.getCurrencyInstance().format((priceDouble / 100))
            textPrice.text = priceFormatted
            textPrice.setPadding(5, 10, 5, 10)
            newTableRow.addView(textPrice, TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25F))

            reportsTableLayout.addView(newTableRow, TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT))
        }
    }

    private fun fillReportsList() {
        salesHistoryList.forEach { sales ->
            val index = searchList(sales.itemSoldName)
            if(index == -1){
                reportsList.add(ReportEntry(sales.itemSoldName, sales.itemSoldQty, sales.revenue))
            }
            else{
                reportsList[index].totalQty += sales.itemSoldQty
                reportsList[index].totalRevenue += sales.revenue
            }
        }
    }

    private fun searchList(name: String): Int {
        var index = 0
        salesHistoryList.forEach {
            if(salesHistoryList[index].itemSoldName == name)
                return index
            else
                index++
        }
        return -1
    }
}