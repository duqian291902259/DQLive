package com.duqian.app.helper

object PageRecordHelper {

    private val recordList = arrayListOf<Int>()

    fun record(pageCode: Int) {
        if (pageCode == 0) {
            recordList.clear()
        }
        recordList.add(pageCode)
        if (recordList.size > 20) {
            recordList.removeAt(0)
        }
    }

    fun getPageRecord(): String {
        return recordList.toString()
    }

}