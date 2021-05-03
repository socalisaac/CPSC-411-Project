package org.csuf.cpsc411.cpsc411project

import java.math.BigDecimal

class Item {
    var itemId : Int = 0
    var itemName : String = "Default_Name"
    var itemQty : Int = 0
    //TODO: Change itemPrice to be an Integer stored in the SQLite database for accuracy, then use BigDecimal in backend
    var itemPrice : Float = 0.00F

    constructor(itemId: Int, itemName: String, itemQty: Int, itemPrice: Float ){
        this.itemId = itemId
        this.itemName = itemName
        this.itemQty = itemQty
        this.itemPrice = itemPrice
    }

    constructor(itemName: String, itemQty: Int, itemPrice: Float){
        this.itemName = itemName
        this.itemQty = itemQty
        this.itemPrice = itemPrice
    }

}