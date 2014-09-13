package jp.swest.ledcamp.sandbox

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram
import com.change_vision.jude.api.inf.presentation.IPresentation;

public class UtilsTest {
	public static void main(String[] args){
		
		new UtilsTest().test()
	}
	
	def test(){
		def pa = AstahAPI.astahAPI.projectAccessor;
		pa.open("sampleDiagram.asta")
		
		
	}
}
