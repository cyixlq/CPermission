package top.cyixlq.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import top.cyixlq.permission.`interface`.*
import top.cyixlq.permission.fragment.PermissionFragment

class CPermissionBuilder(
    private var fragmentManager: FragmentManager?
) : IGrant, IShowReason, IDeny, IComplete, IShowReasonScope, IGoToSettingsScope {

    companion object {
        private const val TAG = "PermissionFragment"
    }

    private val mPermissionFragment: PermissionFragment by lazy {
        getLazySingleInstance()
    }

    var allGranted: (() -> Unit)? = null
    var someDeny: ((IGoToSettingsScope, HashSet<String>) -> Unit)? = null
    var showReason: ((IShowReasonScope, HashSet<String>) -> Unit)? = null
    lateinit var permissions: Array<out String>

    private fun getLazySingleInstance(): PermissionFragment {
        var permissionFragment: PermissionFragment? =
            fragmentManager?.findFragmentByTag(TAG) as PermissionFragment?
        if (permissionFragment == null) {
            permissionFragment = PermissionFragment.instance()
            fragmentManager?.beginTransaction()?.add(permissionFragment, TAG)?.commitNow()
        }
        return permissionFragment
    }

    fun permissions(vararg permission: String): CPermissionBuilder {
        permissions = permission
        return this
    }

    override fun showReasonDialog(title: String, msg: String, btnText: String) {
        showDialog(title, msg, btnText) {
            retry()
        }
    }

    override fun retry() {
        mPermissionFragment.request(this)
    }

    override fun onAllGrant(block: () -> Unit): IShowReason {
        this.allGranted = block
        return this
    }

    override fun onShowReason(block: (IShowReasonScope, HashSet<String>) -> Unit): IDeny {
        this.showReason = block
        return this
    }

    override fun onSomeDeny(block: (IGoToSettingsScope, HashSet<String>) -> Unit): IComplete {
        this.someDeny = block
        return this
    }

    override fun go() {
        mPermissionFragment.request(this)
    }

    override fun showForwardToSettingsDialog(title: String, msg: String, btnText: String) {
        showDialog(title, msg, btnText) {
            goToSetting(it)
        }
    }

    override fun goToSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        mPermissionFragment.startActivityForResult(
            intent,
            PermissionFragment.SETTING_REQUEST_CODE
        )
    }

    private fun showDialog(title: String, msg: String, btnText: String, block: (Context) -> Unit) {
        mPermissionFragment.context?.let { context ->
            AlertDialog.Builder(context).apply {
                if (title.isNotEmpty())
                    setTitle(title)
                setMessage(msg)
                setCancelable(btnText.isEmpty())
                if (btnText.isEmpty()) {
                    setOnDismissListener {
                        block(context)
                    }
                } else {
                    setPositiveButton(btnText) { _, _ ->
                        block(context)
                    }
                }
            }.show()
        }
    }
}