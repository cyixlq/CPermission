package top.cyixlq.cpermission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import top.cyixlq.permission.CPermission

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CPermission.get(this).permissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
        ).onAllGrant {
            showToast("全部权限已经允许")
        }.onShowReason{scope, permissions ->
            val msg = genMessage(permissions,
                "这些权限是程序运行时的必要权限，但是您暂时拒绝了它们，请在接下来的弹窗中允许它们")
            scope.showReasonDialog(msg = msg, btnText = " 我明白了")
        }.onSomeDeny { scope, permissions ->
            val msg = genMessage(permissions,
                "这些权限是程序运行时的必要权限，但是您永久拒绝了它们，请前往设置重新允许它们")
            scope.showForwardToSettingsDialog(msg = msg, btnText = "前往设置")
        }.go()
    }

    fun startSecondActivity(v: View) {
        SecondActivity.start(this)
    }

    fun startThirdActivity(v: View) {
        startActivity(Intent(this, ThirdActivity::class.java))
    }

    private fun genMessage(permissions: HashSet<String>, subMsg: String): String {
        return StringBuilder().apply {
            permissions.forEachIndexed { index, s ->
                append(convertPermissionName(s))
                if (index < permissions.size - 1)
                    append("、")
                else
                    append(";")
            }
            append(subMsg)
        }.toString()
    }

    private fun convertPermissionName(tag: String): String {
        return when(tag) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> "读写存储权限"
            Manifest.permission.CAMERA -> "相机权限"
            Manifest.permission.CALL_PHONE -> "拨打电话权限"
            else -> ""
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("CY_TAG", message)
    }
}
