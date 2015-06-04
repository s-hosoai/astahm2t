package jp.swest.ledcamp.generator;

import java.awt.HeadlessException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JOptionPane;

import jp.swest.ledcamp.setting.SettingManager;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CodeGenerator {
	public static void generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {
		AstahAPI api = AstahAPI.getAstahAPI();
        ProjectAccessor projectAccessor = api.getProjectAccessor();
		IModel projectRoot = projectAccessor.getProject(); // exist project?
		SettingManager setting = SettingManager.getInstance();
		
		// Collect classes and statemachines.
		ArrayList<IClass> classes = new ArrayList<IClass>();
		Hashtable<IClass, IStateMachine> statemachines = new Hashtable<IClass, IStateMachine>();
		for(IElement element : projectRoot.getOwnedElements()){
			if(element instanceof IClass){
				IClass iClass = (IClass)element;
				classes.add(iClass);
				for(IDiagram diagram : iClass.getDiagrams()){
					if(diagram instanceof IStateMachineDiagram){
						statemachines.put(iClass, ((IStateMachineDiagram)diagram).getStateMachine());
					}
				}
			}
		}
		
		// code generate
		for(IClass iClass : classes){
			//iClass.getStereotypes();	// 後々 ステレオタイプごとに生成ルールを変える
			Properties p = new Properties();  
			p.setProperty("input.encoding", "UTF-8");
			p.setProperty("file.resource.loader.path","C:/Users/hosoai/git/astahm2t-github/astahm2t/template/");
			Velocity.init(p); 

			VelocityContext ctx = new VelocityContext();
			ctx.put("c", iClass);
			ctx.put("s", statemachines.get(iClass));

			Template template = Velocity.getTemplate("sample.vm");
			FileWriter writer = new FileWriter("C:/Users/hosoai/git/astahm2t-github/astahm2t/out/sample.cpp");
			template.merge(ctx, writer);
			writer.flush();
			writer.close();
		}
		JOptionPane.showMessageDialog(projectAccessor.getViewManager().getMainFrame(), "Generate Finish");
/*		
		GroovyScriptEngine gse;
		Binding binding = new Binding();
		binding.setVariable("projectaccessor", projectAccessor);

		// Collect models from project
		gse = new GroovyScriptEngine(setting.getScriptPath());
		gse.run(generatorType.getModelCollectorPath(),binding);
		
		// Generate Files
		binding.setVariable("targetDir", targetDir);
		binding.setVariable("project", projectName);
		binding.setVariable("settings", setting);
		binding.setVariable("generatorType", generatorType);
		gse.run(generatorType.getFileGeneratorPath(), binding);		
*/
	}	
}
