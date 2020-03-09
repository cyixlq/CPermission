package top.cyixlq.cpermission.permission

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment

class PermissionFragment private constructor(): Fragment() {

    companion object {
        @JvmStatic
        fun instance(permissionListener: PermissionListener): PermissionFragment {
            return PermissionFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable("listener", permissionListener)
                arguments = bundle
            }
        }
    }

    private var permissionListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionListener = arguments?.get("listener") as PermissionListener
    }

    fun requestPermission(vararg permissions: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 666)
        } else {
            val results = IntArray(permissions.size)
            for (index in permissions.indices) {
                results[index] = PackageManager.PERMISSION_GRANTED
            }
            onRequestPermissionsResult(666, permissions, results)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 666) {
            permissionListener?.onPermissionResult(requestCode, permissions, grantResults)
        }
    }

}