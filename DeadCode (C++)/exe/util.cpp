#define _CRT_SECURE_NO_WARNINGS
#include <boost/asio.hpp>

#include "util.h"

namespace util
{
	// https://stackoverflow.com/a/13259720
	std::string CalculateMD5Hash( const void* data, const size_t size )
	{
		HCRYPTPROV prov = 0;
		std::string ret = "";

		if( !CryptAcquireContext( &prov, 0, 0, PROV_RSA_AES, CRYPT_VERIFYCONTEXT ) )
			return ret;

		HCRYPTPROV hash = 0;

		if( !CryptCreateHash( prov, CALG_MD5, 0, 0, &hash ) )
		{
			CryptReleaseContext( prov, 0 );
			return ret;
		}

		if( !CryptHashData( hash, ( const BYTE* )( data ), size, 0 ) )
		{
			CryptDestroyHash( hash );
			CryptReleaseContext( prov, 0 );
			return ret;
		}

		DWORD hashsize = 0;
		DWORD hashcount = sizeof( DWORD );

		if( !CryptGetHashParam( hash, HP_HASHSIZE, ( BYTE* )( &hashsize ), &hashcount, 0 ) )
		{
			CryptDestroyHash( hash );
			CryptReleaseContext( prov, 0 );
			return ret;
		}

		std::vector< BYTE > buffer( hashsize );
		if( !CryptGetHashParam( hash, HP_HASHVAL, ( BYTE* )( &buffer[ 0 ] ), &hashsize, 0 ) )
		{
			CryptDestroyHash( hash );
			CryptReleaseContext( prov, 0 );
			return ret;
		}

		std::ostringstream oss;
		for( auto it = buffer.begin( ); it != buffer.end( ); it++ )
		{
			oss.fill( '0' );
			oss.width( 2 );
			oss << std::hex << ( const int )( *it );
		}

		ret = oss.str( );

		CryptDestroyHash( hash );
		CryptReleaseContext( prov, 0 );
		return ret;
	}

	void PrintOk( )
	{
		SetConsoleTextAttribute( GetStdHandle( STD_OUTPUT_HANDLE ), 2 );
		printf( " ok\n" );
		SetConsoleTextAttribute( GetStdHandle( STD_OUTPUT_HANDLE ), 7 );
	}

	void PrintError( )
	{
		SetConsoleTextAttribute( GetStdHandle( STD_OUTPUT_HANDLE ), 4 );
		printf( " error\n" );
		SetConsoleTextAttribute( GetStdHandle( STD_OUTPUT_HANDLE ), 7 );
	}

	// проверяет есть ли папка и создает ее если нету
	// возвращает true если есть и false если нет
	bool CheckCreateFolder( std::string path )
	{
		static bool once = false;

		if( !std::filesystem::exists( path ) )
		{
			if( !once )
			{
				PrintError( );
				once = true;
			}

			wprintf( L"Папка %hs не найдена, создаем...", path.c_str( ) );
			if( !std::filesystem::create_directory( path ) )
			{
				PrintError( );
				wprintf( L"Не удалось создать папку %hs, запустите кряк от имени администратора\n", path.c_str( ) );
				system( "pause" );
				exit( 1 );
			}
			else
				PrintOk( );

			return false;
		}

		return true;
	}

	// проверяет есть ли файл по указанному пути и проверяет его хеш
	// если файла нет или хеш не совпадает то перезаписывает файл
	bool CheckWriteResource( int id, std::string path )
	{
		static HMODULE handle = GetModuleHandleA( 0 ); // хендл текущего процесса, GetCurrentProcess() сосет хуй

		const auto resource = FindResourceW( handle, MAKEINTRESOURCEW( id ), L"TXT" );
		if( !resource )
		{
			wprintf( L"\nНе удалось найти ресурс [%i / %hs] в памяти процесса\n", id, path.c_str( ) );
			system( "pause" );
			exit( 1 );
		}

		const auto resourcehandle = LoadResource( handle, resource );
		if( !resourcehandle )
		{
			wprintf( L"\nНе удалось загрузить ресурс [%i / %hs]\n", id, path.c_str( ) );
			system( "pause" );
			exit( 1 );
		}

		const auto size = SizeofResource( handle, resource );
		const auto data = ( char* )LockResource( resourcehandle );

		const auto hash = CalculateMD5Hash( data, size );
		bool fail = false;
		static bool once = false;

		if( !std::filesystem::exists( path ) )
		{
			if( !once )
			{
				once = true;
				PrintError( );
			}

			wprintf( L"Файл %hs не найден, записываем...", path.c_str( ) );

			fail = true;
		}
		else
		{
			const auto file = OpenFile( size, path.c_str( ) );
			const auto hash2 = CalculateMD5Hash( file, size );

			if( hash != hash2 )
			{
				if( !once )
				{
					once = true;
					PrintError( );
				}

				fail = true;
				wprintf( L"Неверный хеш у файла %hs, перезаписываем...", path.c_str( ) );
			}
		}

		if( fail )
		{
			WriteFile( data, size, path.c_str( ) );
			util::PrintOk( );
		}

		return !fail;
	}

