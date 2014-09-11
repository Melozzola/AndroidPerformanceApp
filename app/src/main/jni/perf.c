/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include <jni.h>

jstring Java_silvano_riz_perfevaluation_PerfEvaluationApp_getCompileABI( JNIEnv* env, jobject thiz ) {
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
    #else
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a (hard-float)"
      #else
        #define ABI "armeabi-v7a"
      #endif
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__x86_64__)
   #define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
   #define ABI "mips64"
#elif defined(__mips__)
   #define ABI "mips"
#elif defined(__aarch64__)
   #define ABI "arm64-v8a"
#else
   #define ABI "unknown"
#endif

    return (*env)->NewStringUTF(env, "Compiled with ABI " ABI ".");
}

/*
 * This is a trivial JNI example where we use a native method to return a new VM String.
 */
jstring Java_silvano_riz_perfevaluation_Tests_returnHelloWorldStringJni( JNIEnv* env, jobject thiz ) {

    return (*env)->NewStringUTF(env, "Hello world JNI!");

}

jint Java_silvano_riz_perfevaluation_Tests_forSumArray100Jni( JNIEnv* env, jobject thiz, jint iterations ) {

    int i;
    int j;
    int k;
    int sum = 0;
    int mod;
    int array[100];

    for (i=0; i<100; i++){
        array[i] = 0;
    }

    for (j=0; j<iterations; j++){
        mod = j%100;
        //__android_log_print(ANDROID_LOG_DEBUG, "forSumArray100Jni", "mod is %d", mod);
        array[mod] = array[mod] + 1;
    }

    for (k=0; k<100; k++){
        sum +=array[k];
    }

    return sum;
}
