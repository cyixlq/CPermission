package top.cyixlq.permission.`interface`

interface IShowReasonScope {
    fun showReasonDialog(title: String = "", msg: String, btnText: String = "")
    fun retry()
}