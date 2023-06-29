#include "main.h"
#include "resource.h"
#include "fonts.h"
#include "photos.h"
#include "resourcehandler.h"

void speak( const char* text, ... )
{
	char converted[ 1024 ];
	va_list list;
	va_start( list, text );
	vsnprintf( converted, sizeof( converted ), text, list );
	va_end( list );

	printf( "[jewnative] %s\n", converted );
	fflush( stdout ); // чтоб принтило в консоль джавы
}

namespace util
{
	std::string GetClassName( JNIEnv* env, jclass clazz )
	{
		const auto Class = env->FindClass( "java/lang/Class" );

		skipmethod = true; // дабы наш хук GetMethodID ничего не сломал

		const auto Class_getName_ID = env->GetMethodID( Class, "getName", "()Ljava/lang/String;" );
		const auto classname = ( jstring )env->CallObjectMethod( clazz, Class_getName_ID );
		if( !classname ) return "";

		auto chars = env->GetStringUTFChars( classname, 0 );
		std::string ret = chars;
		env->ReleaseStringUTFChars( classname, chars );

		return ret;
	}

	void WriteError( )
	{
		const auto handle = CreateFileA( "C:/DeadCode/Libs/State/n1.bool", GENERIC_WRITE, 0, 0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0 );
		if( handle )
			CloseHandle( handle );
		else
		{
			// ИДИ НАХУЙ
			exit( 1 );
		}
	}

	void WriteOk( )
	{
		const auto handle = CreateFileA( "C:/DeadCode/Libs/State/1.bool", GENERIC_WRITE, 0, 0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0 );
		if( handle )
			CloseHandle( handle );
		else
		{
			// ИДИ НАХУЙ
			exit( 1 );
		}
	}

	char* GetResource( int id, const char* name, DWORD& sizeout )
	{
		const auto resource = FindResourceW( mod, MAKEINTRESOURCEW( id ), L"TXT" );
		if( !resource )
		{
			wchar_t wide[ 1024 ];
			wsprintf( wide, L"Не удалось найти ресурс [%i / %hs] в памяти процесса", id, name );
			MessageBoxW( 0, wide, L"jewnative - error", MB_OK );
			exit( 1 );
		}

		const auto resourcehandle = LoadResource( mod, resource );
		if( !resourcehandle )
		{
			wchar_t wide[ 1024 ];
			wsprintf( wide, L"Не удалось загрузить ресурс [%i / %hs]", id, name );
			MessageBoxW( 0, wide, L"jewnative - error", MB_OK );
			exit( 1 );
		}

		sizeout = SizeofResource( mod, resource );
		return ( char* )LockResource( resourcehandle );
	}

	// для использования в MakeResponse
	std::string Convert( unsigned char* what, size_t size )
	{
		unsigned char* ret = ( unsigned char* )malloc( size );
		if( !ret )
		{
			speak( "[Convert] Failed to allocate memory (%i bytes)", size );
			exit( 1 );
			return "";
		}

		memcpy( ret, what, size );
		return std::string( ( const char* )ret, size );
	}

