package com.example.moodapp.model


class User{
    val id: Int
    val login: String
    val dateOfBirth: String
    var password: String

    constructor(id: Int, login: String, dateOfBirth: String, password: String) {
        this.id = id
        this.login = login
        this.dateOfBirth = dateOfBirth
        this.password = password
    }

    override fun toString(): String {
        return "User(name='$login', dateOfBirth='$dateOfBirth', password='$password')"
    }
}
