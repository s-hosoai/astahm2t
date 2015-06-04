import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IFinalState;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;

class ArduinoUtils{
	def getName(IClass clazz){
		return clazz.getName()
	}
	def getClassName(IClass clazz){
		def name = clazz.name
		return ""+name.charAt(0).toUpperCase()+name.substring(1)
	}
	def getInstanceName(IClass clazz){
		def name = clazz.name
		return ""+name.charAt(0).toLowerCase()+name.substring(1)
	}
}