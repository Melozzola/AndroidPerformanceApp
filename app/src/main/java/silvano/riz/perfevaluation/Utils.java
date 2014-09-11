package silvano.riz.perfevaluation;

import java.util.concurrent.TimeUnit;

/**
 * Created by mele on 11/09/2014.
 */
public class Utils {

    private Utils(){};

    static long getElapsedTimeInMilliseconds(long nanoStart, long nanoEnd){
        return TimeUnit.MILLISECONDS.convert((nanoEnd - nanoStart), TimeUnit.NANOSECONDS);
        //return (nanoEnd - nanoStart)/ 1000000000.0;
    }

}
