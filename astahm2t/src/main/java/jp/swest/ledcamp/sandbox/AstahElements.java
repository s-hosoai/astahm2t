package jp.swest.ledcamp.sandbox;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPseudostate;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IUsage;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class AstahElements {
	public static void main(String[] args) throws Exception{
		AstahElements test = new AstahElements();
		test.viewElements();
	}
	
	
	public void viewElements() throws Exception{
		AstahAPI api = AstahAPI.getAstahAPI();
		ProjectAccessor pa = api.getProjectAccessor();
		pa.open("ledSample.asta");
		IClass clazz = (IClass)pa.findElements(IClass.class)[0];
		System.out.println(clazz.getName());
		IOperation op = clazz.getOperations()[0];
		IUsage[] usage = op.getSupplierUsages();
		op.getParameters()[0].getTypeExpression();
		op.getDefinition();
		IStateMachine sm = (IStateMachine)pa.findElements(IStateMachine.class)[0];
		for(IVertex elem: sm.getVertexes()){
			if(elem instanceof IState){
				IState state = (IState)elem;
			System.out.println("statename:"+state);
			
//			System.out.println("sub:"+state.getSubvertexes());
			for(IVertex ver:state.getSubvertexes()){
				System.out.println("sub:"+ver);
			}
//			System.out.println("sub:"+state.getContainers());
			state.getDoActivity();
			}else{
				System.out.println(elem);
			}

		}
	}
}
