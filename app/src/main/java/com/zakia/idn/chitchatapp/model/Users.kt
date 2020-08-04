package com.zakia.idn.chitchatapp.model

class Users {
    private var uid: String = ""
    private var username: String = ""
    private var profile: String = ""

    constructor()

    constructor(
        uid: String,
        username: String,
        profile: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
    }

    fun getUID(): String? {
        return uid
    }

    fun setUID(uid: String) {
        this.uid = uid
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getProfile(): String? {
        return profile
    }

    fun setProfil(profile: String) {
        this.profile = profile
    }
}