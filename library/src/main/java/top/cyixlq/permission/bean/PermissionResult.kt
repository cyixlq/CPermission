package top.cyixlq.permission.bean

sealed class PermissionResult {

    companion object {
        fun allGrant(): PermissionResult = AllGrant
        fun someDeny(permissions: MutableList<String>): PermissionResult = SomeDeny(permissions)
        fun someNotice(permissions: MutableList<String>): PermissionResult = SomeNotice(permissions)
    }

    object AllGrant: PermissionResult()
    data class SomeDeny(val permissions: MutableList<String>): PermissionResult()
    data class SomeNotice(val permissions: MutableList<String>): PermissionResult()
}