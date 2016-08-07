/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

#include "de_dfki_resc28_art4j_DTrackSDK.h"
#include "handle.h"

#include "DTrackSDK.hpp"
#include "DTrackParse.hpp"
#include "DTrackDataTypes.h"
#include "DTrackNet.h"
#include "DTrackParser.hpp"

#include <unistd.h>
#include <stdio.h>
#include <sstream>
#include <iostream>
#include <memory>

using namespace std;



class JNIString
{
public:

    JNIString(JNIEnv *env, jstring str)
    {
        if (!str)
            return;
        const char *s = env->GetStringUTFChars(str, 0);
        if (!s)
            return;
        str_.reset(new std::string(s));
        env->ReleaseStringUTFChars(str, s);
    }

    explicit operator bool() const {
          return str_.operator bool();
    }

    std::string * get() const
    {
        return str_.get();
    }

private:
    std::unique_ptr<std::string> str_;
};

jint jniThrowNullPointerException(JNIEnv *env, const char *message)
{
    jclass exClass = env->FindClass("java/lang/NullPointerException");
    if (! exClass)
        return -1;

    return env->ThrowNew( exClass, message );
}

jint jniThrowIllegalArgumentException(JNIEnv *env, const char *message)
{
    jclass exClass = env->FindClass("java/lang/IllegalArgumentException");
    if (! exClass)
        return -1;

    return env->ThrowNew( exClass, message );
}

jlong Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1create__I(JNIEnv *env, jclass cls, jint dataPort)
{
    DTrackSDK* dt = new DTrackSDK((unsigned short)dataPort);
    return handle_to(dt);
}

jlong Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1create__Ljava_lang_String_2II(JNIEnv *env, jclass cls, jstring serverHost, jint serverPort, jint dataPort)
{
    DTrackSDK *dt = 0;
    unsigned short sPort = (unsigned short)serverPort;
    unsigned short dPort = (unsigned short)dataPort;

    JNIString sHost(env, serverHost);

    if (env->ExceptionCheck())
        return handle_to(dt);

    if (!sHost)
    {
        jniThrowNullPointerException(env, "serverHost is null");
        return handle_to(dt);
    }

    dt = new DTrackSDK(*sHost.get(), sPort, dPort);
    return handle_to(dt);
}

jlong JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1destroy(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK *dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
        jniThrowIllegalArgumentException(env, "DTrackSDK object already destroyed");
    else
        delete dt;
    return handle_to<DTrackSDK>(0);
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getLastDataError(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getLastDataError();
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getLastServerError(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getLastServerError();
}

JNIEXPORT jint JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getLastDTrackError(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getLastDTrackError();
}

jstring Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getLastDTrackErrorDescription (JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return env->NewStringUTF(dt->getLastDTrackErrorDescription().c_str());
}

JNIEXPORT jint JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getRemoteSystemType(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getRemoteSystemType();
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1sendCommand(JNIEnv *env, jclass cls, jlong handle, jstring command)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }

    JNIString commandStr(env, command);

    if (env->ExceptionCheck())
        return (jboolean)0;

    if (!commandStr)
    {
        jniThrowNullPointerException(env, "command is null");
        return (jboolean)0;
    }

    bool result = dt->sendCommand(*commandStr.get());

    return (jboolean)result;
}

JNIEXPORT jobject JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1sendDTrack2Command(JNIEnv *env, jclass cls, jlong handle, jstring command)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jobject)0;
    }

    JNIString commandStr(env, command);

    if (env->ExceptionCheck())
        return (jobject)0;

    if (!commandStr)
    {
        jniThrowNullPointerException(env, "command is null");
        return (jobject)0;
    }

    std::string answer;
    int result = dt->sendDTrack2Command(*commandStr.get(), &answer);

    jstring janswer = env->NewStringUTF(answer.c_str());

    jclass resultCls = env->FindClass("de/dfki/resc28/art4j/DTrackSDK$CommandResult");
    jmethodID methodID = env->GetMethodID(resultCls, "<init>", "(ILjava/lang/String;)V");
    jobject jresult = env->NewObject(resultCls, methodID, (jint)result, janswer);

    return jresult;
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1setParam__JLjava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2
(JNIEnv *env, jclass cls, jlong handle, jstring category, jstring name, jstring value)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }

    JNIString categoryStr(env, category);

    if (env->ExceptionCheck())
        return (jboolean)0;

    if (!categoryStr)
    {
        jniThrowNullPointerException(env, "category is null");
        return (jboolean)0;
    }


    JNIString nameStr(env, name);

    if (env->ExceptionCheck())
        return (jboolean)0;

    if (!nameStr)
    {
        jniThrowNullPointerException(env, "name is null");
        return (jboolean)0;
    }

    JNIString valueStr(env, value);

    if (env->ExceptionCheck())
        return (jboolean)0;

    if (!valueStr)
    {
        jniThrowNullPointerException(env, "value is null");
        return (jboolean)0;
    }

    return (jboolean)dt->setParam(*categoryStr.get(), *nameStr.get(), *valueStr.get());
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1setParam__JLjava_lang_String_2
(JNIEnv *env, jclass cls, jlong handle, jstring parameter)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }

    JNIString parameterStr(env, parameter);

    if (env->ExceptionCheck())
        return (jboolean)0;

    if (!parameterStr)
    {
        jniThrowNullPointerException(env, "parameter is null");
        return (jboolean)0;
    }

    return (jboolean)dt->setParam(*parameterStr.get());
}

