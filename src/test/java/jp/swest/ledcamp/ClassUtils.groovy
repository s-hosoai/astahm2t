package jp.swest.ledcamp
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IFinalState;
import com.change_vision.jude.api.inf.model.IPseudostate;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;

class ClassUtils{
	IClass clazz;
	IStateMachine stateMachine
	String name
	def ClassUtils(IClass clazz){
		this.clazz = clazz
		this.stateMachine = (clazz.diagrams.find  {diag -> diag instanceof IStateMachineDiagram} as IStateMachineDiagram)?.stateMachine
//		this.stateMachine = stateMachine
		this.name = clazz.name;
	}
	def getInitialState(){
		IPseudostate initPseudo = stateMachine?.vertexes?.find {v->v instanceof IPseudostate && (v as IPseudostate).isInitialPseudostate()} as IPseudostate
		return initPseudo?.outgoings[0].target
	}

	def getName(IClass clazz){
		return clazz.getName()
	}
	def getClassName(){
		return ""+name.charAt(0).toUpperCase()+name.substring(1)
	}
	def getInstanceName(){
		return ""+name.charAt(0).toLowerCase()+name.substring(1)
	}
	def getStateInstance(){
		return instanceName+"_state"
	}
	def getStates(){
		return stateMachine?.vertexes?.findAll {v->v instanceof IState}
	}	
	def getSubStates(IState state){
		return state.subvertexes;
	}
	
	def getStateName(IState state){
		if(state instanceof IFinalState){
			return "S_"+name+"__Final"
		}else{
			return "S_"+getClassName()+"_"+state.name
		}
	}

	def getEventInstance(){
		return instanceName+"_event"
	}
	def getEvents(){
		def events = new HashSet<String>()
		for(t in stateMachine?.transitions){
			if(t.event.trim()!="" && t.event.trim()!="true" && t.event.trim()!="false"){
				events.add(t.event)
			}
		}
		return events
	}
	def getEventName(String event){
		if(event=="true"){
			return "true"
		}
		return "E_"+getClassName()+"_"+event
	}
}