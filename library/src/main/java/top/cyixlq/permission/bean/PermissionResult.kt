package top.cyixlq.permission.bean

sealed class PermissionResult {

    companion object {
        fun allGrant(): PermissionResult = AllGrant
        fun someDeny(permissions: MutableList<Permission>): PermissionResult = SomeDeny(permissions)
    }

    object AllGrant: PermissionResult()
    data class SomeDeny(val permissions: MutableList<Permission>): PermissionResult()
}