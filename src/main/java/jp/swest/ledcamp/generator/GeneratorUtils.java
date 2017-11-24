package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IFinalState;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IPseudostate;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.ITransition;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.JFrame;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class GeneratorUtils {
  @Accessors
  private AstahAPI api;
  
  @Accessors
  private ProjectAccessor projectAccessor;
  
  @Accessors
  private IModel projectRoot;
  
  @Accessors
  private IClass iclass;
  
  @Accessors
  private IStateMachine statemachine;
  
  @Accessors
  private List<IClass> classes;
  
  @Accessors
  private HashMap<IClass, IStateMachine> statemachines;
  
  public GeneratorUtils() {
    try {
      this.api = AstahAPI.getAstahAPI();
      this.projectAccessor = this.api.getProjectAccessor();
      this.projectRoot = this.projectAccessor.getProject();
      ArrayList<IClass> _arrayList = new ArrayList<IClass>();
      this.classes = _arrayList;
      HashMap<IClass, IStateMachine> _hashMap = new HashMap<IClass, IStateMachine>();
      this.statemachines = _hashMap;
      this.getAllClassAndStatemachines(this.projectRoot, this.classes);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void getAllClassAndStatemachines(final INamedElement element, final List<IClass> classes) {
    boolean _matched = false;
    if (element instanceof IPackage) {
      _matched=true;
      final Consumer<INamedElement> _function = (INamedElement it) -> {
        this.getAllClassAndStatemachines(it, classes);
      };
      ((List<INamedElement>)Conversions.doWrapArray(((IPackage)element).getOwnedElements())).forEach(_function);
    }
    if (!_matched) {
      if (element instanceof IClass) {
        _matched=true;
        classes.add(((IClass)element));
        final Function1<IClass, List<IDiagram>> _function = (IClass it) -> {
          return IterableExtensions.<IDiagram>toList(((Iterable<IDiagram>)Conversions.doWrapArray(it.getDiagrams())));
        };
        final Consumer<IStateMachineDiagram> _function_1 = (IStateMachineDiagram it) -> {
          this.statemachines.put(((IClass)element), it.getStateMachine());
        };
        Iterables.<IStateMachineDiagram>filter(Iterables.<IDiagram>concat(ListExtensions.<IClass, List<IDiagram>>map(classes, _function)), IStateMachineDiagram.class).forEach(_function_1);
      }
    }
  }
  
  public String getAstahProjectName() {
    return this.projectRoot.getName();
  }
  
  public String getName() {
    return this.iclass.getName();
  }
  
  public String getInstanceName() {
    return StringExtensions.toFirstLower(this.iclass.getName());
  }
  
  public String getInstanceName(final IClass c) {
    return StringExtensions.toFirstLower(c.getName());
  }
  
  public String toFirstUpperCase(final String str) {
    return StringExtensions.toFirstUpper(str);
  }
  
  public String toFirstLowerCase(final String str) {
    return StringExtensions.toFirstLower(str);
  }
  
  public Iterable<IClass> getAllReferenceClasses() {
    final Function1<IAttribute, IClass> _function = (IAttribute e) -> {
      return e.getType();
    };
    final Function1<IClass, Boolean> _function_1 = (IClass e) -> {
      return Boolean.valueOf(this.classes.contains(e));
    };
    return IterableExtensions.<IClass>filter(ListExtensions.<IAttribute, IClass>map(((List<IAttribute>)Conversions.doWrapArray(this.iclass.getAttributes())), _function), _function_1);
  }
  
  private ArrayList<IState> allStates;
  
  public ArrayList<IState> getStates() {
    if ((this.statemachine == null)) {
      return null;
    }
    ArrayList<IState> _arrayList = new ArrayList<IState>();
    this.allStates = _arrayList;
    this.getStates(this.statemachine);
    return this.allStates;
  }
  
  private void getStates(final IStateMachine m) {
    final Function1<IVertex, Boolean> _function = (IVertex s) -> {
      return Boolean.valueOf((!((s instanceof IPseudostate) || (s instanceof IFinalState))));
    };
    Iterable<IVertex> _filter = IterableExtensions.<IVertex>filter(((Iterable<IVertex>)Conversions.doWrapArray(m.getVertexes())), _function);
    final IState[] substates = ((IState[]) ((IState[])Conversions.unwrapArray(_filter, IState.class)));
    CollectionExtensions.<IState>addAll(this.allStates, substates);
    final Consumer<IState> _function_1 = (IState sub) -> {
      this.getStates(sub);
    };
    ((List<IState>)Conversions.doWrapArray(substates)).forEach(_function_1);
  }
  
  private void getStates(final IState state) {
    final Function1<IVertex, Boolean> _function = (IVertex s) -> {
      return Boolean.valueOf((!((s instanceof IPseudostate) || (s instanceof IFinalState))));
    };
    Iterable<IVertex> _filter = IterableExtensions.<IVertex>filter(((Iterable<IVertex>)Conversions.doWrapArray(state.getSubvertexes())), _function);
    final IState[] substates = ((IState[]) ((IState[])Conversions.unwrapArray(_filter, IState.class)));
    CollectionExtensions.<IState>addAll(this.allStates, substates);
    final Consumer<IState> _function_1 = (IState sub) -> {
      this.getStates(sub);
    };
    ((List<IState>)Conversions.doWrapArray(substates)).forEach(_function_1);
  }
  
  public Set<String> getEvents() {
    ITransition[] _transitions = null;
    if (this.statemachine!=null) {
      _transitions=this.statemachine.getTransitions();
    }
    final Function1<ITransition, String> _function = (ITransition t) -> {
      return t.getEvent();
    };
    final Function1<String, Boolean> _function_1 = (String e) -> {
      int _length = e.trim().length();
      return Boolean.valueOf((_length > 1));
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<String>filter(ListExtensions.<ITransition, String>map(((List<ITransition>)Conversions.doWrapArray(_transitions)), _function), _function_1));
  }
  
  public Set<String> getEvents(final IClass c) {
    Set<String> _xblockexpression = null;
    {
      final IStateMachine _statemachine = this.statemachines.get(c);
      ITransition[] _transitions = null;
      if (_statemachine!=null) {
        _transitions=_statemachine.getTransitions();
      }
      Iterable<ITransition> _filter = null;
      if (((Iterable<ITransition>)Conversions.doWrapArray(_transitions))!=null) {
        final Function1<ITransition, Boolean> _function = (ITransition it) -> {
          return Boolean.valueOf((((it.getEvent() != null) && (it.getEvent().trim().length() > 1)) && (!Objects.equal(it.getEvent().trim(), "true"))));
        };
        _filter=IterableExtensions.<ITransition>filter(((Iterable<ITransition>)Conversions.doWrapArray(_transitions)), _function);
      }
      final Function1<ITransition, String> _function_1 = (ITransition it) -> {
        return it.getEvent();
      };
      _xblockexpression = IterableExtensions.<String>toSet(IterableExtensions.<ITransition, String>map(_filter, _function_1));
    }
    return _xblockexpression;
  }
  
  public Set<String> getAllEvents() {
    final Function1<IStateMachine, List<ITransition>> _function = (IStateMachine s) -> {
      return IterableExtensions.<ITransition>toList(((Iterable<ITransition>)Conversions.doWrapArray(s.getTransitions())));
    };
    final Function1<ITransition, String> _function_1 = (ITransition t) -> {
      return t.getEvent();
    };
    final Function1<String, Boolean> _function_2 = (String e) -> {
      int _length = e.trim().length();
      return Boolean.valueOf((_length > 1));
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<String>filter(IterableExtensions.<ITransition, String>map(Iterables.<ITransition>concat(IterableExtensions.<IStateMachine, List<ITransition>>map(this.statemachines.values(), _function)), _function_1), _function_2));
  }
  
  public IVertex getInitialState() {
    IVertex[] _vertexes = null;
    if (this.statemachine!=null) {
      _vertexes=this.statemachine.getVertexes();
    }
    Iterable<IPseudostate> _filter = null;
    if (((Iterable<?>)Conversions.doWrapArray(_vertexes))!=null) {
      _filter=Iterables.<IPseudostate>filter(((Iterable<?>)Conversions.doWrapArray(_vertexes)), IPseudostate.class);
    }
    Iterable<IPseudostate> _filter_1 = null;
    if (_filter!=null) {
      final Function1<IPseudostate, Boolean> _function = (IPseudostate s) -> {
        return Boolean.valueOf(s.isInitialPseudostate());
      };
      _filter_1=IterableExtensions.<IPseudostate>filter(_filter, _function);
    }
    IPseudostate _get = null;
    if (((IPseudostate[])Conversions.unwrapArray(_filter_1, IPseudostate.class))!=null) {
      _get=((IPseudostate[])Conversions.unwrapArray(_filter_1, IPseudostate.class))[0];
    }
    IPseudostate initialPseudo = _get;
    ITransition[] _outgoings = null;
    if (initialPseudo!=null) {
      _outgoings=initialPseudo.getOutgoings();
    }
    ITransition _get_1 = null;
    if (_outgoings!=null) {
      _get_1=_outgoings[0];
    }
    IVertex _target = null;
    if (_get_1!=null) {
      _target=_get_1.getTarget();
    }
    return _target;
  }
  
  public ITransition[] getAllParentTransitions(final IState state) {
    IElement _container = state.getContainer();
    if ((_container instanceof IState)) {
      ITransition[] _outgoings = state.getOutgoings();
      IElement _container_1 = state.getContainer();
      ITransition[] _allParentTransitions = this.getAllParentTransitions(((IState) _container_1));
      return ((ITransition[])Conversions.unwrapArray(Iterables.<ITransition>concat(((Iterable<? extends ITransition>)Conversions.doWrapArray(_outgoings)), ((Iterable<? extends ITransition>)Conversions.doWrapArray(((ITransition[]) _allParentTransitions)))), ITransition.class));
    } else {
      return state.getOutgoings();
    }
  }
  
  public HashMap<String, HashMap<String, IVertex>> generateStateTable() {
    HashMap<String, HashMap<String, IVertex>> table = new HashMap<String, HashMap<String, IVertex>>();
    ArrayList<IState> _states = this.getStates();
    for (final IState o : _states) {
      {
        final IState s = ((IState) o);
        final HashMap<String, IVertex> eventToNextState = new HashMap<String, IVertex>();
        table.put(s.getName(), eventToNextState);
        final Consumer<ITransition> _function = (ITransition e) -> {
          eventToNextState.put(e.getEvent(), e.getTarget());
        };
        ((List<ITransition>)Conversions.doWrapArray(this.getAllParentTransitions(s))).forEach(_function);
      }
    }
    return table;
  }
  
  public List<IClass> getClasses() {
    return this.classes;
  }
  
  public JFrame getFrame() {
    try {
      return this.projectAccessor.getViewManager().getMainFrame();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Iterable<IClass> stereotypeFilter(final List<IClass> classes, final String stereotype) {
    final Function1<IClass, Boolean> _function = (IClass c) -> {
      return Boolean.valueOf(((List<String>)Conversions.doWrapArray(c.getStereotypes())).contains(stereotype));
    };
    return IterableExtensions.<IClass>filter(classes, _function);
  }
  
  public Iterable<IClass> stereotypeNotFilter(final List<IClass> classes, final String stereotype) {
    final Function1<IClass, Boolean> _function = (IClass c) -> {
      boolean _contains = ((List<String>)Conversions.doWrapArray(c.getStereotypes())).contains(stereotype);
      return Boolean.valueOf((!_contains));
    };
    return IterableExtensions.<IClass>filter(classes, _function);
  }
  
  @Pure
  public AstahAPI getApi() {
    return this.api;
  }
  
  public void setApi(final AstahAPI api) {
    this.api = api;
  }
  
  @Pure
  public ProjectAccessor getProjectAccessor() {
    return this.projectAccessor;
  }
  
  public void setProjectAccessor(final ProjectAccessor projectAccessor) {
    this.projectAccessor = projectAccessor;
  }
  
  @Pure
  public IModel getProjectRoot() {
    return this.projectRoot;
  }
  
  public void setProjectRoot(final IModel projectRoot) {
    this.projectRoot = projectRoot;
  }
  
  @Pure
  public IClass getIclass() {
    return this.iclass;
  }
  
  public void setIclass(final IClass iclass) {
    this.iclass = iclass;
  }
  
  @Pure
  public IStateMachine getStatemachine() {
    return this.statemachine;
  }
  
  public void setStatemachine(final IStateMachine statemachine) {
    this.statemachine = statemachine;
  }
  
  public void setClasses(final List<IClass> classes) {
    this.classes = classes;
  }
  
  @Pure
  public HashMap<IClass, IStateMachine> getStatemachines() {
    return this.statemachines;
  }
  
  public void setStatemachines(final HashMap<IClass, IStateMachine> statemachines) {
    this.statemachines = statemachines;
  }
}
