package top.cyixlq.permission.`interface`

import android.content.Context

interface IGoToSettingsScope {
    fun showForwardToSettingsDialog(title: String = "", msg: String, btnText: String = "")
    fun goToSetting(context: Context)
}