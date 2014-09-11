package silvano.riz.perfevaluation;

/**
 * Created by mele on 09/09/2014.
 */
public class Tests {

    static {
        System.loadLibrary("perf");
    }

    public enum TestsId{
        TEST_HELLO_WORLD_STRING,
        TEST_FOR_100_SUM;
    }

    private void Tests(){};
    private static final Tests INSTANCE = new Tests();

    public static Tests getInstance(){
        return INSTANCE;
    }

    // TEST_HELLO_WORLD_STRING
    public native String returnHelloWorldStringJni();
    public String returnHelloWorldStringJava(){
        return "Hello world JAVA!";
    }

    public native int forSumArray100Jni(int iterations);
    public int forSumArray100Java(int iterations){
        int i;
        int j;
        int k;
        int sum = 0;
        int mod;
        int[] array = new int[100];

        for (i=0; i<100; i++){
            array[i] = 0;
        }

        for (j=0; j<iterations; j++){
            mod = j%100;
            array[mod] = array[mod] + 1;
        }

        for (k=0; k<100; k++){
            sum +=array[k];
        }

        return sum;
    }

}
