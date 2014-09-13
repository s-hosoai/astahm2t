package jp.swest.ledcamp;

import java.io.IOException;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CodeGenerator {
	public void generate(String targetDir,String projectName, GeneratorType generatorType) throws ClassNotFoundException, ProjectNotFoundException, IOException, ResourceException, ScriptException{
		AstahAPI api = AstahAPI.getAstahAPI();
        ProjectAccessor projectAccessor = api.getProjectAccessor();
		projectAccessor.getProject(); // exist project?
		SettingManager setting = SettingManager.getInstance();
		GroovyScriptEngine gse;
		Binding binding = new Binding();
		binding.setVariable("projectaccessor", projectAccessor);

		/* Collect models from project */
		gse = new GroovyScriptEngine(setting.getScriptPath());
		gse.run(generatorType.getModelCollectorPath(),binding);
		
		/* Generate Files */
		binding.setVariable("targetDir", targetDir);
		binding.setVariable("project", projectName);
		binding.setVariable("settings", setting);
		binding.setVariable("generatorType", generatorType);
		gse.run(generatorType.getFileGeneratorPath(), binding);		
	}
}
