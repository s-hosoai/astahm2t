package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.model.IClass
import com.change_vision.jude.api.inf.model.IStateMachine
import com.change_vision.jude.api.inf.AstahAPI
import com.change_vision.jude.api.inf.model.IPackage
import com.change_vision.jude.api.inf.model.IModel
import java.util.List
import java.util.ArrayList
import org.eclipse.xtend.lib.annotations.Accessors
import com.change_vision.jude.api.inf.project.ProjectAccessor
import jp.swest.ledcamp.setting.SettingManager
import java.util.Hashtable
import com.change_vision.jude.api.inf.model.IStateMachineDiagram
import java.util.HashMap
import com.change_vision.jude.api.inf.model.IState
import com.change_vision.jude.api.inf.model.IPseudostate
import java.awt.event.FocusAdapter
import java.awt.event.FocusListener
import java.awt.event.FocusEvent
import javax.swing.JOptionPane

class GeneratorUtils {
    @Accessors private AstahAPI api
    @Accessors private ProjectAccessor projectAccessor
    @Accessors private IModel projectRoot
    @Accessors private IClass iclass
    @Accessors private IStateMachine statemachine
    @Accessors List<IClass> classes
    @Accessors HashMap<IClass, IStateMachine> statemachines
    
    new(){
        api = AstahAPI.getAstahAPI()
        projectAccessor = api.getProjectAccessor()
//        projectAccessor.open("Create2.asta");
        projectRoot = projectAccessor.getProject() // exist project?
        api.viewManager.mainFrame.addFocusListener(new FocusAdapter(){            
            override focusGained(FocusEvent e) {
                JOptionPane.showConfirmDialog(api.viewManager.mainFrame,"hello");
            }
            });

        // Collect classes and statemachines.
        classes = new ArrayList<IClass>()
        statemachines = new HashMap<IClass, IStateMachine>()
        for (iClass : projectRoot.getOwnedElements().filter(IClass)) {
            classes.add(iClass)
            for (diagram : iClass.getDiagrams().filter(IStateMachineDiagram)) {
                statemachines.put(iClass, diagram.getStateMachine())
            }
        }
    }
    
    // for class diagram utility
    def getName(){
        iclass.name
    }
    
    def getInstanceName(){
        iclass.name.toFirstLower
    }
    
    // for statemachine utility
    def getStates(){
        return statemachine?.vertexes.filter[s|!(s instanceof IPseudostate)]
    }
    def getEvents(){
        statemachine?.transitions.map[t|t.event].filter[e|e.trim.length>1]
    }
    
    def getInitialState(){
        var initialPseudo = statemachine?.vertexes?.filter(IPseudostate).filter[s|s.isInitialPseudostate].get(0)
        return initialPseudo?.outgoings.get(0).target.name
    }
    
    // for global generate
    def getClasses(){
        return this.classes
    }
    
    def getFrame(){
        return projectAccessor.viewManager.mainFrame
    }
    
    def stereotypeFilter(List<IClass> classes, String stereotype){
        return classes.filter[c|c.stereotypes.contains(stereotype)]
    }
    def stereotypeNotFilter(List<IClass> classes, String stereotype){
        return classes.filter[c|!c.stereotypes.contains(stereotype)]
    }
    
    private def recursiveClassCollect(IModel model, List<IClass> classes){
        classes.addAll(model.ownedElements.filter(IClass))
        model.ownedElements.filter(IPackage).forEach[p|recursiveClassCollect(p, classes)]        
    }
    private def recursiveClassCollect(IPackage model, List<IClass> classes){
        classes.addAll(model.ownedElements.filter(IClass))
        model.ownedElements.filter(IPackage).forEach[p|recursiveClassCollect(p, classes)]
    }
    
    def static void main(String[] args) {
        var utils = new GeneratorUtils
//        utils.test
        for(c : utils.classes){
            utils.iclass = c
            utils.statemachine = utils.statemachines.get(c)
            println(c.name)
            println(utils.initialState)
            for(s : utils.events){
                println("  "+s)
            }
        }
    }
    
    def test(){
        this.classes = new ArrayList<IClass>()
        var api = AstahAPI.getAstahAPI();
        var pa = api.getProjectAccessor();
        pa.open("Create2.asta");
        recursiveClassCollect(pa.project, classes)
        classes.stereotypeNotFilter("library").forEach[c|println(c)]
    }
}