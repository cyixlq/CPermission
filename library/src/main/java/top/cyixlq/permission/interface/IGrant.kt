package top.cyixlq.permission.`interface`

interface IGrant {
    fun onAllGrant(block: () -> Unit): IShowReason
}