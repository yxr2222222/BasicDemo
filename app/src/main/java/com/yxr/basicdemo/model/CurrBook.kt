package com.yxr.basicdemo.model

data class CurrBook(
    val bookId: Int,
    val bookName: String,
    val bookAuthor: String?,
    val bookCover: String?,
    val bookDesc: String?
)