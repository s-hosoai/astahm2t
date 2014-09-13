import groovy.text.GStringTemplateEngine;
import com.change_vision.jude.api.inf.model.IClass;
import jp.swest.ledcamp.SettingManager;

//SettingManager setting = settings
HashMap<String, IClass> cls = classes
//map = settings.getTemplates()

path = generatorType.getTemplatePath()

/*Generate Arduino File*/
arduinoTemplate = settings.getTemplate("arduino")
models = ["models":classes.values(), "u":new ArduinoUtils()]
engine = new GStringTemplateEngine()
template = engine.createTemplate(new File(path+arduinoTemplate.path)).make(models)
println("** Arduino.ino **")
println(template.toString())

File projectDir = new File(targetDir+"/"+project+"/")
if(!projectDir.exists()){
	projectDir.mkdirs()
}
FileWriter writer = new FileWriter(targetDir+"/"+project+"/"+project+".ino")
writer.write(template.toString());
writer.flush();
writer.close();

/* Generate C++ source and header Files*/
ClassUtils utils;
for(clazz in classes.values()){
	utils = new ClassUtils(clazz)
	models = ["model":clazz, "u":utils]

	templateSetting = settings.getTemplate("cpp")
//	engine = new GStringTemplateEngine()
	template = engine.createTemplate(new File(settings.getTemplatePath()+templateSetting.path)).make(models)
	println(template.toString())
	writer = new FileWriter(targetDir+"/"+project+"/"+utils.className+".cpp")
	writer.write(template.toString());
	writer.flush();
	writer.close();
	
	templateSetting = settings.getTemplate("header")
	template = engine.createTemplate(new File(settings.getTemplatePath()+templateSetting.path)).make(models)
	println(template.toString())
	writer = new FileWriter(targetDir+"/"+project+"/"+utils.className+".h")
	writer.write(template.toString());
	writer.flush();
	writer.close();
}
