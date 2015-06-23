#ifndef Controller_H
#define Controller_H
//#include <arduino.h>
//#include <Create.h>
#include "Create2.h"
/* class declaration */
class Controller
{
public:
/* events and states enums */
	enum State_T{
		Start, MoveForward, TurnLeft, 
	};
	enum Event_T{
		PushButton, PushBumpper, True,  None
	};
/* member var */

/* functions */
	static Controller* getInstance(){
		static Controller instance;
		return &instance;
	}
	void transition(Controller::Event_T event);
	void doAction();
private:
	Controller();
	~Controller();
	State_T controller_state;
	Create2* create2;
};
#endif //Controller_H
