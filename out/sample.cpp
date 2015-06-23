#include <stdio.h>
#include "Create2.h"
#include "Controller.h"

int main(int argc, char** args){
	Controller* controller = Controller::getInstance();
	controller->doAction();
	controller->transition(Controller::PushButton);
	controller->transition(Controller::PushBumpper);
	controller->transition(Controller::True);
}
