package top.cyixlq.cpermission.permission;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;

import top.cyixlq.cpermission.permission.annotation.OnDenied;
import top.cyixlq.cpermission.permission.annotation.OnGranted;
import top.cyixlq.cpermission.permission.annotation.OnNotShowRequestEver;

public class CPermission {

    private static final String TAG = CPermission.class.getSimpleName();

    public static CPermission get(FragmentActivity activity) {
        return new CPermission(activity, null);
    }

    public static CPermission get(Fragment fragment) {
        return new CPermission(null, fragment);
    }

    private Lazy<PermissionFragment> mPermissionFragment;
    private Class<?> mClazz;
    private FragmentActivity mActivity;
    private Fragment mFragment;

    private CPermission(FragmentActivity activity, Fragment fragment) {
        if (activity != null) {
            mPermissionFragment = getLazySingleton(activity.getSupportFragmentManager());
            mClazz = activity.getClass();
            mActivity = activity;
        } else if (fragment != null) {
            mPermissionFragment = getLazySingleton(fragment.getChildFragmentManager());
            mClazz = fragment.getClass();
            mFragment = fragment;
        }
    }

    private Lazy<PermissionFragment> getLazySingleton(final FragmentManager fragmentManager) {
        return new Lazy<PermissionFragment>() {

            private PermissionFragment permissionFragment;

            @Override
            public synchronized PermissionFragment get() {
                if (permissionFragment == null) {
                    permissionFragment = getPermissionFragment(fragmentManager);
                }
                return permissionFragment;
            }
        };
    }

    private PermissionFragment getPermissionFragment(FragmentManager fragmentManager) {
        PermissionFragment permissionFragment = findPermissionFragmentByTag(fragmentManager);
        if (permissionFragment == null) {
            permissionFragment = PermissionFragment.instance(new PermissionListener() {
                @Override
                public void onPermissionResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
                    dealResult(requestCode, permissions, grantResults);
                }
            });
            fragmentManager.beginTransaction().add(permissionFragment, TAG).commitNow();
        }
        return permissionFragment;
    }

    private PermissionFragment findPermissionFragmentByTag(FragmentManager fragmentManager) {
        return (PermissionFragment) fragmentManager.findFragmentByTag(TAG);
    }

    public void requestPermission(String... permissions) {
        mPermissionFragment.get().requestPermission(permissions);
    }

    private void dealResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        final ArrayList<Permission> grantedPermissions = new ArrayList<>(permissions.length);
        final ArrayList<Permission> deniedPermissions = new ArrayList<>(permissions.length);
        final ArrayList<Permission> notShowPermissions = new ArrayList<>(permissions.length);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(new Permission(true, permissions[i], false));
            } else {
                final boolean shouldShow = mPermissionFragment.get().shouldShowRequestPermissionRationale(permissions[i]);
                if (shouldShow) {
                    deniedPermissions.add(new Permission(false, permissions[i], true));
                } else {
                    notShowPermissions.add(new Permission(false, permissions[i], false));
                }
            }
        }
        Method[] declaredMethods = mClazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            OnGranted onGranted = declaredMethod.getAnnotation(OnGranted.class);
            if (onGranted != null) {
                invokeMethod(declaredMethod, grantedPermissions);
                continue;
            }
            OnDenied onDenied = declaredMethod.getAnnotation(OnDenied.class);
            if (onDenied != null) {
                invokeMethod(declaredMethod, deniedPermissions);
                continue;
            }
            OnNotShowRequestEver should = declaredMethod.getAnnotation(OnNotShowRequestEver.class);
            if (should != null) {
                invokeMethod(declaredMethod, notShowPermissions);
            }
        }
    }

    private void invokeMethod(Method declaredMethod, ArrayList<Permission> permissions) {
        try {
            if (mActivity != null)
                declaredMethod.invoke(mActivity, permissions);
            else if (mFragment != null) {
                declaredMethod.invoke(mFragment, permissions);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage() + "");
        }
    }

    interface Lazy<T> {
        T get();
    }
}
