package jp.swest.ledcamp;

import java.io.IOException;

import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CodeGenerateTest {
	
	@Test
	public void modelLoad() throws Exception{
		AstahAPI api = AstahAPI.getAstahAPI();
		ProjectAccessor pa = api.getProjectAccessor();
		pa.open("ledSample.asta");
		CodeGenerator generator = new CodeGenerator();
		GeneratorType testGenType = new GeneratorType("arduino", "ModelCollector.groovy","FileGenerator.groovy");
		
		INamedElement[] classes = pa.findElements(IClass.class);
		for(INamedElement elem : classes){
			IClass clazz = (IClass) elem;
		}
		
		generator.generate("C:/Users/hosoai/Desktop/Create/led-camp2/Astah/astahm2t/gen", "Project", testGenType);
	}	
}
