package com.zakia.idn.chitchatapp.model

class Users {
    private var uid: String = ""
    private var username: String = ""
    private var bio: String = ""
    private var profile: String = ""

    constructor()

    constructor(
        uid: String,
        username: String,
        bio: String,
        profile: String
    ) {
        this.uid = uid
        this.username = username
        this.bio = bio
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

    fun getBio(): String? {
        return bio
    }

    fun setBio(bio: String) {
        this.bio = bio
    }

    fun getProfile(): String? {
        return profile
    }

    fun setProfil(profile: String) {
        this.profile = profile
    }
}