package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.google.common.base.Objects;
import java.awt.HeadlessException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jp.swest.ledcamp.generator.GeneratorUtils;
import jp.swest.ledcamp.generator.GroovyGenerator;
import jp.swest.ledcamp.setting.GenerateSetting;
import jp.swest.ledcamp.setting.SettingManager;
import jp.swest.ledcamp.setting.TemplateMap;
import jp.swest.ledcamp.setting.TemplateType;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CodeGenerator {
  public static void generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {
    final GroovyGenerator generator = new GroovyGenerator();
    final SettingManager settingManager = SettingManager.getInstance();
    final GenerateSetting setting = settingManager.getCurrentSetting();
    final HashMap<String, Object> map = new HashMap<String, Object>();
    final GeneratorUtils utils = new GeneratorUtils();
    String _templatePath = setting.getTemplatePath();
    final Path templatePath = Paths.get(_templatePath);
    String _targetPath = setting.getTargetPath();
    final Path targetPath = Paths.get(_targetPath);
    boolean _exists = Files.exists(targetPath, LinkOption.NOFOLLOW_LINKS);
    if (_exists) {
      Files.createDirectories(targetPath);
    }
    List<IClass> _classes = utils.getClasses();
    for (final IClass iClass : _classes) {
      {
        utils.setIclass(iClass);
        HashMap<IClass, IStateMachine> _statemachines = utils.getStatemachines();
        IStateMachine _get = _statemachines.get(iClass);
        utils.setStatemachine(_get);
        map.put("u", utils);
        HashSet<TemplateMap> _mapping = setting.getMapping();
        final Function1<TemplateMap, Boolean> _function = new Function1<TemplateMap, Boolean>() {
          public Boolean apply(final TemplateMap v) {
            TemplateType _templateType = v.getTemplateType();
            return Boolean.valueOf(Objects.equal(_templateType, TemplateType.Default));
          }
        };
        Iterable<TemplateMap> _filter = IterableExtensions.<TemplateMap>filter(_mapping, _function);
        for (final TemplateMap mapping : _filter) {
          String _name = iClass.getName();
          String _plus = (_name + ".");
          String _fileExtension = mapping.getFileExtension();
          String _plus_1 = (_plus + _fileExtension);
          Path _resolve = targetPath.resolve(_plus_1);
          String _templateFile = mapping.getTemplateFile();
          Path _resolve_1 = templatePath.resolve(_templateFile);
          generator.doGenerate(map, _resolve, _resolve_1);
        }
      }
    }
    HashSet<TemplateMap> _mapping = setting.getMapping();
    final Function1<TemplateMap, Boolean> _function = new Function1<TemplateMap, Boolean>() {
      public Boolean apply(final TemplateMap v) {
        TemplateType _templateType = v.getTemplateType();
        return Boolean.valueOf(Objects.equal(_templateType, TemplateType.Global));
      }
    };
    Iterable<TemplateMap> _filter = IterableExtensions.<TemplateMap>filter(_mapping, _function);
    for (final TemplateMap mapping : _filter) {
      {
        map.put("u", utils);
        String _fileName = mapping.getFileName();
        Path _resolve = targetPath.resolve(_fileName);
        String _templateFile = mapping.getTemplateFile();
        Path _resolve_1 = templatePath.resolve(_templateFile);
        generator.doGenerate(map, _resolve, _resolve_1);
      }
    }
    JFrame _frame = utils.getFrame();
    JOptionPane.showMessageDialog(_frame, "Generate Finish");
  }
}
