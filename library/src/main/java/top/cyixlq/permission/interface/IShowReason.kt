package top.cyixlq.permission.`interface`

interface IShowReason: IDeny {
    fun onShowReason(block: (scope: IShowReasonScope, permissions: HashSet<String>) -> Unit): IDeny
}