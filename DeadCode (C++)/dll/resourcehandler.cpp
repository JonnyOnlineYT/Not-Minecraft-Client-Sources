#include "resourcehandler.h"

namespace resources
{
	std::unordered_map< std::string, Resource* > map;

	void Setup( )
	{
		Resource* base = new Resource( );
		base->addr = util::GetResource( IDR_TXT1, "gc", base->size );
		map.insert( { "gc", base } );
	}

	Resource* Get( std::string name )
	{
		return map.at( name );
	}
}
