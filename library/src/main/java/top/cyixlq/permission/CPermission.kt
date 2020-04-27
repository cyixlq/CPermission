package top.cyixlq.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import top.cyixlq.permission.bean.DealWithDSL
import top.cyixlq.permission.bean.Permission
import top.cyixlq.permission.bean.PermissionResult
import top.cyixlq.permission.fragment.PermissionFragment

class CPermission(private val fragmentManager: FragmentManager) {

    companion object {
        @JvmStatic
        fun get(activity: FragmentActivity): CPermission {
            return CPermission(activity.supportFragmentManager)
        }

        @JvmStatic
        fun get(fragment: Fragment): CPermission {
            return CPermission(fragment.childFragmentManager)
        }

        private const val TAG = "PermissionFragment"
    }

    private val mPermissionFragment by lazy {
        getLazySingleInstance()
    }

    private var allGranted: (() -> Unit)? = null
    private var someDeny: ((MutableList<Permission>) -> Unit)? = null

    private fun getLazySingleInstance(): PermissionFragment {
        var permissionFragment: PermissionFragment? = fragmentManager.findFragmentByTag(TAG) as PermissionFragment?
        if (permissionFragment == null) {
            permissionFragment = PermissionFragment.instance()
            fragmentManager.beginTransaction().add(permissionFragment, TAG).commitNow();
        }
        return permissionFragment
    }

    fun requestPermissions(vararg permission: String): CPermission {
        mPermissionFragment.permissionLiveData.value = permission.toMutableList()
        return this
    }

    fun dealWith(init: DealWithDSL.() -> Unit): CPermission {
        val dealWithDSL = DealWithDSL(mPermissionFragment.permissionLiveData)
        dealWithDSL.init()
        this.allGranted = dealWithDSL.allGranted
        this.someDeny = dealWithDSL.someDeny
        return this
    }

    fun start(lifecycleOwner: LifecycleOwner) {
        mPermissionFragment.liveData.observe(lifecycleOwner, Observer {
            when(it) {
                is PermissionResult.AllGrant -> allGranted?.invoke()
                is PermissionResult.SomeDeny -> someDeny?.invoke(it.permissions)
            }
        })
    }

}