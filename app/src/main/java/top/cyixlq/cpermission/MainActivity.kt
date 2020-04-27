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
        CPermission.get(this).requestPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
        ).dealWith {
            allGranted {
                showToast("已经全部允许")
            }
            someDeny {
                showToast("部分被暂时拒绝或者永远拒绝")
                val denyStr = StringBuilder("已经被永久拒绝：")
                val rationaleStr = StringBuilder("被暂时拒绝：")
                for (permission in it) {
                    if (permission.canShowAgain) {
                        rationaleStr.append(permission.name).append("\n")
                        retry(permission.name) // 当这个权限可以继续弹出时，重试一次
                    } else {
                        denyStr.append(permission.name).append("\n")
                    }
                }
                Log.w("CY_TAG", denyStr.toString() + "\n" + rationaleStr.toString())
            }
        }.start(this)
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
