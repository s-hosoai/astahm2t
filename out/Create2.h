#ifndef Create2_H
#define Create2_H
//#include <arduino.h>
//#include <Create.h>
/* class declaration */
class Create2
{
public:
/* events and states enums */
/* member var */

/* functions */
	static Create2* getInstance(){
		static Create2 instance;
		return &instance;
	}
	void Drive(int, int);
	void Start();
	void Stop();
private:
	Create2();
	~Create2();
};
#endif //Create2_H
