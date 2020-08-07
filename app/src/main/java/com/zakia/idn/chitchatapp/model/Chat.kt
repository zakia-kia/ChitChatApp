package com.zakia.idn.chitchatapp.model

class Chat {
    private var sender: String = ""
    private var message: String = ""
    private var receiver: String = ""
    private var iseen = false
    private var url: String = ""
    private var messageId: String = ""

    constructor()

    constructor(
        sender: String,
        message: String,
        receiver: String,
        iseen: Boolean,
        url: String,
        messageId: String
    ) {
        this.sender = sender
        this.message = message
        this.receiver = receiver
        this.iseen = iseen
        this.url = url
        this.messageId = messageId
    }

    fun getSender() : String? {
        return sender
    }
    fun setSender(sender: String?){
        this.sender = sender!!
    }

    fun getMessage() : String? {
        return message
    }
    fun setMessage(message: String?){
        this.message = message!!
    }

    fun getReceiver() : String? {
        return receiver
    }
    fun setReceiver(receiver: String?){
        this.receiver = receiver!!
    }

    fun isSeen() : Boolean {
        return iseen
    }
    fun setIseen(iseen: Boolean?){
        this.iseen = iseen!!
    }

    fun getUrl() : String? {
        return url
    }
    fun setUrl(url: String?){
        this.url = url!!
    }

    fun getMessageId() : String? {
        return messageId
    }
    fun setMessageId(messageId: String?){
        this.messageId = messageId!!
    }
}