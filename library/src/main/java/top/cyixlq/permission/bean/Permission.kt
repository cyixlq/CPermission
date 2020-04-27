package top.cyixlq.permission.bean

data class Permission(
    val name: String, // 权限名
    val canShowAgain: Boolean = true // 是否可以再次弹出权限申请弹框
)