#include "main.h"
#include "sslclasses.h"
#include "securityclass.h"

// для util::GetClassName()
bool skipmethod = false;

template< typename T >
T Hook( const char* name, void* address, void* newfunc )
{
	void* ret = nullptr;

	const auto status = MH_CreateHook( address, newfunc, ( LPVOID* )&ret );
	if( status != MH_OK )
	{
		speak( "[hook] Failed to hook %s [%s]", name, MH_StatusToString( status ) );
		return nullptr;
	}

	speak( "[hook] Hooked %s", name );
	return ( T )ret;
}

JavaVM* jvm = nullptr;
JNIEnv* env = nullptr;

typedef jmethodID( JNICALL* GetMethodIDFn )( JNIEnv*, jclass, const char*, const char* );
GetMethodIDFn original_GetMethodID;

jmethodID JNICALL hooked_GetMethodID( JNIEnv* env, jclass clazz, const char* name, const char* sig )
{
	if( skipmethod )
	{
		skipmethod = false;
		goto original;
	}

	if( clazz && name )
	{
		const auto classname = util::GetClassName( env, clazz );
		if( !strcmp( name, "<init>" ) && classname == "org.apache.http.client.methods.HttpGet" )
		{
			// это new HttpGet( "https://ссылка" )
			// добавляем дабы в будущем поменять хттпс на хттп
			// (апач хттп похуй на то что мы вырубили ссл проверки)

			auto ret = original_GetMethodID( env, clazz, name, sig );
			httpmethodhandler->AddNewMethod( ret );
			return ret;
		}
	}

original:
	return original_GetMethodID( env, clazz, name, sig );
}

typedef jmethodID( JNICALL* GetStaticMethodIDFn )( JNIEnv*, jclass, const char*, const char* );
GetStaticMethodIDFn original_GetStaticMethodID;

