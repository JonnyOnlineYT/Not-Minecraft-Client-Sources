#pragma once
#include "main.h"

#ifdef GetClassName
#undef GetClassName
#endif

namespace util
{
	std::string GetClassName( JNIEnv* env, jclass clazz );

	void WriteError( );
	void WriteOk( );

	char* GetResource( int id, const char* name, DWORD& sizeout );

	std::string Convert( unsigned char* what, size_t size );
	std::string MakeResponse( std::string target, std::string& type );
	std::string NetworkRequest( const std::string& host, const std::string& path, int port, DWORD flags );
}