	std::string MakeResponse( std::string target, std::string& type )
	{
		type = "text/plain";

		auto lowercase = target;
		std::transform( lowercase.begin( ), lowercase.end( ), lowercase.begin( ),
						[ ]( unsigned char c ) { return std::tolower( c ); } );

		if( lowercase == "/" )
			return "127.0.0.1";
		else if( lowercase == "/test" )
			return "kill yourself";
		else if( lowercase.rfind( "/gs", 0 ) == 0 )
			return "{2_itCH9zgamNN4OJmo2dd8luOTtXJxqdpEmlHfSmYyXY7ex2ySjP$aE_uiV3vDULtf44WQ_VH1tPXr5gA2pJB57DblZWiohqFv2gIsYithJcgwI9e7sywq384H$lZRg5yUCS3vrJGtECRRsgl73EY0XoesVdaXLjjGYxckgnEoJDmpVPjsIR$EZewspMqQu4;mn7DUGYAIczAbqrBlT5Jugj7Fl2BdjCyC24T8x1tbJk=}";
		else if( lowercase.rfind( "/gld", 0 ) == 0 )
			return "G67OJ5JbD4sjF7Ie1R390nwKHAX6KwC4hlz9tDE$ie_TeSoM5XpQYt9blLHm$7sdrzKexWJ_ZYSakEp39Oxg08eWsqdt7WXT7n3leIyIv_k=;dLORGBdp_WNtlZ887aNBR6QG_5_h$q_llQd7Ly_sxzQ=";
		else if( lowercase.rfind( "/gc", 0 ) == 0 )
		{
			const auto res = resources::Get( "gc" );
			
			const char* ret = res->addr;
			return std::string( ret );
		}

		// шрифты
		if( lowercase.find( "aeroport" ) != std::string::npos )
		{
			type = "font/ttf";
			return Convert( Aeroport, sizeof( Aeroport ) );
		}
		else if( lowercase.find( "petch-bold" ) != std::string::npos )
		{
			type = "font/ttf";
			return Convert( ChakraPetchBold, sizeof( ChakraPetchBold ) );
		}
		else if( lowercase.find( "petch-medium" ) != std::string::npos )
		{
			type = "font/ttf";
			return Convert( ChakraPetchMedium, sizeof( ChakraPetchMedium ) );
		}
		else if( lowercase.find( "petch-regular" ) != std::string::npos )
		{
			type = "font/ttf";
			return Convert( ChakraPetchRegular, sizeof( ChakraPetchRegular ) );
		}
		else if( lowercase.find( "montserrat" ) != std::string::npos )
		{
			type = "font/ttf";
			return Convert( MontserratBlack, sizeof( MontserratBlack ) );
		}

		// фотки (пиздец)
		if( lowercase.find( "default_0." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_0, sizeof( Default_0 ) );
		}
		else if( lowercase.find( "default_1." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_1, sizeof( Default_1 ) );
		}
		else if( lowercase.find( "default_2." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_2, sizeof( Default_2 ) );
		}
		else if( lowercase.find( "default_3." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_3, sizeof( Default_3 ) );
		}
		else if( lowercase.find( "default_4." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_4, sizeof( Default_4 ) );
		}
		else if( lowercase.find( "default_5." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_5, sizeof( Default_5 ) );
		}
		else if( lowercase.find( "default_6." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_6, sizeof( Default_6 ) );
		}
		else if( lowercase.find( "default_7." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_7, sizeof( Default_7 ) );
		}
		else if( lowercase.find( "default_8." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_8, sizeof( Default_8 ) );
		}
		else if( lowercase.find( "default_9." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default_9, sizeof( Default_9 ) );
		}
		if( lowercase.find( "default2_0." ) != std::string::npos )
		{
			type = "image/png";
			return Convert( Default2_0, sizeof( Default2_0 ) );
		}

		speak( "[MakeResponse] Returning 404 for [%s]", target.c_str( ) );
		return "";
	}

	std::string NetworkRequest( const std::string& host, const std::string& path, int port, DWORD flags )
	{
		const auto open = InternetOpenW( L"NiggaImCurrentlyBallin", INTERNET_OPEN_TYPE_DIRECT, 0, 0, 0 );
		if( !open )
			return "";

		const auto connect = InternetConnectA( open, host.c_str( ), port, 0, 0, INTERNET_SERVICE_HTTP, 0, 0 );
		if( !connect )
		{
			InternetCloseHandle( open );
			return "";
		}

		const auto openrequest = HttpOpenRequestA( connect, "GET", path.c_str( ), "HTTP/1.1", 0, 0, flags, 0 );
		if( !openrequest )
		{
			InternetCloseHandle( open );
			InternetCloseHandle( connect );
			return "";
		}

		std::string ret = "";

		if( HttpSendRequest( openrequest, 0, 0, 0, 0 ) )
		{
			char buf[ 1024 ];
			DWORD read = 0;

			while( InternetReadFile( openrequest, buf, sizeof( buf ) - 1, &read ) && read )
				ret.append( buf, read );
		}

		InternetCloseHandle( openrequest );
		InternetCloseHandle( connect );
		InternetCloseHandle( open );

		return ret;
	}
}
