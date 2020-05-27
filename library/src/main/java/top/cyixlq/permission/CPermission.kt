package top.cyixlq.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import top.cyixlq.permission.bean.DealWithDSL
import top.cyixlq.permission.bean.PermissionResult
import top.cyixlq.permission.fragment.PermissionFragment

class CPermission(
    private var fragmentManager: FragmentManager?,
    private var lifecycleOwner: LifecycleOwner?
) : LifecycleObserver{

    companion object {
        @JvmStatic
        fun get(activity: FragmentActivity): CPermission {
            return CPermission(activity.supportFragmentManager, activity)
        }

        @JvmStatic
        fun get(fragment: Fragment): CPermission {
            return CPermission(fragment.childFragmentManager, fragment)
        }

        private const val TAG = "PermissionFragment"
    }

    private val mPermissionFragment: PermissionFragment by lazy {
        getLazySingleInstance()
    }


    init {
        lifecycleOwner?.let {owner ->
            mPermissionFragment.permissionResultLiveData.observe(owner, Observer {
                when(it) {
                    is PermissionResult.AllGrant -> allGranted?.invoke()
                    is PermissionResult.SomeDeny -> someDeny?.invoke(it.permissions)
                    is PermissionResult.SomeNotice -> someNotice?.invoke(it.permissions)
                }
            })
            mPermissionFragment.retryLiveData.observe(owner, Observer {
                permissions?.let {
                    mPermissionFragment.request(it)
                }
            })
            owner.lifecycle.addObserver(this)
        }
    }

    private var allGranted: (() -> Unit)? = null
    private var someDeny: ((MutableList<String>) -> Unit)? = null
    private var someNotice: ((MutableList<String>) -> Unit)? = null
    private var permissions: Array<out String>? = null

    private fun getLazySingleInstance(): PermissionFragment {
        var permissionFragment: PermissionFragment? = fragmentManager?.findFragmentByTag(TAG) as PermissionFragment?
        if (permissionFragment == null) {
            permissionFragment = PermissionFragment.instance()
            fragmentManager?.beginTransaction()?.add(permissionFragment, TAG)?.commitNow()
        }
        return permissionFragment
    }

    fun requestPermissions(vararg permission: String): CPermission {
        mPermissionFragment.retryLiveData.value = mPermissionFragment.retryLiveData.value?.plus(1)
        permissions = permission
        return this
    }

    fun dealWith(init: DealWithDSL.() -> Unit): CPermission {
        val dealWithDSL = DealWithDSL(mPermissionFragment.retryLiveData)
        dealWithDSL.init()
        this.allGranted = dealWithDSL.allGranted
        this.someDeny = dealWithDSL.someDeny
        this.someNotice = dealWithDSL.someNotice
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun release() {
        this.someNotice = null
        this.allGranted = null
        this.someDeny = null
        this.lifecycleOwner?.lifecycle?.removeObserver(this)
        this.lifecycleOwner = null
        this.fragmentManager?.beginTransaction()?.remove(mPermissionFragment)
        this.fragmentManager = null
    }
}