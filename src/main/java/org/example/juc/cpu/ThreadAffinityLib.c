#include <jni.h>
#include <pthread.h>
#include <sched.h>
#include <stdio.h>
#include "ThreadAffinity.h" // 生成的头文件，具体名称根据 Java 类名可能不同

JNIEXPORT void JNICALL Java_ThreadAffinity_bindThreadToCpu(JNIEnv *env, jobject obj, jint cpuId) {
    cpu_set_t cpuset;
    CPU_ZERO(&cpuset);
    CPU_SET(cpuId, &cpuset);

    pthread_t current_thread = pthread_self(); // 获取当前线程

    // 设定线程亲和性
    int result = pthread_setaffinity_np(current_thread, sizeof(cpu_set_t), &cpuset);
    if (result != 0) {
        perror("pthread_setaffinity_np");
    }
}
// gcc -shared -fPIC -o libThreadAffinityLib.so -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux ThreadAffinityLib.c -lpthread
