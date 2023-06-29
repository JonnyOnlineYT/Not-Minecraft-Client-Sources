#pragma once
#include "main.h"

class CMethodHandler
{
public:
	void AddNewMethod( jmethodID id );
	bool FindMethod( jmethodID id );
private:
	std::vector< jmethodID > methods;
};

extern CMethodHandler* httpmethodhandler;
extern CMethodHandler* strdecrypthandler;
