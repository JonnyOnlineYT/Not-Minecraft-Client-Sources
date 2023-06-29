#include "methodhandler.h"

CMethodHandler* httpmethodhandler = new CMethodHandler( );
CMethodHandler* strdecrypthandler = new CMethodHandler( );

void CMethodHandler::AddNewMethod( jmethodID id )
{
	if( !FindMethod( id ) )
		methods.push_back( id );
}

bool CMethodHandler::FindMethod( jmethodID id )
{
	for( auto method : methods )
	{
		if( method == id )
			return true;
	}

	return false;
}
