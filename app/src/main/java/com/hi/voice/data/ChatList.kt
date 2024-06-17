package com.hi.voice.data

/**
 * type：会话类型
 * text：文本
 */
data class ChatList(
    val type: Int
) {
    lateinit var text: String

    //天气
    lateinit var wid: String
    lateinit var info: String
    lateinit var city: String
    lateinit var temperature: String
}