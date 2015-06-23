#include <stdio.h>
#include "EventTestA.h"

void A::call(EventA a){
	printf("call A\n");
}

void A::call(EventB b){
	printf("call B\n");
}

int main(int argc, char** argv){
	A a;
	a.call(B);
	a.call(E);
}