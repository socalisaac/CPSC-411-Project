package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDateTime

class MakeTransaction : AppCompatActivity() {
    private val itemList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_transaction)

        fillItemList()

        val itemNameErrorText = findViewById<TextView>(R.id.itemNameErrorText)
        val itemPrice = findViewById<TextView>(R.id.itemPriceValue)
        val totalPrice = findViewById<TextView>(R.id.totalPriceValue)
        val itemQtyField = findViewById<EditText>(R.id.itemQtyField)
        val makeTransaction = findViewById<Button>(R.id.makeTransactionButton)
        val back = findViewById<Button>(R.id.makeTransactionBack)

        val itemNameAutoField = findViewById<AutoCompleteTextView>(R.id.itemNameField)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, getNamesList())
        itemNameAutoField.setAdapter(adapter)

        var itemValid = false

        back.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        itemNameAutoField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val item: Item? = itemList.find { it.itemName == s.toString() }
                if(s.isEmpty()){
                    itemValid = false
                    itemNameErrorText.visibility = TextView.INVISIBLE
                }
                else if (item != null) {
                    val priceString = item.itemPrice.toString()
                    val priceDouble = priceString.toDouble()
                    val priceFormatted = NumberFormat.getCurrencyInstance().format((priceDouble / 100))
                    itemPrice.text = priceFormatted
                    if(itemQtyField.text.isNotEmpty() && itemQtyField.text.toString().toInt() >= 0){
                        val price = item.itemPrice.toBigDecimal()
                        val qty = itemQtyField.text.toString().toBigDecimal()
                        val total: BigDecimal = price * qty
                        val totalFormatted = NumberFormat.getCurrencyInstance().format((total.toDouble() / 100))
                        totalPrice.text = totalFormatted
                    }

                    itemValid = true
                    itemNameErrorText.visibility = TextView.INVISIBLE
                }
                else{
                    itemValid = false
                    itemNameErrorText.visibility = TextView.VISIBLE
                }
            }
        })

        itemQtyField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.toString().toInt() >= 0 && itemValid) {
                    val price = itemPrice.text.toString().replace("""[$,.]""".toRegex(), "").toBigDecimal()
                    val qty = itemQtyField.text.toString().toBigDecimal()
                    val total: BigDecimal = price * qty
                    val totalFormatted = NumberFormat.getCurrencyInstance().format((total.toDouble() / 100))
                    totalPrice.text = totalFormatted                }
            }
        })

        makeTransaction.setOnClickListener {
            val name = itemNameAutoField.text.toString()
            val qty = itemQtyField.text.toString()
            val revenue = totalPrice.text.toString().replace("""[$,.]""".toRegex(), "").toLong()
            val date = System.currentTimeMillis()

            val item: Item? = itemList.find { it.itemName == name}

            when {
                name.trim().isEmpty() -> Toast.makeText(this, "Please enter the item name", Toast.LENGTH_SHORT).show()
                qty.trim().isEmpty() -> Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show()
                item != null && qty.toInt() > item.itemQty -> Toast.makeText(this, "Quantity exceeds amount in inventory", Toast.LENGTH_SHORT).show()
                else -> {
                    val transaction = Transaction(name, qty.toInt(), revenue, date)
                    val db = DataBaseHandler(this)
                    db.addTransaction(transaction)
                    /**
                    val serverDB = ServerHandler()

                    serverDB.addTransaction(transaction, this)
                    **/
                    itemNameAutoField.text.clear()
                    itemQtyField.text.clear()
                }
            }
        }
    }

    private fun fillItemList() {
        val db = DataBaseHandler(this)
        val cursor = db.getAllInventoryData()

        if (cursor!!.moveToFirst() && cursor.count >= 1) {
            do {
                itemList.add(db.getItem(cursor))
            } while (cursor.moveToNext())
            cursor.close()
        }
        itemList.sortBy { it.itemName }
    }

    private fun getNamesList() : List<String> {
        val namesList = mutableListOf<String>()
        itemList.forEach { namesList.add(it.itemName) }
        return namesList
    }
}