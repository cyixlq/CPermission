package top.cyixlq.permission.fragment

import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import top.cyixlq.permission.CPermissionBuilder

class PermissionFragment : Fragment() {

    companion object {
        fun instance(): PermissionFragment = PermissionFragment()
        private const val REQUEST_CODE = 100
        const val SETTING_REQUEST_CODE = 200
    }

    private var builder: CPermissionBuilder? = null

    fun request(builder: CPermissionBuilder) {
        this.builder = builder
        requestPermissions(builder.permissions, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTING_REQUEST_CODE) { // 设置完权限回到界面再次请求权限用来检查是否全部允许了
            builder?.let {
                requestPermissions(it.permissions, REQUEST_CODE)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val pb = builder ?: return
        val denyPermissions = HashSet<String>()
        val noticePermissions = HashSet<String>()
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
        if (denyPermissions.isEmpty() && noticePermissions.isEmpty()) {
            pb.allGranted?.invoke()
        } else if (noticePermissions.isNotEmpty()) {
            val showReasonCallback = pb.showReason
            if (showReasonCallback != null) {
                showReasonCallback.invoke(pb, noticePermissions)
            } else {
                denyPermissions.addAll(noticePermissions)
                pb.someDeny?.invoke(pb, denyPermissions)
            }
        } else {
            pb.someDeny?.invoke(pb, denyPermissions)
        }
    }
}