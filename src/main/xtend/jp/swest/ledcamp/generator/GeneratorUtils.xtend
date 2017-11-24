package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.AstahAPI
import com.change_vision.jude.api.inf.model.IClass
import com.change_vision.jude.api.inf.model.IFinalState
import com.change_vision.jude.api.inf.model.IModel
import com.change_vision.jude.api.inf.model.IPackage
import com.change_vision.jude.api.inf.model.IPseudostate
import com.change_vision.jude.api.inf.model.IState
import com.change_vision.jude.api.inf.model.IStateMachine
import com.change_vision.jude.api.inf.model.IStateMachineDiagram
import com.change_vision.jude.api.inf.model.ITransition
import com.change_vision.jude.api.inf.model.IVertex
import com.change_vision.jude.api.inf.project.ProjectAccessor
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors
import com.change_vision.jude.api.inf.model.INamedElement

class GeneratorUtils {
    @Accessors private AstahAPI api
    @Accessors private ProjectAccessor projectAccessor
    @Accessors private IModel projectRoot
    @Accessors private IClass iclass
    @Accessors private IStateMachine statemachine
    @Accessors List<IClass> classes
    @Accessors HashMap<IClass, IStateMachine> statemachines

    new() {
        api = AstahAPI.getAstahAPI()
        projectAccessor = api.getProjectAccessor()
        projectRoot = projectAccessor.getProject() // exist project?
        // Collect classes and statemachines.
        classes = new ArrayList<IClass>()
        statemachines = new HashMap<IClass, IStateMachine>()
        getAllClassAndStatemachines(projectRoot, classes)
    }

    def void getAllClassAndStatemachines(INamedElement element, List<IClass> classes){
        switch(element){
            IPackage : {element.ownedElements.forEach[getAllClassAndStatemachines(it, classes)]}
            IClass : {
                classes.add(element)
                classes.map[it.diagrams.toList].flatten.filter(IStateMachineDiagram).forEach[statemachines.put(element, it.stateMachine)]
            }
        }
    }

    def getAstahProjectName() {
        projectRoot.name
    }

    // for class diagram utility
    def getName() {
        iclass.name
    }

    def getInstanceName() {
        iclass.name.toFirstLower
    }

    def getInstanceName(IClass c) {
        c.name.toFirstLower
    }

    def toFirstUpperCase(String str) {
        str.toFirstUpper
    }

    def toFirstLowerCase(String str) {
        str.toFirstLower
    }

    def getAllReferenceClasses() {
        iclass.attributes.map[e|e.type].filter[e|classes.contains(e)]
    }

    // for state machine utility
    private ArrayList<IState> allStates

    def getStates() {
        if (statemachine === null) {
            return null
        }
        allStates = new ArrayList
        getStates(statemachine)
        return allStates
    }

    private def void getStates(IStateMachine m) {
        val substates = m.vertexes.filter[s|!(s instanceof IPseudostate || s instanceof IFinalState)] as IState[]
        allStates.addAll(substates)
        substates.forEach[sub|getStates(sub)]
    }

    private def void getStates(IState state) {
        val substates = state.subvertexes.filter[s|!(s instanceof IPseudostate || s instanceof IFinalState)] as IState[]
        allStates.addAll(substates)
        substates.forEach[sub|getStates(sub)]
    }

    def getEvents() {
        statemachine?.transitions.map[t|t.event].filter[e|e.trim.length > 1].toSet
    }

    def getEvents(IClass c) {
        val _statemachine = statemachines.get(c)
        _statemachine?.transitions?.filter[it.event !== null && it.event.trim.length > 1 && it.event.trim != "true"].map [
            it.event
        ].toSet
    }

    def getAllEvents() {
        statemachines.values.map[s|s.transitions.toList].flatten.map[t|t.event].filter[e|e.trim.length > 1].toSet
    }

    def getInitialState() {
        var initialPseudo = statemachine?.vertexes?.filter(IPseudostate)?.filter[s|s.isInitialPseudostate]?.get(0)
        return initialPseudo?.outgoings?.get(0)?.target
    }

    def ITransition[] getAllParentTransitions(IState state) {
        if (state.container instanceof IState) {
            return state.outgoings + (getAllParentTransitions(state.container as IState) as ITransition[]);
        } else {
            return state.outgoings
        }
    }

    def generateStateTable() {
        var table = new HashMap<String, HashMap<String, IVertex>>
        for (o : states) {
            val s = o as IState
            val eventToNextState = new HashMap<String, IVertex>
            table.put(s.name, eventToNextState)
            getAllParentTransitions(s).forEach[e|eventToNextState.put(e.event, e.target)]
        }
        return table
    }

    // for global generate
    def getClasses() {
        return this.classes
    }

    def getFrame() {
        return projectAccessor.viewManager.mainFrame
    }

    def stereotypeFilter(List<IClass> classes, String stereotype) {
        return classes.filter[c|c.stereotypes.contains(stereotype)]
    }

    def stereotypeNotFilter(List<IClass> classes, String stereotype) {
        return classes.filter[c|!c.stereotypes.contains(stereotype)]
    }
}
