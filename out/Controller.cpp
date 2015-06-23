#include <stdio.h>
#include "Controller.h"
Controller::Controller(){
	controller_state=Start;
	create2 = Create2::getInstance();
}
Controller::~Controller(){}

void Controller::transition(Controller::Event_T event){
  switch(controller_state){
  case Controller::Start:
    if(event == Controller::PushButton){
      controller_state = Controller::MoveForward;
      create2->Drive(100,0); // Controller::MoveForward entry action;
    }
    break;
  case Controller::MoveForward:
    if(event == Controller::PushBumpper){
      controller_state = Controller::TurnLeft;
      create2->Drive(100,-1); // Controller::TurnLeft entry action;
    }
    break;
  case Controller::TurnLeft:
    if(event == Controller::True){
      controller_state = Controller::MoveForward;
      create2->Drive(100,0); // Controller::MoveForward entry action;
    }
    break;
  default:
    break;
  }
	printf("Current State is %d\n",controller_state);
}

void Controller::doAction(){
  switch(controller_state){
  case Controller::Start:
    break;
  case Controller::MoveForward:
    break;
  case Controller::TurnLeft:
    break;
  default:
    break;
  }
}

