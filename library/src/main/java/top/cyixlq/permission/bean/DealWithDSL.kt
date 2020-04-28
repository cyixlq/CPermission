package top.cyixlq.permission.bean

import androidx.lifecycle.MutableLiveData

class DealWithDSL(private val liveData: MutableLiveData<Int>) {
    var allGranted: (() -> Unit)? = null
    var someDeny: ((MutableList<String>) -> Unit)? = null
    // 用户拒绝但未勾选不再询问的情况下，需要给个弹框告知用户为啥需要这个权限。可以再次弹出权限申请窗口的权限处理
    var someNotice: ((MutableList<String>) -> Unit)? = null

    fun allGranted(allGranted: (() -> Unit)) {
        this.allGranted = allGranted
    }

    fun someDeny(someDeny: ((MutableList<String>) -> Unit)) {
        this.someDeny = someDeny
    }

    fun someNotice(someNotice: ((MutableList<String>) -> Unit)) {
        this.someNotice = someNotice
    }

    // 全部重试申请，永久拒绝的仍然是永久拒绝，始终允许了的就是始终允许，针对的只是拒绝了但是没有勾选不再提示的重试操作
    fun retry() {
        liveData.value = liveData.value?.plus(1)
    }
}