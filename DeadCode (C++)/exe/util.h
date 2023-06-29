#pragma once
#include "main.h"

namespace util
{
	std::string CalculateMD5Hash( const void* data, const size_t size );
	void PrintOk( );
	void PrintError( );
	bool CheckCreateFolder( std::string path );
	bool CheckWriteResource( int id, std::string path );
	void WriteFile( char* bytes, int size, const char* path );
	char* OpenFile( int size, const char* path );
	bool IsPortInUse( unsigned short port );
	DWORD GetProcessID( const wchar_t* windowname, const wchar_t* processname );
	bool Inject( DWORD processid, const char* path );
	long GetFileSize( const char* path );
}
