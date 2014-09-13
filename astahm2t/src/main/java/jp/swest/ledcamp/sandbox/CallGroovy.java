package jp.swest.ledcamp.sandbox;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

public class CallGroovy {
	String[] paths = {"C:/Data/workspace/iArch/astahgroovy/"};
	String script = "Sample.groovy";

	public static void main(String[] args) {
		CallGroovy callGroovy = new CallGroovy();
//		callGroovy.runWithShell();
		callGroovy.runWithGSE();
	}
	
	public void runWithGSE() {
		try{
			GroovyScriptEngine gse = new GroovyScriptEngine(paths);
			Binding binding = new Binding();
			binding.setVariable("args", "World");
			gse.run(script,binding);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void runWithShell(){
		GroovyShell shell = new GroovyShell();
		Script script = shell.parse("Sample.groovy");
		Binding binding = new Binding();
		binding.setVariable("args", "World");
		script.setBinding(binding);
		script.run();
	}
}
