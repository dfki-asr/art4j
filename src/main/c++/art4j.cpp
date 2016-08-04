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

using namespace std;


void Java_de_dfki_resc28_art4j_DTrackSDK_initialise (JNIEnv *env, jobject obj)
{
    unsigned short port = 5000;
    DTrackSDK* dt = new DTrackSDK(port);

    if(!dt->isLocalDataPortValid()){
        cout << "DTrack init error" << endl;
    }
    
    setHandle(env, obj, dt);
}

void Java_de_dfki_resc28_art4j_DTrackSDK_initialise__Ljava_lang_String_2II (JNIEnv *env, jobject obj, jstring serverHost, jint serverPort, jint dataPort)
{
    unsigned short sPort = serverPort;
    unsigned short dPort = dataPort;
    const char *sHost = env->GetStringUTFChars(serverHost, 0);
    
    DTrackSDK* dt = new DTrackSDK(sHost, sPort, dPort);
    setHandle(env, obj, dt);
    env->ReleaseStringUTFChars(serverHost, sHost);
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getLastDataError(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getLastDataError();

}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getLastServerError(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getLastServerError();

}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getLastDTrackError(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getLastDTrackError();
}

jstring Java_de_dfki_resc28_art4j_DTrackSDK_getLastDTrackErrorDescription (JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return env->NewStringUTF(dt->getLastDTrackErrorDescription().c_str());
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getRemoteSystemType(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getRemoteSystemType();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_sendCommand(JNIEnv *env, jobject obj, jstring command)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    const char *commandStr = env->GetStringUTFChars(command, 0);
    return (jboolean)dt->sendCommand(commandStr ? commandStr : "");
}

jobject JNICALL Java_de_dfki_resc28_art4j_DTrackSDK_sendDTrack2Command(JNIEnv *env, jobject obj, jstring command)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    const char *commandStr = env->GetStringUTFChars(command, 0);

    std::string answer;
    int result = dt->sendDTrack2Command(commandStr ? commandStr : "", &answer);

    jstring janswer = env->NewStringUTF(answer.c_str());

    jclass cls = env->FindClass("de/dfki/resc28/art4j/DTrackSDK$CommandResult");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(ILjava/lang/String;)V");
    jobject jresult = env->NewObject(cls, methodID, (jint)result, janswer);

    return jresult;
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getDataPort(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getDataPort();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_isLocalDataPortValid(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jboolean)dt->isLocalDataPortValid();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_isUDPValid(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jboolean)dt->isUDPValid();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_isCommandInterfaceValid(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jboolean)dt->isCommandInterfaceValid();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_isTCPValid(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jboolean)dt->isTCPValid();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_receive(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
//    dt->receive();
//    setHandle(env, obj, dt);
    return (jboolean)dt->receive();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_startMeasurement(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jboolean)dt->startMeasurement();
}

jboolean Java_de_dfki_resc28_art4j_DTrackSDK_stopMeasurement(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jboolean)dt->stopMeasurement();
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getFrameCounter(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getFrameCounter();
}

jdouble Java_de_dfki_resc28_art4j_DTrackSDK_getTimeStamp(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jdouble)dt->getTimeStamp();
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getNumMarker(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getNumMarker();
}

jobject Java_de_dfki_resc28_art4j_DTrackSDK_getMarker(JNIEnv *env, jobject obj, jint index)
{
    DTrackSDK* tracker = getHandle<DTrackSDK>(env,obj);
    DTrack_Marker_Type_d marker = *tracker->getMarker(index);

    jclass cls = env->FindClass("de/dfki/resc28/art4j/targets/Marker");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(ID[D)V");
    jobject a= env->NewObject(cls, methodID, marker.id, marker.quality, marker.loc);
    
    return a;
}

jint Java_de_dfki_resc28_art4j_DTrackSDK_getNumBody(JNIEnv *env, jobject obj)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    return (jint)dt->getNumBody();
}

jobject Java_de_dfki_resc28_art4j_DTrackSDK_getBody(JNIEnv *env, jobject obj, jint index)
{
    DTrackSDK* dt = getHandle<DTrackSDK>(env,obj);
    
    DTrack_Body_Type_d body = *dt->getBody(index);
    
    jboolean isCopy1;
    jdoubleArray location = env -> NewDoubleArray(3);
    jdouble* destArrayElems = env -> GetDoubleArrayElements(location, &isCopy1);
    for (int i = 0; i < 3; i++) {
        destArrayElems[i] = (double) body.loc[i];
    }
    if (isCopy1 == JNI_TRUE) {
        env -> ReleaseDoubleArrayElements(location, destArrayElems, 0);
    }

    jboolean isCopy2;
    jdoubleArray rotation = env -> NewDoubleArray(9);
    jdouble* destArrayElems2 = env -> GetDoubleArrayElements(rotation, &isCopy2);
    for (int i = 0; i < 9; i++) {
        destArrayElems2[i] = (double) body.rot[i];
    }
    if (isCopy2 == JNI_TRUE) {
        env -> ReleaseDoubleArrayElements(rotation, destArrayElems2, 0);
    }


    jclass cls = env->FindClass("de/dfki/resc28/art4j/targets/Body");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(ID[D[D)V");
    jobject a= env->NewObject(cls, methodID, body.id, body.quality, location, rotation);
    
    return a;
}
