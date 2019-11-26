package com.example.deimos.fwp

fun updateDataList(dataList: ArrayList<ArticleData>) : ArrayList<ArticleData> {
    kotlin.repeat(30) {
        dataList.add(dataList[dataList.size + 1])
    }
    return dataList
}