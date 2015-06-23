#include "EventDispatcher.h"
EventDispatcher::EventDispatcher(){
	listeners[0] = Create2::getInstance();
	listeners[1] = Create2::getInstance();
	listeners[2] = Create2::getInstance();
	listeners[3] = Create2::getInstance();
	listeners[4] = Create2::getInstance();
}

EventDispatcher::~EventDispatcher(){}

void EventDispatcher::raiseEvent(int event, void* from){
	for(int i=0;i<5;i++){
		listeners[0]->transition(event);
	}
}

int main(int argc, char** args){
	EventDispatcher dispatcher;
	dispatcher.raiseEvent(1, 0);
}
