package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val changeLoginInfoButton = findViewById<Button>(R.id.changeLoginInfoButton)

        changeLoginInfoButton.setOnClickListener{
            val intent = Intent(this, ChangeLoginInfo::class.java)
            startActivity(intent)
        }

        val logoutButton = findViewById<Button>(R.id.LogoutButton)

        logoutButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val inventoryButton = findViewById<Button>(R.id.inventoryButton)

        inventoryButton.setOnClickListener{
            val intent = Intent(this, Inventory::class.java)
            startActivity(intent)
        }

        val makeTransactionButton = findViewById<Button>(R.id.makeTransactionButton)

        makeTransactionButton.setOnClickListener{
            val intent = Intent(this, MakeTransaction::class.java)
            startActivity(intent)
        }

        val reportsButton = findViewById<Button>(R.id.reportsButton)

        reportsButton.setOnClickListener{
            val intent = Intent(this, Reports::class.java)
            startActivity(intent)
        }

        val salesHistoryButton = findViewById<Button>(R.id.salesHistoryButton)

        salesHistoryButton.setOnClickListener{
            val intent = Intent(this, SalesHistory::class.java)
            startActivity(intent)
        }
    }
}