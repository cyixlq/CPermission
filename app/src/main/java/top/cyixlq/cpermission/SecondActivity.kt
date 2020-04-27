package top.cyixlq.cpermission

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SecondActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.e("CYTAG", "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.e("CYTAG", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("CYTAG", "onResume")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("CYTAG", "onNewIntent")
    }

    fun startMe(v: View) {
        start(this)
    }

    fun showTaskInfo(v: View) {
        //获取activity任务栈
        //获取activity任务栈
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //参数1是指最大任务栈数，一般APP也就只有一个任务栈
        //参数1是指最大任务栈数，一般APP也就只有一个任务栈
        val tasks: List<ActivityManager.RunningTaskInfo> = activityManager.getRunningTasks(3)
        for (item in tasks) {
            Log.e("CY_TAG", "Activity数量：${item.numActivities}；顶部Activity：${item.topActivity?.className}")
        }
    }
}
