package top.cyixlq.permission.fragment

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import top.cyixlq.permission.bean.PermissionResult
import java.util.ArrayList

class PermissionFragment : Fragment() {

    companion object {
        fun instance(): PermissionFragment = PermissionFragment()
        private const val REQUEST_CODE = 100
    }

    val permissionResultLiveData = MutableLiveData<PermissionResult>()
    val retryLiveData = MutableLiveData<Int>()

    fun request(permissions: Array<out String>) {
        requestPermissions(permissions, REQUEST_CODE)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val denyPermissions = ArrayList<String>()
        val noticePermissions = ArrayList<String>()
        if (requestCode != REQUEST_CODE) return
        grantResults.forEachIndexed { index, i ->
            val permission = permissions[index]
            if (i == PackageManager.PERMISSION_DENIED) {
                val canShowAgain = shouldShowRequestPermissionRationale(permission)
                if (canShowAgain)
                    noticePermissions.add(permission)
                else
                    denyPermissions.add(permission)
            }
        }
        // 会优先处理可以再次弹框的权限，所以请保证不要漏写someNotice方法
        if (denyPermissions.isEmpty() && noticePermissions.isEmpty()) {
            permissionResultLiveData.value = PermissionResult.allGrant()
        } else if (noticePermissions.isNotEmpty()) {
            permissionResultLiveData.value = PermissionResult.someNotice(noticePermissions)
        } else {
            permissionResultLiveData.value = PermissionResult.someDeny(denyPermissions)
        }
    }

}