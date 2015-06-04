import groovy.text.GStringTemplateEngine;
import com.change_vision.jude.api.inf.model.IClass;
import jp.swest.ledcamp.SettingManager;

//SettingManager setting = settings
HashMap<String, IClass> cls = classes
//map = settings.getTemplates()

path = generatorType.getTemplatePath()

/*Generate Arduino File*/
arduinoTemplate = settings.getTemplate("java-controller")
models = ["models":classes.values(), "u":new JavaUtils()]
engine = new GStringTemplateEngine()
template = engine.createTemplate(new File(path+arduinoTemplate.path)).make(models)
println("** Arduino.ino **")
println(template.toString())

File projectDir = new File(targetDir+"/"+project+"/")
if(!projectDir.exists()){
	projectDir.mkdirs()
}
FileWriter writer = new FileWriter(targetDir+"/"+project+"/Controller.java")
writer.write(template.toString());
writer.flush();
writer.close();

/* Generate C++ source and header Files*/
JavaUtils utils;
for(clazz in classes.values()){
	templateSetting = settings.getTemplate("java")
	utils = new ClassUtils(clazz)
	models = ["model":clazz, "u":utils]
//	engine = new GStringTemplateEngine()
	template = engine.createTemplate(new File(generatorType.getTemplatePath()+templateSetting.path)).make(models)
	println(template.toString())
	writer = new FileWriter(targetDir+"/Project/"+utils.className+".java")
	writer.write(template.toString());
	writer.flush();
	writer.close();	
}
