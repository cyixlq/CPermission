package top.cyixlq.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class CPermission {

    companion object {

        @JvmStatic
        fun get(activity: FragmentActivity): CPermissionBuilder {
            return CPermissionBuilder(activity.supportFragmentManager)
        }

        @JvmStatic
        fun get(fragment: Fragment): CPermissionBuilder {
            return CPermissionBuilder(fragment.childFragmentManager)
        }

    }
}