package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import java.text.NumberFormat
import java.util.*

class Reports : AppCompatActivity() {

    private lateinit var reportsTableLayout: TableLayout
    private lateinit var revenueTotalText: TextView
    private lateinit var qtyTotalText: TextView
    private var salesHistoryList = mutableListOf<Transaction>()
    private var reportsList = mutableListOf<ReportEntry>()
    var sortedAscName = false
    var sortedAscQty = false
    var sortedAscRevenue = false

    fun syncListWithServerDB(list: MutableList<Transaction>){
        salesHistoryList.clear()
        list.forEach {
            salesHistoryList.add(it)
        }
        Toast.makeText(this, "Sales History refreshed", Toast.LENGTH_SHORT).show()
        fillReportsList()
        updateTotals()
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
        reportsTableLayout = findViewById(R.id.reportsTableLayout)

        revenueTotalText = findViewById(R.id.totalRevenueValue)
        qtyTotalText = findViewById(R.id.totalQtySoldValue)

        val serverDB = ServerHandler()
        serverDB.getTransactionFromServer(this)

        reportsBack.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        syncButton.setOnClickListener{
            serverDB.getTransactionFromServer(this)
        }

        sortNameText.setOnClickListener {
            if (sortedAscName) {
                reportsList.sortByDescending { it.itemName }
                Toast.makeText(this, "Desc Name", Toast.LENGTH_SHORT).show()
                sortedAscName = !sortedAscName
            } else {
                reportsList.sortBy { it.itemName }
                Toast.makeText(this, "Asc Name", Toast.LENGTH_SHORT).show()
                sortedAscName = !sortedAscName
            }
            refreshTable()
        }

        sortQtyText.setOnClickListener {
            if (sortedAscQty) {
                reportsList.sortByDescending { it.totalQty }
                Toast.makeText(this, "Desc Qty", Toast.LENGTH_SHORT).show()
                sortedAscQty = !sortedAscQty
            } else {
                reportsList.sortBy { it.totalQty }
                Toast.makeText(this, "Asc Qty", Toast.LENGTH_SHORT).show()
                sortedAscQty = !sortedAscQty
            }
            refreshTable()
        }

        sortRevenueText.setOnClickListener {
            if (sortedAscRevenue) {
                reportsList.sortByDescending { it.totalRevenue }
                Toast.makeText(this, "Desc Revenue", Toast.LENGTH_SHORT).show()
                sortedAscRevenue = !sortedAscRevenue
            } else {
                reportsList.sortBy { it.totalRevenue }
                Toast.makeText(this, "Asc Revenue", Toast.LENGTH_SHORT).show()
                sortedAscRevenue = !sortedAscRevenue
            }
            refreshTable()
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
        reportsList.clear()
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
        reportsList.forEach {
            if(salesHistoryList[index].itemSoldName == name)
                return index
            else
                index++
        }
        return -1
    }

    private fun updateTotals() {
        revenueTotalText.text = "$0.00"
        qtyTotalText.text = "0"

        var totalRevenue = 0L
        reportsList.forEach {
            totalRevenue += it.totalRevenue
        }
        revenueTotalText.text = NumberFormat.getCurrencyInstance().format((totalRevenue.toDouble() / 100))

        var totalQtySold = 0
        reportsList.forEach {
            totalQtySold += it.totalQty
        }
        qtyTotalText.text = totalQtySold.toString()
    }
}