#pragma once
#include <Windows.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <thread>

#include <WinInet.h>
#pragma comment( lib, "Wininet.lib" )

#pragma warning( disable : 4302 4311 4312 )

#include "minhook/MinHook.h"

#include "java/jni.h"
#include "java/jni_md.h"
#include "java/jvmti.h"

#include "methodhandler.h"

extern void speak( const char* text, ... );

extern HMODULE mod;
extern bool skipmethod;
extern bool serverfail;

#include "resourcehandler.h"
#include "util.h"
