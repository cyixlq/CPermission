package top.cyixlq.cpermission.permission

import java.io.Serializable

interface PermissionListener: Serializable {

    fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

}