JNIEXPORT jstring JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getParam__JLjava_lang_String_2Ljava_lang_String_2
(JNIEnv *env, jclass cls, jlong handle, jstring category, jstring name)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jstring)0;
    }

    JNIString categoryStr(env, category);

    if (env->ExceptionCheck())
        return (jstring)0;

    if (!categoryStr)
    {
        jniThrowNullPointerException(env, "category is null");
        return (jstring)0;
    }

    JNIString nameStr(env, name);

    if (env->ExceptionCheck())
        return (jstring)0;

    if (!nameStr)
    {
        jniThrowNullPointerException(env, "name is null");
        return (jstring)0;
    }

    std::string value;
    const bool result = dt->getParam(*categoryStr.get(), *nameStr.get(), value);

    return result ? env->NewStringUTF(value.c_str()) : 0;
}

JNIEXPORT jstring JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getParam__JLjava_lang_String_2
(JNIEnv *env, jclass jclass, jlong handle, jstring parameter)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jstring)0;
    }

    JNIString parameterStr(env, parameter);

    if (env->ExceptionCheck())
        return (jstring)0;

    if (!parameterStr)
    {
        jniThrowNullPointerException(env, "parameter is null");
        return (jstring)0;
    }

    std::string value;
    const bool result = dt->getParam(*parameterStr.get(), value);

    return result ? env->NewStringUTF(value.c_str()) : 0;
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getDataPort(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }

    return (jint)dt->getDataPort();
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1isLocalDataPortValid(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->isLocalDataPortValid();
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1isUDPValid(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->isUDPValid();
}

JNIEXPORT jboolean JNICALL  Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1isCommandInterfaceValid(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->isCommandInterfaceValid();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1isTCPValid(JNIEnv *env, jclass cls, jlong handle)
{
    const DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->isTCPValid();
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1receive
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->receive();
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1startMeasurement
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->startMeasurement();
}

JNIEXPORT jboolean JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1stopMeasurement
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jboolean)0;
    }
    return (jboolean)dt->stopMeasurement();
}

JNIEXPORT jint JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getFrameCounter
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getFrameCounter();
}

JNIEXPORT jdouble JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getTimeStamp
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jdouble)dt->getTimeStamp();
}

JNIEXPORT jint JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getNumMarker
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getNumMarker();
}

JNIEXPORT jobject JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getMarker
(JNIEnv *env, jclass cls, jlong handle, jint index)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jobject)0;
    }
    const DTrack_Marker_Type_d *marker = dt->getMarker(index);
    if (!marker)
    {
        jniThrowIllegalArgumentException(env, "Invalid marker index");
        return (jobject)0;
    }

    jclass markerCls = env->FindClass("de/dfki/resc28/art4j/targets/Marker");
    jmethodID methodID = env->GetMethodID(markerCls, "<init>", "(ID[D)V");
    jobject a = env->NewObject(markerCls, methodID, marker->id, marker->quality, marker->loc);
    
    return a;
}

JNIEXPORT jint JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getNumBody
(JNIEnv *env, jclass cls, jlong handle)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    return (jint)dt->getNumBody();
}

JNIEXPORT jobject JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_DTrackSDK_1getBody
(JNIEnv *env, jclass cls, jlong handle, jint index)
{
    DTrackSDK* dt = handle_cast<DTrackSDK>(handle);
    if (!dt)
    {
        jniThrowIllegalArgumentException(env, "DTrackSDK object is destroyed");
        return (jint)0;
    }
    
    const DTrack_Body_Type_d *body = dt->getBody(index);
    if (!body)
    {
        jniThrowIllegalArgumentException(env, "Invalid body index");
        return (jobject)0;
    }

    jboolean isCopy1;
    jdoubleArray location = env -> NewDoubleArray(3);
    jdouble* destArrayElems = env -> GetDoubleArrayElements(location, &isCopy1);
    for (int i = 0; i < 3; i++) {
        destArrayElems[i] = (double) body->loc[i];
    }
    if (isCopy1 == JNI_TRUE) {
        env -> ReleaseDoubleArrayElements(location, destArrayElems, 0);
    }

    jboolean isCopy2;
    jdoubleArray rotation = env -> NewDoubleArray(9);
    jdouble* destArrayElems2 = env -> GetDoubleArrayElements(rotation, &isCopy2);
    for (int i = 0; i < 9; i++) {
        destArrayElems2[i] = (double) body->rot[i];
    }
    if (isCopy2 == JNI_TRUE) {
        env -> ReleaseDoubleArrayElements(rotation, destArrayElems2, 0);
    }


    jclass bodyCls = env->FindClass("de/dfki/resc28/art4j/targets/Body");
    jmethodID methodID = env->GetMethodID(bodyCls, "<init>", "(ID[D[D)V");
    jobject a = env->NewObject(bodyCls, methodID, body->id, body->quality, location, rotation);
    
    return a;
}
