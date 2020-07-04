package top.cyixlq.permission.`interface`

interface IDeny {
    fun onSomeDeny(block: (scope: IGoToSettingsScope, permissions: HashSet<String>) -> Unit): IComplete
}