package silvano.riz.perfevaluation;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by mele on 10/09/2014.
 */
public class PerfEvaluationApp extends Application{

    private static final String LOG_TAG = PerfEvaluationApp.class.getSimpleName();

    static {
        System.loadLibrary("perf");
    }

    private static Context appContext;
    private static Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        PerfEvaluationApp.appContext = getApplicationContext();
        updatePreferences();

        Log.i(LOG_TAG, getCompileABI());
    }

    public static PowerManager getPowerManager(){
        return (PowerManager)appContext.getSystemService(Context.POWER_SERVICE);
    }

    public static synchronized void updatePreferences(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(appContext);
        boolean useCpuLock = sharedPref.getBoolean(SettingsActivity.PREF_LOCK_CPU, true);
        boolean runJniTest = sharedPref.getBoolean(SettingsActivity.PREF_NATIVE, true);
        boolean runTestsOnUIThread = sharedPref.getBoolean(SettingsActivity.PREF_ON_UI_THREAD, true);
        int processPriority = Integer.parseInt(sharedPref.getString(SettingsActivity.PREF_PROCESS_PRIORITY, "-20"));

        preferences = new Preferences(runJniTest, runTestsOnUIThread, processPriority, useCpuLock);
    }

    public static synchronized Preferences getPreferences(){
        return preferences;
    }

    private native String getCompileABI();

    static class Preferences{
        final boolean runJniTest;
        final boolean runTestsOnUIThread;
        final int processPriority;
        final boolean useCpuLock;

        Preferences(boolean runJniTest, boolean runTestsOnUIThread, int processPriority, boolean useCpuLock) {
            this.runJniTest = runJniTest;
            this.runTestsOnUIThread = runTestsOnUIThread;
            this.processPriority = processPriority;
            this.useCpuLock = useCpuLock;
        }

    }
}
