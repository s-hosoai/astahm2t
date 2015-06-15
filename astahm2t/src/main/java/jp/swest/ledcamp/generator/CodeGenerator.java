package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.google.common.base.Objects;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jp.swest.ledcamp.generator.GeneratorUtils;
import jp.swest.ledcamp.generator.GroovyGenerator;
import jp.swest.ledcamp.setting.GenerateSetting;
import jp.swest.ledcamp.setting.GenerateType;
import jp.swest.ledcamp.setting.SettingManager;
import jp.swest.ledcamp.setting.TemplateMap;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.MapExtensions;

@SuppressWarnings("all")
public class CodeGenerator {
  public static void generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {
    final GroovyGenerator generator = new GroovyGenerator();
    final SettingManager settingManager = SettingManager.getInstance();
    final GenerateSetting setting = settingManager.get("test");
    final HashMap<String, Object> map = new HashMap<String, Object>();
    final GeneratorUtils utils = new GeneratorUtils();
    List<IClass> _classes = utils.getClasses();
    for (final IClass iClass : _classes) {
      {
        utils.setIclass(iClass);
        HashMap<IClass, IStateMachine> _statemachines = utils.getStatemachines();
        IStateMachine _get = _statemachines.get(iClass);
        utils.setStatemachine(_get);
        IStateMachine _statemachine = utils.getStatemachine();
        boolean _equals = Objects.equal(_statemachine, null);
        InputOutput.<Boolean>println(Boolean.valueOf(_equals));
        map.put("u", utils);
        HashMap<String, TemplateMap> _mapping = setting.getMapping();
        final Function2<String, TemplateMap, Boolean> _function = new Function2<String, TemplateMap, Boolean>() {
          public Boolean apply(final String k, final TemplateMap v) {
            GenerateType _generateType = v.getGenerateType();
            return Boolean.valueOf(Objects.equal(_generateType, GenerateType.Default));
          }
        };
        Map<String, TemplateMap> _filter = MapExtensions.<String, TemplateMap>filter(_mapping, _function);
        Collection<TemplateMap> _values = _filter.values();
        for (final TemplateMap mapping : _values) {
          String _targetPath = setting.getTargetPath();
          String _name = iClass.getName();
          String _plus = (_targetPath + _name);
          String _plus_1 = (_plus + ".");
          String _fileExtension = mapping.getFileExtension();
          String _plus_2 = (_plus_1 + _fileExtension);
          String _templatePath = setting.getTemplatePath();
          String _templateFile = mapping.getTemplateFile();
          String _plus_3 = (_templatePath + _templateFile);
          generator.doGenerate(map, _plus_2, _plus_3);
        }
      }
    }
    JFrame _frame = utils.getFrame();
    JOptionPane.showMessageDialog(_frame, "Generate Finish");
  }
}
