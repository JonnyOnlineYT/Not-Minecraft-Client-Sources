#define WIN32_LEAN_AND_MEAN
#define _WINSOCK_DEPRECATED_NO_WARNINGS
#include "main.h"
#include <winsock2.h>
#include <WS2tcpip.h>
#pragma comment( lib, "Ws2_32.lib" )

template< typename T >
T HookAPI( const wchar_t* dll, const char* func, void* newfunc )
{
	if( !GetModuleHandleW( dll ) )
	{
		speak( "[hook] Loading %ws", dll );
		if( !LoadLibraryW( dll ) )
		{
			speak( "[hook] Failed to load %ws", dll );
			return nullptr;
		}
	}

	void* ret = nullptr;

	const auto result = MH_CreateHookApi( dll, func, newfunc, ( LPVOID* )&ret );
	if( result != MH_OK )
	{
		speak( "[hook] Failed to create hook on [%ws / %s] -> [%s]", dll, func, MH_StatusToString( result ) );
		return nullptr;
	}

	return ( T )ret;
}

typedef int( WSAAPI* getaddrinfoFn )( PCSTR, PCSTR, const ADDRINFOA*, PADDRINFOA* );
getaddrinfoFn original_getaddrinfo;

int WSAAPI hooked_getaddrinfo( PCSTR pNodeName, PCSTR pServiceName, const ADDRINFOA* pHints, PADDRINFOA* ppResult )
{
	if( !pNodeName ) goto original;

	/*speak( "getaddrinfo call" );
	speak( "pNodeName -> [%s]", pNodeName );*/

	if( !strcmp( pNodeName, "deadcodehack.org" ) )
		return original_getaddrinfo( "localhost", pServiceName, pHints, ppResult );

original:
	return original_getaddrinfo( pNodeName, pServiceName, pHints, ppResult );
}

typedef hostent*( WSAAPI* gethostbynameFn )( const char* );
gethostbynameFn original_gethostbyname;

hostent* WSAAPI hooked_gethostbyname( const char* name )
{
	if( !name ) return original_gethostbyname( name );

	//speak( "gethostbyname call" );
	//speak( "[%s]", name );

	if( !strcmp( name, "api.ipify.org" ) )
		return original_gethostbyname( "localhost" );

	return original_gethostbyname( name );
}

typedef int( WSAAPI* GetAddrInfoExWFn )( PCWSTR, PCWSTR, DWORD, LPGUID, const ADDRINFOEXW*, PADDRINFOEXW*, timeval*, LPOVERLAPPED, LPLOOKUPSERVICE_COMPLETION_ROUTINE, LPHANDLE );
GetAddrInfoExWFn original_GetAddrInfoExW;

int WSAAPI hooked_GetAddrInfoExW( PCWSTR pName, PCWSTR pServiceName, DWORD dwNameSpace, LPGUID lpNspId, const ADDRINFOEXW* hints,
								  PADDRINFOEXW* ppResult, timeval* timeout, LPOVERLAPPED lpOverlapped,
								  LPLOOKUPSERVICE_COMPLETION_ROUTINE lpCompletionRoutine, LPHANDLE lpNameHandle )
{
	if( !pName ) goto original;

	/*speak( "GetAddrInfoExW call" );
	speak( "[%ws]", pName );*/

	if( !wcscmp( pName, L"deadcodehack.org" ) )
		return original_GetAddrInfoExW( L"localhost", pServiceName, dwNameSpace, lpNspId, hints, ppResult, timeout, lpOverlapped, lpCompletionRoutine, lpNameHandle );

original:
	return original_GetAddrInfoExW( pName, pServiceName, dwNameSpace, lpNspId, hints, ppResult, timeout, lpOverlapped, lpCompletionRoutine, lpNameHandle );
}

typedef int( WINAPI* connectFn )( SOCKET, const sockaddr*, int );
connectFn original_connect;

int WINAPI hooked_connect( SOCKET socket, const sockaddr* name, int namelen )
{
	if( !name ) return original_connect( socket, name, namelen );

	sockaddr_in sin;
	memcpy( &sin, name, sizeof( sin ) );

	const auto ip = inet_ntoa( sin.sin_addr );
	const auto port = ntohs( sin.sin_port );

	if( ip && ( port == 80 || port == 443 ) )
	{
		std::string strip = ip;
		if( strip.starts_with( "192.168." ) || strip == "127.0.0.1" || strip == "0.0.0.0" )
		{
			if( port == 80 )
				sin.sin_port = ntohs( 48888 );
			else if( port == 443 )
				sin.sin_port = ntohs( 49999 );

			return original_connect( socket, ( sockaddr* )&sin, namelen );
		}
	}

	return original_connect( socket, name, namelen );
}

