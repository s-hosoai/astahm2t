package jp.swest.ledcamp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.IStateMachine
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import groovy.text.GStringTemplateEngine
import groovy.text.SimpleTemplateEngine

class TemplateTest {

	@Test
	public void HeaderTemplateTest() {
		def classes = new HashMap<String, IClass>()
		def stateMachines = new HashMap<IClass, IStateMachine>()
		def accessor = AstahAPI.astahAPI.projectAccessor
		SimpleTemplateEngine engine = new SimpleTemplateEngine()
		engine.verbose = true;
		
		
		accessor.open("led.asta")
		for(classDiag in accessor.findElements(IClassDiagram)){
			for(clazzPres in classDiag.presentations.findAll {p->p.model instanceof IClass}){
				IClass clazz = clazzPres.model
				ClassUtils utils = new ClassUtils(clazz)
				def models = ["model":clazz, "u":utils]
				print "aaa"+utils.initialState;
//				templateSetting = settings.getTemplate("header")
				def template = engine.createTemplate(new File("groovy/templates/header.template")).make(models)
				println(template.toString())
				IStateMachineDiagram stateMachine = clazz.diagrams.find {diag->diag instanceof IStateMachineDiagram}
				if(stateMachine!=null){
					stateMachines.put(clazz, stateMachine)
				}
				
				
				
			}
		}
	}
}
