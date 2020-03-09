package top.cyixlq.cpermission.permission;

data class Permission(
    val isGranted: Boolean,
    val name: String,
    val shouldShowRequestPermissionRationale: Boolean
)
