import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

ProjectAccessor accessor = projectaccessor;

/* models */
classes = new HashMap<String, IClass>()
stateMachines = new HashMap<IClass, IStateMachine>()

/* get class and relation statemachines */
classDiagrams = accessor.findElements(IClassDiagram)
for(classDiag in classDiagrams){
	for(clazzPres in classDiag.presentations.findAll {p->p.model instanceof IClass}){
		IClass clazz = clazzPres.model
		if(clazz.getStereotypes().find{stereo -> stereo.equals("lib")}){
			continue // <<lib>>ステレオタイプ持ちはスキップ
		}
		classes.put(clazz.name, clazz)
		IStateMachineDiagram stateMachine = clazz.diagrams.find {diag->diag instanceof IStateMachineDiagram}
		if(stateMachine!=null){
			stateMachines.put(clazz, stateMachine)
		}
	}
}

/* put in binding */
binding.putAt("classes", classes)
binding.putAt("statemachines", stateMachines)

