enum EventA{
	B, C, D
};
enum EventB{
	E, F, G
};

class A {
public:
	void call(EventA a);
	void call(EventB b);
};
