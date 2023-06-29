#include "main.h"

HMODULE mod;
bool serverfail = false;

// hooks.cpp
bool Hook( );

// httpserver.cpp
void StartHTTPServer( );

// httpsserver.cpp
void StartHTTPSServer( );

void Start( )
{
	speak( "Initializing" );

	std::thread t( StartHTTPServer );
	t.detach( );

	std::thread t2( StartHTTPSServer );
	t2.detach( );

	resources::Setup( );
	
	if( !Hook( ) )
	{
		speak( "Failed to initialize" );
		util::WriteError( );
	}
	else
	{
		speak( "Initialized hooks" );

		auto start = GetTickCount64( );
		while( true )
		{
			if( serverfail ) break;

			auto diff = GetTickCount64( ) - start;
			if( diff > 15000 )
				break;

			const auto http = util::NetworkRequest( "localhost", "/test", 48888, INTERNET_FLAG_NO_CACHE_WRITE );
			const auto https = util::NetworkRequest( "localhost", "/test", 49999, INTERNET_FLAG_SECURE | INTERNET_FLAG_NO_CACHE_WRITE );

			if( http.length( ) > 5 || https.length( ) > 5 )
				break;

			Sleep( 100 );
		}

		if( serverfail )
			util::WriteError( );
		else
		{
			speak( "Started server" );
			Sleep( 1100 );
			util::WriteOk( );
		}
	}
}

BOOL WINAPI DllMain( HINSTANCE instance, DWORD reason, void* reserved )
{
	if( reason == DLL_PROCESS_ATTACH )
	{
		mod = instance;

		const auto thread = CreateThread( 0, 0, ( LPTHREAD_START_ROUTINE )Start, reserved, 0, 0 );
		if( thread )
		{
			CloseHandle( ( HANDLE )thread );
			return TRUE;
		}
	}

	return FALSE;
}
