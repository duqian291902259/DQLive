#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_duqian_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++,DQ";
    return env->NewStringUTF(hello.c_str());
}