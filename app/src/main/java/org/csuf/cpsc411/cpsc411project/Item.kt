package org.csuf.cpsc411.cpsc411project

class Item {
    var itemId : Int = 0
    var itemName : String = ""
    var itemQty : Int = 0
    var itemPrice : String = ""

    constructor(itemId: Int, itemName: String, itemQty: Int, itemPrice: String ){
        this.itemId = itemId
        this.itemName = itemName
        this.itemQty = itemQty
        this.itemPrice = itemPrice
    }

    constructor(itemName: String, itemQty: Int, itemPrice: String){
        this.itemName = itemName
        this.itemQty = itemQty
        this.itemPrice = itemPrice
    }

}