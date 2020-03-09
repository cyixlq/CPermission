package top.cyixlq.cpermission

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import top.cyixlq.cpermission.permission.CPermission
import top.cyixlq.cpermission.permission.Permission
import top.cyixlq.cpermission.permission.annotation.OnDenied
import top.cyixlq.cpermission.permission.annotation.OnGranted
import top.cyixlq.cpermission.permission.annotation.OnNotShowRequestEver
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CPermission.get(this).requestPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE)
    }

    @OnGranted
    fun permissionGranted(permissions: ArrayList<Permission>) {
        for(permission in permissions) {
            showToast("已授权：${permission.name}")
        }
    }

    @OnDenied
    fun permissionDenied(permissions: ArrayList<Permission>) {
        for(permission in permissions) {
            showToast("未授权：${permission.name}")
        }
    }

    @OnNotShowRequestEver
    fun permissionNotShow(permissions: ArrayList<Permission>) {
        for(permission in permissions) {
            showToast("不再显示权限申请弹框：${permission.name}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("CY_TAG", message)
    }
}
