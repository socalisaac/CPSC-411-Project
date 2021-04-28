package org.csuf.cpsc411.cpsc411project

class User {
    var id : Int = 0
    var userName : String = ""
    var password : String = ""

    constructor(userName: String, password: String ){
        this.userName = userName
        this.password = password
    }

}