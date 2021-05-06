package org.csuf.cpsc411.cpsc411project

import kotlinx.serialization.Serializable

@Serializable
class User {
    var id : Int = 0
    var username : String = ""
    var password : String = ""

    constructor(userName: String, password: String ){
        this.username = userName
        this.password = password
    }

}