jmethodID JNICALL hooked_GetStaticMethodID( JNIEnv* env, jclass clazz, const char* name, const char* sig )
{
	if( name && sig )
	{
		if( !strcmp( name, "this" ) && !strcmp( sig, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;" ) )
		{
			auto ret = original_GetStaticMethodID( env, clazz, name, sig );
			strdecrypthandler->AddNewMethod( ret );
			return ret;
		}
	}

	return original_GetStaticMethodID( env, clazz, name, sig );
}

typedef void( JNICALL* CallNonvirtualVoidMethodVFn )( JNIEnv*, jobject, jclass, jmethodID, va_list );
CallNonvirtualVoidMethodVFn original_CallNonvirtualVoidMethodV;

// для нормального вызова оригинальной функции CallNonvirtualVoidMethodV
void CallOriginalCallNonvirtualVoidMethodV( JNIEnv* env, jobject obj, jclass clazz, jmethodID methodID, ... )
{
	va_list args;
	va_start( args, methodID );
	original_CallNonvirtualVoidMethodV( env, obj, clazz, methodID, args );
	va_end( args );
}

void JNICALL hooked_CallNonvirtualVoidMethodV( JNIEnv* env, jobject obj, jclass clazz, jmethodID methodID, va_list args )
{
	va_list copy;
	va_copy( copy, args );

	if( httpmethodhandler->FindMethod( methodID ) )
	{
		auto urlstr = va_arg( args, jstring );
		if( urlstr )
		{
			auto chars = env->GetStringUTFChars( urlstr, 0 );
			std::string url = chars;
			env->ReleaseStringUTFChars( urlstr, chars );

			// поменяем только начало
			url.replace( url.find( "https://" ), 8, "http://" );

			CallOriginalCallNonvirtualVoidMethodV( env, obj, clazz, methodID, env->NewStringUTF( url.c_str( ) ) );
			return;
		}
	}

	original_CallNonvirtualVoidMethodV( env, obj, clazz, methodID, copy );
}

typedef jobject( JNICALL* CallStaticObjectMethodVFn )( JNIEnv*, jclass, jmethodID, va_list );
CallStaticObjectMethodVFn original_CallStaticObjectMethodV;

jobject JNICALL hooked_CallStaticObjectMethodV( JNIEnv* env, jclass clazz, jmethodID methodID, va_list args )
{
	if( strdecrypthandler->FindMethod( methodID ) )
	{
		auto decryptedstring = original_CallStaticObjectMethodV( env, clazz, methodID, args );
		if( !decryptedstring ) return decryptedstring;

		auto utfchars = env->GetStringUTFChars( ( jstring )decryptedstring, 0 );
		std::string str = utfchars;
		env->ReleaseStringUTFChars( ( jstring )decryptedstring, utfchars );

		if( str.find( "EB01A5A4-5ABF" ) != std::string::npos )
			return ( jobject )env->NewStringUTF( "PlutoSolutions</>2</>0</>EB01A5A4-5ABF-4524-9FC7-314721E94568</>12-31-9999-00-00-00" );

		return decryptedstring;
	}

	return original_CallStaticObjectMethodV( env, clazz, methodID, args );
}

typedef jclass( JNICALL* DefineClassFn )( JNIEnv*, jobject, jstring, jbyteArray, jint, jint, jobject, jstring );
DefineClassFn original_DefineClass;

jclass JNICALL hooked_DefineClass( JNIEnv* env, jobject loader, jstring name, jbyteArray data, jint offset, jint length, jobject pd, jstring source )
{
	if( length == 709 )
	{
		jbyte* bytes = ( jbyte* )( malloc( length ) );
		env->GetByteArrayRegion( data, offset, length, bytes );

		static char find[ ] = { 'A', 'p', 'p', 'l', 'e', 'W', 'e', 'b', 'K', 'i', 't', '/', '5', '3', '7' };
		bool found = false;

		for( int i = 0; i < length - sizeof( find ); i++ )
		{
			bool f = true;

			for( int j = 0; j < sizeof( find ); j++ )
			{
				if( bytes[ i + j ] != find[ j ] )
				{
					f = false;
					break;
				}
			}

			if( f )
			{
				found = true;
				break;
			}
		}

		free( bytes );
		bytes = nullptr;

		if( found )
		{
			speak( "[hook] Replacing security class" );

			jbyteArray bytes = env->NewByteArray( sizeof( SecurityClass ) );
			env->SetByteArrayRegion( bytes, 0, sizeof( SecurityClass ), ( jbyte* )SecurityClass );

			return original_DefineClass( env, loader, name, bytes, offset, sizeof( SecurityClass ), pd, source );
		}
	}

	return original_DefineClass( env, loader, name, data, offset, length, pd, source );
}

bool HookJVM( )
{
	// сначала найдем джаву
	const auto jvmdll = GetModuleHandleA( "jvm.dll" );
	if( !jvmdll )
	{
		// братишка ты еблан????
		speak( "[hook] Failed to find jvm.dll" );
		return false;
	}

	// надо для хука DefineClass позже
	const auto javadll = GetModuleHandleA( "java.dll" );
	if( !javadll )
	{
		// братишка ты еблан????
		speak( "[hook] Failed to find java.dll" );
		return false;
	}

	const auto defineClass1 = GetProcAddress( javadll, "Java_java_lang_ClassLoader_defineClass1" );
	if( !defineClass1 )
	{
		speak( "[hook] Failed to find Java_java_lang_ClassLoader_defineClass1 in java.dll" );
		return false;
	}

	const auto JNI_GetCreatedJavaVMsAddress = GetProcAddress( jvmdll, "JNI_GetCreatedJavaVMs" );
	if( !JNI_GetCreatedJavaVMsAddress )
	{
		speak( "[hook] Failed to find JNI_GetCreatedJavaVMs in jvm.dll" );
		return false;
	}

	typedef jint( JNICALL* GetCreatedJavaVMsFn )( JavaVM**, jsize, jsize* );
	auto JNI_GetCreatedJavaVMs = ( GetCreatedJavaVMsFn )JNI_GetCreatedJavaVMsAddress;

	jsize vmcount = 0;
	JNI_GetCreatedJavaVMs( 0, 0, &vmcount );
	auto vms = new JavaVM*[ vmcount ];
	JNI_GetCreatedJavaVMs( vms, vmcount, &vmcount );

	if( !vms || vmcount == 0 )
	{
		speak( "[hook] Failed to find JavaVM" );
		return false;
	}

	jvm = vms[ 0 ];

	jvm->AttachCurrentThread( ( void** )&env, 0 );
	jvm->GetEnv( ( void** )&env, JNI_VERSION_1_8 );

	if( !env )
	{
		speak( "[hook] Failed to find JNIEnv" );
		jvm->DetachCurrentThread( );
		return false;
	}

	// получаем версию джавы
	const auto System = env->FindClass( "java/lang/System" );
	const auto getProperty = env->GetStaticMethodID( System, "getProperty", "(Ljava/lang/String;)Ljava/lang/String;" );
	const auto javaversion_jstr = ( jstring )env->CallStaticObjectMethod( System, getProperty, env->NewStringUTF( "java.version" ) );

	// конвертируем в std::string
	const auto cstrversion = env->GetStringUTFChars( javaversion_jstr, 0 );
	std::string version = cstrversion;
	env->ReleaseStringUTFChars( javaversion_jstr, cstrversion );

	// если джава хуевая (в тл легаси по умолчанию стоит 8u51) то говорим юзеру прямо У ТЕБЯ ДЖАВА ХУЙНЯ ИДИ МЕНЯЙ
	if( version == "1.8.0_51" )
	{
		util::WriteError( );
		speak( "Bad java version" );
		MessageBoxW( 0, L"Ваша версия Java не работает с DeadCode. Скачайте новую с https://crystalpvp.ru/deadcode/java.php\n"
						L"Гайд по установке в TL Legacy: https://crystalpvp.ru/deadcode/guide.png",
						L"jewnative - error", MB_OK | MB_ICONWARNING | MB_SYSTEMMODAL );
		exit( 1 );
		return false;
	}
	
	// ищем класслоадер майна в который зарегистрируем классы для отключения ссл проверок
	// класслоадер можно и не искать, но мне похуй у меня уже есть код для этого
	const auto Launch = env->FindClass( "net/minecraft/launchwrapper/Launch" );
	if( !Launch )
	{
		speak( "[hook] Failed to find net.minecraft.launchwrapper.Launch class" );
		jvm->DetachCurrentThread( );
		return false;
	}

	const auto Launch_classLoader = env->GetStaticObjectField(
		Launch, env->GetStaticFieldID( Launch, "classLoader", "Lnet/minecraft/launchwrapper/LaunchClassLoader;" ) );
	if( !Launch_classLoader )
	{
		speak( "[hook] Failed to find 'classLoader' field in net.minecraft.launchwrapper.Launch" );
		jvm->DetachCurrentThread( );
		return false;
	}

	// регистрируем нужные классы
	if( !env->DefineClass( "me/mrnv/CustomHostnameVerifier", Launch_classLoader, ( jbyte* )CustomHostnameVerifier, sizeof( CustomHostnameVerifier ) ) )
	{
		speak( "[hook] Failed to define 'me.mrnv.CustomHostnameVerifier' class" );
		jvm->DetachCurrentThread( );
		return false;
	}

	if( !env->DefineClass( "me/mrnv/EmptyTrustManager", Launch_classLoader, ( jbyte* )EmptyTrustManager, sizeof( EmptyTrustManager ) ) )
	{
		speak( "[hook] Failed to define 'me.mrnv.EmptyTrustManager' class" );
		if( env->ExceptionCheck( ) )
			env->ExceptionDescribe( );
		jvm->DetachCurrentThread( );
		return false;
	}

	auto DisableSSLCheck_class = env->DefineClass( "me/mrnv/DisableSSLCheck", Launch_classLoader, ( jbyte* )DisableSSLCheck, sizeof( DisableSSLCheck ) );
	if( !DisableSSLCheck_class )
	{
		speak( "[hook] Failed to define 'me.mrnv.DisableSSLCheck' class" );
		if( env->ExceptionCheck( ) )
			env->ExceptionDescribe( );
		jvm->DetachCurrentThread( );
		return false;
	}

	// отключаем ссл проверки с помощью классов которые мы только что зарегистрировали
	const auto sslresult = env->CallStaticBooleanMethod( DisableSSLCheck_class,
														 env->GetStaticMethodID( DisableSSLCheck_class, "disable", "()Z" ) );
	if( !sslresult )
	{
		speak( "[hook] Failed to disable SSL checks" );
		jvm->DetachCurrentThread( );
		return false;
	}

	// теперь можно и похукать хуйню
	// эта функция вызывается после MH_Initialize (в hooks.cpp) так что minhook во второй раз инициализировать не надо

	original_GetMethodID = Hook< GetMethodIDFn >( "GetMethodID", env->functions->GetMethodID, hooked_GetMethodID );
	if( !original_GetMethodID ) return false;

	original_GetStaticMethodID = Hook< GetStaticMethodIDFn >( "GetStaticMethodID", env->functions->GetStaticMethodID, hooked_GetStaticMethodID );
	if( !original_GetStaticMethodID ) return false;

	original_CallNonvirtualVoidMethodV = Hook< CallNonvirtualVoidMethodVFn >( "CallNonvirtualVoidMethodV",
																			  env->functions->CallNonvirtualVoidMethodV, hooked_CallNonvirtualVoidMethodV );
	if( !original_CallNonvirtualVoidMethodV ) return false;

	original_CallStaticObjectMethodV = Hook< CallStaticObjectMethodVFn >( "CallStaticObjectMethodV",
																		  env->functions->CallStaticObjectMethodV, hooked_CallStaticObjectMethodV );
	if( !original_CallStaticObjectMethodV ) return false;

	original_DefineClass = Hook< DefineClassFn >( "DefineClass", defineClass1, hooked_DefineClass );
	if( !original_DefineClass ) return false;

	// все заебок бляяяя
	return true;
}
