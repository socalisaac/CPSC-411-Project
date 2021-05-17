package org.csuf.cpsc411.cpsc411project

import kotlinx.serialization.Serializable

@Serializable
class ReportEntry {
    var id : Int = 0
    var itemName : String = "Default_Name"
    var totalQty : Int = 0
    // itemTotalRevenue is stored as an Int: smallest unit of currency (cost in cents)
    var totalRevenue : Long = 0

    constructor(itemId: Int, itemName: String, itemTotalQty: Int, totalRevenue: Long ){
        this.id = itemId
        this.itemName = itemName
        this.totalQty = itemTotalQty
        this.totalRevenue = totalRevenue
    }

    constructor(itemName: String, itemTotalQty: Int, totalRevenue: Long ){
        this.itemName = itemName
        this.totalQty = itemTotalQty
        this.totalRevenue = totalRevenue
    }
}