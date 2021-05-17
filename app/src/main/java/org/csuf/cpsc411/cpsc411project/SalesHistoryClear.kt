package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class SalesHistoryClear : AppCompatActivity() {

    /**
    fun clearLocalTransactionTable(){
        val db = DataBaseHandler(this)
        db.clearTransactionTable()

        val intent = Intent(this, SalesHistory::class.java)
        startActivity(intent)
    }
    **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_history_clear)

        val salesHistoryClearBack = findViewById<Button>(R.id.salesHistoryClearBack)
        val salesHistoryConfirmClear = findViewById<Button>(R.id.salesHistoryConfirmClear)

        salesHistoryClearBack.setOnClickListener{
            val intent = Intent(this, SalesHistory::class.java)
            startActivity(intent)
        }

        salesHistoryConfirmClear.setOnClickListener{
            var serverDB = ServerHandler()
            serverDB.clearTransactionsTable(this)
        }
    }
}