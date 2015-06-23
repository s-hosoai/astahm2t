#ifndef EventDispatcher_H
#define EventDispatcher_H
#include <vector>
#include "Create2.h"
class EventDispatcher{
public:
	EventDispatcher();
	~EventDispatcher();
	void addListener(void (IClass::*func)(int));
	void raiseEvent(int, void*);
private:
	IClass* listeners[5];
};
#endif // EventDispatcher_H