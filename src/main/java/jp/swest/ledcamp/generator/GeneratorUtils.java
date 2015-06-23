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
import com.change_vision.jude.api.inf.view.IViewManager;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
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
      AstahAPI _astahAPI = AstahAPI.getAstahAPI();
      this.api = _astahAPI;
      ProjectAccessor _projectAccessor = this.api.getProjectAccessor();
      this.projectAccessor = _projectAccessor;
      this.projectAccessor.open("Create2.asta");
      IModel _project = this.projectAccessor.getProject();
      this.projectRoot = _project;
      ArrayList<IClass> _arrayList = new ArrayList<IClass>();
      this.classes = _arrayList;
      HashMap<IClass, IStateMachine> _hashMap = new HashMap<IClass, IStateMachine>();
      this.statemachines = _hashMap;
      INamedElement[] _ownedElements = this.projectRoot.getOwnedElements();
      Iterable<IClass> _filter = Iterables.<IClass>filter(((Iterable<?>)Conversions.doWrapArray(_ownedElements)), IClass.class);
      for (final IClass iClass : _filter) {
        {
          this.classes.add(iClass);
          IDiagram[] _diagrams = iClass.getDiagrams();
          Iterable<IStateMachineDiagram> _filter_1 = Iterables.<IStateMachineDiagram>filter(((Iterable<?>)Conversions.doWrapArray(_diagrams)), IStateMachineDiagram.class);
          for (final IStateMachineDiagram diagram : _filter_1) {
            IStateMachine _stateMachine = diagram.getStateMachine();
            this.statemachines.put(iClass, _stateMachine);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String getName() {
    return this.iclass.getName();
  }
  
  public String getInstanceName() {
    String _name = this.iclass.getName();
    return StringExtensions.toFirstLower(_name);
  }
  
  public Iterable<IClass> getAllReferenceClasses() {
    IAttribute[] _attributes = this.iclass.getAttributes();
    final Function1<IAttribute, IClass> _function = new Function1<IAttribute, IClass>() {
      public IClass apply(final IAttribute e) {
        return e.getType();
      }
    };
    List<IClass> _map = ListExtensions.<IAttribute, IClass>map(((List<IAttribute>)Conversions.doWrapArray(_attributes)), _function);
    final Function1<IClass, Boolean> _function_1 = new Function1<IClass, Boolean>() {
      public Boolean apply(final IClass e) {
        return Boolean.valueOf(GeneratorUtils.this.classes.contains(e));
      }
    };
    return IterableExtensions.<IClass>filter(_map, _function_1);
  }
  
  private ArrayList<IState> allStates;
  
  public ArrayList<IState> getStates() {
    boolean _equals = Objects.equal(this.statemachine, null);
    if (_equals) {
      return null;
    }
    ArrayList<IState> _arrayList = new ArrayList<IState>();
    this.allStates = _arrayList;
    this.getStates(this.statemachine);
    return this.allStates;
  }
  
  private void getStates(final IStateMachine m) {
    IVertex[] _vertexes = m.getVertexes();
    final Function1<IVertex, Boolean> _function = new Function1<IVertex, Boolean>() {
      public Boolean apply(final IVertex s) {
        return Boolean.valueOf((!((s instanceof IPseudostate) || (s instanceof IFinalState))));
      }
    };
    Iterable<IVertex> _filter = IterableExtensions.<IVertex>filter(((Iterable<IVertex>)Conversions.doWrapArray(_vertexes)), _function);
    final IState[] substates = ((IState[]) ((IState[])Conversions.unwrapArray(_filter, IState.class)));
    CollectionExtensions.<IState>addAll(this.allStates, substates);
    final Procedure1<IState> _function_1 = new Procedure1<IState>() {
      public void apply(final IState sub) {
        GeneratorUtils.this.getStates(sub);
      }
    };
    IterableExtensions.<IState>forEach(((Iterable<IState>)Conversions.doWrapArray(substates)), _function_1);
  }
  
  private void getStates(final IState state) {
    IVertex[] _subvertexes = state.getSubvertexes();
    final Function1<IVertex, Boolean> _function = new Function1<IVertex, Boolean>() {
      public Boolean apply(final IVertex s) {
        return Boolean.valueOf((!((s instanceof IPseudostate) || (s instanceof IFinalState))));
      }
    };
    Iterable<IVertex> _filter = IterableExtensions.<IVertex>filter(((Iterable<IVertex>)Conversions.doWrapArray(_subvertexes)), _function);
    final IState[] substates = ((IState[]) ((IState[])Conversions.unwrapArray(_filter, IState.class)));
    CollectionExtensions.<IState>addAll(this.allStates, substates);
    final Procedure1<IState> _function_1 = new Procedure1<IState>() {
      public void apply(final IState sub) {
        GeneratorUtils.this.getStates(sub);
      }
    };
    IterableExtensions.<IState>forEach(((Iterable<IState>)Conversions.doWrapArray(substates)), _function_1);
  }
  
  public Iterable<String> getEvents() {
    ITransition[] _transitions = null;
    if (this.statemachine!=null) {
      _transitions=this.statemachine.getTransitions();
    }
    final Function1<ITransition, String> _function = new Function1<ITransition, String>() {
      public String apply(final ITransition t) {
        return t.getEvent();
      }
    };
    List<String> _map = ListExtensions.<ITransition, String>map(((List<ITransition>)Conversions.doWrapArray(_transitions)), _function);
    final Function1<String, Boolean> _function_1 = new Function1<String, Boolean>() {
      public Boolean apply(final String e) {
        String _trim = e.trim();
        int _length = _trim.length();
        return Boolean.valueOf((_length > 1));
      }
    };
    return IterableExtensions.<String>filter(_map, _function_1);
  }
  
  public String getInitialState() {
    IVertex[] _vertexes = null;
    if (this.statemachine!=null) {
      _vertexes=this.statemachine.getVertexes();
    }
    Iterable<IPseudostate> _filter = null;
    if (((Iterable<?>)Conversions.doWrapArray(_vertexes))!=null) {
      _filter=Iterables.<IPseudostate>filter(((Iterable<?>)Conversions.doWrapArray(_vertexes)), IPseudostate.class);
    }
    final Function1<IPseudostate, Boolean> _function = new Function1<IPseudostate, Boolean>() {
      public Boolean apply(final IPseudostate s) {
        return Boolean.valueOf(s.isInitialPseudostate());
      }
    };
    Iterable<IPseudostate> _filter_1 = IterableExtensions.<IPseudostate>filter(_filter, _function);
    IPseudostate initialPseudo = ((IPseudostate[])Conversions.unwrapArray(_filter_1, IPseudostate.class))[0];
    ITransition[] _outgoings = null;
    if (initialPseudo!=null) {
      _outgoings=initialPseudo.getOutgoings();
    }
    ITransition _get = _outgoings[0];
    IVertex _target = _get.getTarget();
    return _target.getName();
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
        String _name = s.getName();
        table.put(_name, eventToNextState);
        ITransition[] _allParentTransitions = this.getAllParentTransitions(s);
        final Procedure1<ITransition> _function = new Procedure1<ITransition>() {
          public void apply(final ITransition e) {
            String _event = e.getEvent();
            IVertex _target = e.getTarget();
            eventToNextState.put(_event, _target);
          }
        };
        IterableExtensions.<ITransition>forEach(((Iterable<ITransition>)Conversions.doWrapArray(_allParentTransitions)), _function);
      }
    }
    return table;
  }
  
  public List<IClass> getClasses() {
    return this.classes;
  }
  
  public JFrame getFrame() {
    try {
      IViewManager _viewManager = this.projectAccessor.getViewManager();
      return _viewManager.getMainFrame();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Iterable<IClass> stereotypeFilter(final List<IClass> classes, final String stereotype) {
    final Function1<IClass, Boolean> _function = new Function1<IClass, Boolean>() {
      public Boolean apply(final IClass c) {
        String[] _stereotypes = c.getStereotypes();
        return Boolean.valueOf(((List<String>)Conversions.doWrapArray(_stereotypes)).contains(stereotype));
      }
    };
    return IterableExtensions.<IClass>filter(classes, _function);
  }
  
  public Iterable<IClass> stereotypeNotFilter(final List<IClass> classes, final String stereotype) {
    final Function1<IClass, Boolean> _function = new Function1<IClass, Boolean>() {
      public Boolean apply(final IClass c) {
        String[] _stereotypes = c.getStereotypes();
        boolean _contains = ((List<String>)Conversions.doWrapArray(_stereotypes)).contains(stereotype);
        return Boolean.valueOf((!_contains));
      }
    };
    return IterableExtensions.<IClass>filter(classes, _function);
  }
  
  private Object recursiveClassCollect(final IModel model, final List<IClass> classes) {
    INamedElement[] _ownedElements = model.getOwnedElements();
    Iterable<IClass> _filter = Iterables.<IClass>filter(((Iterable<?>)Conversions.doWrapArray(_ownedElements)), IClass.class);
    Iterables.<IClass>addAll(classes, _filter);
    INamedElement[] _ownedElements_1 = model.getOwnedElements();
    Iterable<IPackage> _filter_1 = Iterables.<IPackage>filter(((Iterable<?>)Conversions.doWrapArray(_ownedElements_1)), IPackage.class);
    final Procedure1<IPackage> _function = new Procedure1<IPackage>() {
      public void apply(final IPackage p) {
        GeneratorUtils.this.recursiveClassCollect(p, classes);
      }
    };
    IterableExtensions.<IPackage>forEach(_filter_1, _function);
    return null;
  }
  
  private void recursiveClassCollect(final IPackage model, final List<IClass> classes) {
    INamedElement[] _ownedElements = model.getOwnedElements();
    Iterable<IClass> _filter = Iterables.<IClass>filter(((Iterable<?>)Conversions.doWrapArray(_ownedElements)), IClass.class);
    Iterables.<IClass>addAll(classes, _filter);
    INamedElement[] _ownedElements_1 = model.getOwnedElements();
    Iterable<IPackage> _filter_1 = Iterables.<IPackage>filter(((Iterable<?>)Conversions.doWrapArray(_ownedElements_1)), IPackage.class);
    final Procedure1<IPackage> _function = new Procedure1<IPackage>() {
      public void apply(final IPackage p) {
        GeneratorUtils.this.recursiveClassCollect(p, classes);
      }
    };
    IterableExtensions.<IPackage>forEach(_filter_1, _function);
  }
  
  public static void main(final String[] args) {
    final GeneratorUtils utils = new GeneratorUtils();
    for (final IClass c : utils.classes) {
      {
        utils.iclass = c;
        IStateMachine _get = utils.statemachines.get(c);
        utils.statemachine = _get;
        String _name = c.getName();
        InputOutput.<String>println(_name);
        Iterable<IClass> _allReferenceClasses = utils.getAllReferenceClasses();
        final Procedure1<IClass> _function = new Procedure1<IClass>() {
          public void apply(final IClass r) {
            String _name = r.getName();
            String _plus = (" reference:" + _name);
            InputOutput.<String>println(_plus);
          }
        };
        IterableExtensions.<IClass>forEach(_allReferenceClasses, _function);
        boolean _notEquals = (!Objects.equal(utils.statemachine, null));
        if (_notEquals) {
          HashMap<String, HashMap<String, IVertex>> table = utils.generateStateTable();
          final Procedure2<String, HashMap<String, IVertex>> _function_1 = new Procedure2<String, HashMap<String, IVertex>>() {
            public void apply(final String state, final HashMap<String, IVertex> map) {
              InputOutput.<String>println(state);
              final Procedure2<String, IVertex> _function = new Procedure2<String, IVertex>() {
                public void apply(final String event, final IVertex next) {
                  InputOutput.<String>println((((" " + event) + "->") + next));
                }
              };
              MapExtensions.<String, IVertex>forEach(map, _function);
            }
          };
          MapExtensions.<String, HashMap<String, IVertex>>forEach(table, _function_1);
        }
      }
    }
  }
  
  public void test() {
    try {
      AstahAPI api = AstahAPI.getAstahAPI();
      ProjectAccessor pa = api.getProjectAccessor();
      pa.open("Create2.asta");
      IModel _project = pa.getProject();
      this.recursiveClassCollect(_project, this.classes);
      Iterable<IClass> _stereotypeNotFilter = this.stereotypeNotFilter(this.classes, "library");
      final Procedure1<IClass> _function = new Procedure1<IClass>() {
        public void apply(final IClass c) {
          InputOutput.<IClass>println(c);
        }
      };
      IterableExtensions.<IClass>forEach(_stereotypeNotFilter, _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
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
