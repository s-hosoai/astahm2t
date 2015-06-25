package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.google.common.base.Objects;
import groovy.lang.MissingPropertyException;
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
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CodeGenerator {
  public static void generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {
    try {
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
            try {
              String _name = iClass.getName();
              String _plus = (_name + ".");
              String _fileExtension = mapping.getFileExtension();
              String _plus_1 = (_plus + _fileExtension);
              Path _resolve = targetPath.resolve(_plus_1);
              String _templateFile = mapping.getTemplateFile();
              Path _resolve_1 = templatePath.resolve(_templateFile);
              generator.doGenerate(map, _resolve, _resolve_1);
            } catch (final Throwable _t) {
              if (_t instanceof Exception) {
                final Exception e = (Exception)_t;
                boolean _matched = false;
                if (!_matched) {
                  if (Objects.equal(e, MissingPropertyException.class)) {
                    _matched=true;
                    JFrame _frame = utils.getFrame();
                    String _message = e.getMessage();
                    String _plus_2 = ("Cannot found property :" + _message);
                    String _plus_3 = (_plus_2 + ". model : ");
                    String _name_1 = iClass.getName();
                    String _plus_4 = (_plus_3 + _name_1);
                    JOptionPane.showMessageDialog(_frame, _plus_4);
                  }
                }
                if (!_matched) {
                  JFrame _frame_1 = utils.getFrame();
                  String _message_1 = e.getMessage();
                  String _plus_5 = (_message_1 + ".\n in model : ");
                  String _name_2 = iClass.getName();
                  String _plus_6 = (_plus_5 + _name_2);
                  JOptionPane.showMessageDialog(_frame_1, _plus_6);
                }
                return;
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
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
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
