package com.innext.szqb.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static Stack<FragmentActivity> activityStack;
    private static AppManager instance;
    private ArrayList<ExitHandler> tasks = null;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(FragmentActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public FragmentActivity currentActivity() {
        FragmentActivity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        FragmentActivity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(FragmentActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (FragmentActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束除cls外的所有activity
     */
    public void notFinishMainActivity(Class<?> cls) {
        try {
            List<Activity> delete = new ArrayList<>();
            for (Activity activity : activityStack) {
                if (!cls.equals(activity.getClass())) {
                    delete.add(activity);
                }
            }
            activityStack.removeAll(delete);
            for (Activity activity : delete) {
                if (activity != null) {
                    activity.finish();
                    activity = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 退出应用程序
     */
    public void appExit(Context context) {
        if (tasks != null) {
            for (ExitHandler task : tasks) {
                task.beforeExit();
            }
        }
        try {
            finishAllActivity();
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);

            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                am.killBackgroundProcesses(context.getPackageName());
                // Intent startMain = new Intent(Intent.ACTION_MAIN);
                // startMain.addCategory(Intent.CATEGORY_HOME);
                // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // context.startActivity(startMain);
                // System.exit(0);
            } else {// android2.1
                am.restartPackage(context.getPackageName());
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAppExit() {
        return activityStack == null || activityStack.isEmpty();
    }


    public void addTask(ExitHandler task) {
        if (null == tasks) {
            tasks = new ArrayList<ExitHandler>();
        }
        if (!tasks.contains(task)) {
            tasks.add(task);
        }

    }

    public interface ExitHandler {
        void beforeExit();
        // void afterExit();
    }
}
