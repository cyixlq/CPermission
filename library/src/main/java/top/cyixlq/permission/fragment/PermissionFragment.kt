package top.cyixlq.permission.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import top.cyixlq.permission.bean.Permission
import top.cyixlq.permission.bean.PermissionResult

class PermissionFragment : Fragment() {

    companion object {
        fun instance(): PermissionFragment = PermissionFragment()
        private const val REQUEST_CODE = 100
    }

    val liveData = MutableLiveData<PermissionResult>()
    val permissionLiveData = MutableLiveData<MutableList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLiveData.observe(this, Observer {
            request(it.toTypedArray())
        })
    }

    private fun request(permissions: Array<out String>) {
        requestPermissions(permissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val denyPermissions = ArrayList<Permission>()
        if (requestCode != REQUEST_CODE) return
        grantResults.forEachIndexed { index, i ->
            val permissionName = permissions[index]
            if (i == PackageManager.PERMISSION_DENIED) {
                val canShowAgain = shouldShowRequestPermissionRationale(permissionName)
                val permission = Permission(permissionName, canShowAgain)
                denyPermissions.add(permission)
            }
        }
        if (denyPermissions.isEmpty()) {
            liveData.value = PermissionResult.allGrant()
        } else {
            liveData.value = PermissionResult.someDeny(denyPermissions)
        }
    }

}