	void WriteFile( char* bytes, int size, const char* path )
	{
		HANDLE filehandle = CreateFileA( path, GENERIC_WRITE, FILE_SHARE_WRITE, 0, OPEN_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0 );
		if( filehandle == INVALID_HANDLE_VALUE )
		{
			wprintf( L"\nНе удалось открыть файл %hs для чтения\n", path );
			system( "pause" );
			exit( 1 );
		}

		DWORD in = 0;
		if( !::WriteFile( filehandle, bytes, size, &in, 0 ) )
		{
			wprintf( L"\nНе удалось записать файл %hs\n", path );
			system( "pause" );
			exit( 1 );
		}

		CloseHandle( filehandle );
	}

	char* OpenFile( int size, const char* path )
	{
		HANDLE filehandle = CreateFileA( path, GENERIC_READ, FILE_SHARE_READ, 0, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0 );
		if( filehandle == INVALID_HANDLE_VALUE )
		{
			wprintf( L"\nНе удалось открыть файл %hs для чтения\n", path );
			system( "pause" );
			exit( 1 );
		}

		char* buf = new char[ size ];
		DWORD in = 0;
		if( !ReadFile( filehandle, buf, size, &in, 0 ) )
		{
			wprintf( L"\nНе удалось прочитать файл %hs\n", path );
			CloseHandle( filehandle );
			system( "pause" );
			exit( 1 );
		}

		CloseHandle( filehandle );

		return buf;
	}

	// смотрит закрыт ли порт (для сервер эмулятора)
	bool IsPortInUse( unsigned short port )
	{
		boost::asio::io_service service;
		boost::asio::ip::tcp::acceptor acceptor( service );

		boost::system::error_code code;
		acceptor.open( boost::asio::ip::tcp::v4( ), code );
		acceptor.bind( { boost::asio::ip::tcp::v4( ), port }, code );

		bool closed = code == boost::asio::error::address_in_use;

		if( acceptor.is_open( ) )
			acceptor.close( );

		return closed;
	}

	DWORD GetProcessID( const wchar_t* windowname, const wchar_t* processname )
	{
		const auto window = FindWindowW( 0, windowname );
		if( window )
		{
			DWORD ret = 0;
			if( GetWindowThreadProcessId( window, &ret ) )
				return ret;
		}

		PROCESSENTRY32 entry;
		entry.dwSize = sizeof( PROCESSENTRY32 );

		auto snap = CreateToolhelp32Snapshot( TH32CS_SNAPPROCESS, 0 );
		if( Process32First( snap, &entry ) )
		{
			do
			{
				if( !lstrcmpiW( entry.szExeFile, processname ) )
				{
					CloseHandle( snap );
					return entry.th32ProcessID;
				}
			}
			while( Process32Next( snap, &entry ) );
		}

		// мы нихуя не нашли!
		CloseHandle( snap );

		return 0;
	}

	// инжектит дллку в процесс через лоадлибрари (нихуя не скрытно)
	bool Inject( DWORD processid, const char* path )
	{
		auto open = OpenProcess( PROCESS_ALL_ACCESS, false, processid );
		if( !open )
		{
			PrintError( );
			wprintf( L"Не удалось открыть процесс %i для инжекта DLL. Попробуйте запустить кряк от администратора\n", processid );
			return false;
		}

		auto LoadLibraryAddress = GetProcAddress( GetModuleHandleA( "kernel32.dll" ), "LoadLibraryA" );
		if( !LoadLibraryAddress )
		{
			PrintError( );
			wprintf( L"Не удалось найти функцию LoadLibraryA в kernel32.dll\n" );
			return false;
		}

		// я хуй знает надо ли это или можно использовать path
		// но мне впадлу с этим разбираться поэтому сделаю
		char charpath[ MAX_PATH ];
		snprintf( charpath, MAX_PATH, path );

		auto alloc = VirtualAllocEx( open, 0, strlen( charpath ), MEM_RESERVE | MEM_COMMIT, PAGE_READWRITE );
		if( !WriteProcessMemory( open, alloc, charpath, strlen( charpath ), 0 ) )
		{
			PrintError( );
			wprintf( L"Не удалось записать путь DLL для инжекта в процесс %i. Попробуйте запустить кряк от администратора\n", processid );
			return false;
		}

		// вызываем LoadLibraryA в другом процессе
		const auto thread = CreateRemoteThread( open, 0, 0, ( LPTHREAD_START_ROUTINE )LoadLibraryAddress, alloc, 0, 0 );
		if( !thread )
		{
			PrintError( );
			wprintf( L"Не удалось заинжектить DLL в процесс %i\n", processid );
			return false;
		}

		WaitForSingleObject( thread, INFINITE );

		DWORD code = 0;
		GetExitCodeThread( thread, &code );

		CloseHandle( thread );
		CloseHandle( open );

		return code != 0;
	}

	long GetFileSize( const char* path )
	{
		auto file = fopen( path, "r" );
		if( !file ) return 0;

		fseek( file, 0, SEEK_END );
		auto ret = ftell( file );
		fclose( file );

		return ret;
	}
}
