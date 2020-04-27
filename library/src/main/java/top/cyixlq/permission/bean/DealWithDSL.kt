package top.cyixlq.permission.bean

import androidx.lifecycle.MutableLiveData

class DealWithDSL(private val liveData: MutableLiveData<MutableList<String>>) {
    var allGranted: (() -> Unit)? = null
    var someDeny: ((MutableList<Permission>) -> Unit)? = null

    fun allGranted(allGranted: (() -> Unit)) {
        this.allGranted = allGranted
    }

    fun someDeny(someDeny: ((MutableList<Permission>) -> Unit)) {
        this.someDeny = someDeny
    }

    fun retry(vararg permission: String) {
        liveData.value = permission.toMutableList()
    }

    fun retry(list: MutableList<String>) {
        liveData.value = list
    }
}