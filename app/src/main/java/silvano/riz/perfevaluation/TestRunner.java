package silvano.riz.perfevaluation;

import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;

/**
 * Created by mele on 09/09/2014.
 */
public class TestRunner {

    private final Handler mHandler;

    private TestRunner(){
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    private static TestRunner INSTANCE = new TestRunner();

    public static TestRunner getInstance(){
        return INSTANCE;
    }

    public void run(Tests.TestsId testId, final TestOnSeparateThreadCallback callback){
        PerfEvaluationApp.Preferences prefs = PerfEvaluationApp.getPreferences();

        if (prefs.runTestsOnUIThread){
            Stats stats = runTest(testId, prefs);
            callback.finish(stats);
        }else{
            runTestOnSeparateThread(testId, prefs, callback);
        }
    }

    private Stats runTest(Tests.TestsId testId, PerfEvaluationApp.Preferences prefs){

        Stats stats = new Stats();
        PowerManager.WakeLock cpuLock = null;

        try {
            if (prefs.useCpuLock) {
                cpuLock = PerfEvaluationApp.getPowerManager().newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, testId.name() + System.currentTimeMillis());
                cpuLock.acquire();
            }

            switch (testId) {

                case TEST_HELLO_WORLD_STRING: {
                    if (prefs.runJniTest) {
                        stats.start = System.nanoTime();
                        String message = Tests.getInstance().returnHelloWorldStringJni();
                        stats.end = System.nanoTime();
                        stats.log("returnHelloWorldStringJni - out" + message);
                    }else{
                        stats.start = System.nanoTime();
                        String message = Tests.getInstance().returnHelloWorldStringJava();
                        stats.end = System.nanoTime();
                        stats.log("returnHelloWorldStringJava - out: " + message);
                    }
                    break;
                }

                case TEST_FOR_100_SUM:{
                    long innerStart, innerStop;
                    if (prefs.runJniTest) {
                        stats.start = System.nanoTime();
                        int sum = 0;
                        for (int i=1; i<=20; i++) {
                            innerStart = System.nanoTime();
                            sum = Tests.getInstance().forSumArray100Jni(1000000);
                            innerStop = System.nanoTime();
                            stats.log(i + ") - out: " + sum +
                                    " Time taken: " + Utils.getElapsedTimeInMilliseconds(innerStart, innerStop) + " (ms)");
                        }
                        stats.end = System.nanoTime();
                        stats.log("forSumArray100Jni - out: " + sum);
                    }else{
                        stats.start = System.nanoTime();
                        int sum = 0;
                        for (int i=1; i<=20; i++) {
                            innerStart = System.nanoTime();
                            sum = Tests.getInstance().forSumArray100Java(1000000);
                            innerStop = System.nanoTime();
                            stats.log(i + ") - out: " + sum +
                                    " Time taken: " + Utils.getElapsedTimeInMilliseconds(innerStart, innerStop) + " (ms)");
                        }
                        stats.end = System.nanoTime();
                        stats.log("forSumArray100Java - out: " + sum);
                    }
                    break;
                }

                default:
                    throw new IllegalArgumentException("Unknown TestId " + testId);

            }

            return stats;
        }finally {
            if (cpuLock!=null){
                cpuLock.release();
            }
        }
    }

    private void runTestOnSeparateThread(final Tests.TestsId testId, final PerfEvaluationApp.Preferences prefs, final TestOnSeparateThreadCallback callback){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                android.os.Process.setThreadPriority(prefs.processPriority);
                final Stats stats = runTest(testId, prefs);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.finish(stats);
                    }
                });
            }
        });
        thread.start();
    }



    static class Stats{
        public long start;
        public long end;
        private StringBuffer logs;

        Stats(){
            logs = new StringBuffer();
        }

        public void log(String message){
            logs.append(String.format("%s\n", message));
        }

        public String getLog(){
            return logs.toString();
        }
    }
}