typedef HINTERNET( WINAPI* InternetConnectAFn )( HINTERNET, LPCSTR, INTERNET_PORT, LPCSTR, LPCSTR, DWORD, DWORD, DWORD_PTR );
InternetConnectAFn original_InternetConnectA;

HINTERNET WINAPI hooked_InternetConnectA( HINTERNET hConnect, LPCSTR lpszServerName, INTERNET_PORT nServerPort, LPCSTR lpszUserName, LPCSTR lpszPassword, DWORD dwService, DWORD dwFlags, DWORD_PTR dwContext )
{
	if( !strcmp( lpszServerName, "deadcodehack.org" ) ||
		!strcmp( lpszServerName, "localhost" ) ||
		!strcmp( lpszServerName, "127.0.0.1" ) ||
		!strcmp( lpszServerName, "api.ipify.org" ) )
	{
		if( nServerPort == 80 || nServerPort == 443 )
		{
			INTERNET_PORT newport = nServerPort;
			if( nServerPort == 80 )
				newport = 48888;
			else if( nServerPort == 443 )
				newport = 49999;

			return original_InternetConnectA( hConnect, lpszServerName, newport, lpszUserName, lpszPassword, dwService, dwFlags, dwContext );
		}
	}

	return original_InternetConnectA( hConnect, lpszServerName, nServerPort, lpszUserName, lpszPassword, dwService, dwFlags, dwContext );
}

typedef HINTERNET( WINAPI* InternetConnectWFn )( HINTERNET, LPCWSTR, INTERNET_PORT, LPCWSTR, LPCWSTR, DWORD, DWORD, DWORD_PTR );
InternetConnectWFn original_InternetConnectW;

HINTERNET WINAPI hooked_InternetConnectW( HINTERNET hConnect, LPCWSTR lpszServerName, INTERNET_PORT nServerPort, LPCWSTR lpszUserName, LPCWSTR lpszPassword, DWORD dwService, DWORD dwFlags, DWORD_PTR dwContext )
{
	if( !wcscmp( lpszServerName, L"deadcodehack.org" ) ||
		!wcscmp( lpszServerName, L"localhost" ) ||
		!wcscmp( lpszServerName, L"127.0.0.1" ) ||
		!wcscmp( lpszServerName, L"api.ipify.org" ) )
	{
		if( nServerPort == 80 || nServerPort == 443 )
		{
			INTERNET_PORT newport = nServerPort;
			if( nServerPort == 80 )
				newport = 48888;
			else if( nServerPort == 443 )
				newport = 49999;

			return original_InternetConnectW( hConnect, lpszServerName, newport, lpszUserName, lpszPassword, dwService, dwFlags, dwContext );
		}
	}

	return original_InternetConnectW( hConnect, lpszServerName, nServerPort, lpszUserName, lpszPassword, dwService, dwFlags, dwContext );
}

// gamehooks.cpp
bool HookJVM( );

bool Hook( )
{
	const auto init = MH_Initialize( );
	if( init != MH_OK )
	{
		speak( "[hook] Failed to initialize MinHook [%s]", MH_StatusToString( init ) );
		return false;
	}

	original_getaddrinfo = HookAPI< getaddrinfoFn >( L"ws2_32.dll", "getaddrinfo", hooked_getaddrinfo );
	if( !original_getaddrinfo ) return false;

	original_gethostbyname = HookAPI< gethostbynameFn >( L"ws2_32.dll", "gethostbyname", hooked_gethostbyname );
	if( !original_gethostbyname ) return false;

	original_GetAddrInfoExW = HookAPI< GetAddrInfoExWFn >( L"ws2_32.dll", "GetAddrInfoExW", hooked_GetAddrInfoExW );
	if( !original_GetAddrInfoExW ) return false;

	original_connect = HookAPI< connectFn >( L"ws2_32.dll", "connect", hooked_connect );
	if( !original_connect ) return false;

	original_InternetConnectA = HookAPI< InternetConnectAFn >( L"wininet.dll", "InternetConnectA", hooked_InternetConnectA );
	if( !original_InternetConnectA ) return false;

	original_InternetConnectW = HookAPI< InternetConnectWFn >( L"wininet.dll", "InternetConnectW", hooked_InternetConnectW );
	if( !original_InternetConnectW ) return false;

	if( !HookJVM( ) ) return false;

	const auto enable = MH_EnableHook( MH_ALL_HOOKS );
	if( enable != MH_OK )
	{
		speak( "[hook] Failed to enable all hooks [%s]", MH_StatusToString( init ) );
		return false;
	}

	return true;
}
