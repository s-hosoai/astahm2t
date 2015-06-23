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
import com.change_vision.jude.api.inf.model.IFinalState
import com.change_vision.jude.api.inf.model.IVertex
import java.util.Arrays
import com.change_vision.jude.api.inf.model.ITransition

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
        projectAccessor.open("Create2.asta");
        projectRoot = projectAccessor.getProject() // exist project?

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
    
    def getAllReferenceClasses(){
        iclass.attributes.map[e|e.type].filter[e|classes.contains(e)]
    }
        
    // for statemachine utility
    private ArrayList<IState> allStates
    def getStates(){
        if(statemachine==null){return null}
        allStates = new ArrayList
        getStates(statemachine)
        return allStates
    }
    
    private def getStates(IStateMachine m){
        val substates = m.vertexes.filter[s|!(s instanceof IPseudostate || s instanceof IFinalState)] as IState[]
        allStates.addAll(substates)
        substates.forEach[sub|getStates(sub)]
    }
    
    private def getStates(IState state){
        val substates = state.subvertexes.filter[s|!(s instanceof IPseudostate || s instanceof IFinalState)] as IState[]
        allStates.addAll(substates)
        substates.forEach[sub|getStates(sub)]
    }
    
    def getEvents(){
        statemachine?.transitions.map[t|t.event].filter[e|e.trim.length>1]
    }
    
    def getInitialState(){
        var initialPseudo = statemachine?.vertexes?.filter(IPseudostate).filter[s|s.isInitialPseudostate].get(0)
        return initialPseudo?.outgoings.get(0).target.name
    }
        def ITransition[] getAllParentTransitions(IState state){
        if(state.container instanceof IState){
            return state.outgoings + (getAllParentTransitions(state.container as IState) as ITransition[]);
        }else{
            return state.outgoings
        }
    }
    
    def generateStateTable(){
        var table = new HashMap<String, HashMap<String, IVertex>>
        for(o : states){
            val s = o as IState
            val eventToNextState = new HashMap<String, IVertex>
            table.put(s.name, eventToNextState)
            getAllParentTransitions(s).forEach[e|eventToNextState.put(e.event, e.target)]
        }
        return table
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
        val utils = new GeneratorUtils
//        utils.test
        for(c : utils.classes){
            utils.iclass = c
            utils.statemachine = utils.statemachines.get(c)
            println(c.name)
            utils.allReferenceClasses.forEach[r|println(" reference:"+r.name)]
            if(utils.statemachine!=null){
            var table = utils.generateStateTable()
            table.forEach[state, map|
                println(state);
                map.forEach[event, next| println(" "+event+"->"+next)]
            ]}
        }
    }
    
    def test(){
//        this.classes = new ArrayList<IClass>()
        var api = AstahAPI.getAstahAPI();
        var pa = api.getProjectAccessor();
        pa.open("Create2.asta");
        recursiveClassCollect(pa.project, classes)
        classes.stereotypeNotFilter("library").forEach[c|println(c)]
    }
}