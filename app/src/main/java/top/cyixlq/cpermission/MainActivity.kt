package top.cyixlq.cpermission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import top.cyixlq.permission.CPermission

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CPermission.get(this).requestPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
        ).dealWith {
            allGranted {
                showToast("所有权限已经被允许")
            }
            someDeny {
                showToast("部分权限被永久拒绝")
                for (i in it) {
                    if (i == Manifest.permission.CAMERA) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("警告")
                            .setMessage("相机权限为必须权限，如果没有这个权限，APP将无法正常运行！")
                            .setPositiveButton("确定") { _, _ ->
                                retry()
                            }.show()
                    }
                }
            }
            // 当有权限被临时拒绝和有权限被永久拒绝的情况下
            // 优先回调被临时拒绝的这个someNotice方法
            // 不回调被永久拒绝的这个someDeny方法，注意不回调，不回调，不回调！！！
            // 所以无论怎么样，请保证someNotice被实现，或者说在三种情况下都有不同逻辑的，最好三个方法都要被实现
            someNotice {
                showToast("部分权限被暂时拒绝")
                for (i in it) {
                    if (i == Manifest.permission.CAMERA) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("警告")
                            .setMessage("相机权限为必须权限，如果没有这个权限，APP将无法正常运行！")
                            .setPositiveButton("确定") { _, _ ->
                                retry()
                            }.show()
                    }
                }
            }
        }
    }

    fun startSecondActivity(v: View) {
        SecondActivity.start(this)
    }

    fun startThirdActivity(v: View) {
        startActivity(Intent(this, ThirdActivity::class.java))
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("CY_TAG", message)
    }
}
