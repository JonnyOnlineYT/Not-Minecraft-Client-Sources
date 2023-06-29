#pragma once
#include "resource.h"
#include "main.h"
#include "util.h"
#include <unordered_map>

namespace resources
{
	struct Resource
	{
		char* addr;
		DWORD size;
	};

	extern void Setup( );
	extern Resource* Get( std::string name );

	extern std::unordered_map< std::string, Resource* > map;
}
