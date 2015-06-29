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
import org.eclipse.xtext.xbase.lib.Conversions;
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
      boolean _not = (!_exists);
      if (_not) {
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
          String[] _stereotypes = iClass.getStereotypes();
          int _size = ((List<String>)Conversions.doWrapArray(_stereotypes)).size();
          boolean _equals = (_size == 0);
          if (_equals) {
            HashSet<TemplateMap> _mapping = setting.getMapping();
            final Function1<TemplateMap, Boolean> _function = new Function1<TemplateMap, Boolean>() {
              public Boolean apply(final TemplateMap it) {
                TemplateType _templateType = it.getTemplateType();
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
          } else {
            String[] _stereotypes_1 = iClass.getStereotypes();
            for (final String stereotype : _stereotypes_1) {
              HashSet<TemplateMap> _mapping_1 = setting.getMapping();
              final Function1<TemplateMap, Boolean> _function_1 = new Function1<TemplateMap, Boolean>() {
                public Boolean apply(final TemplateMap it) {
                  boolean _and = false;
                  TemplateType _templateType = it.getTemplateType();
                  boolean _equals = Objects.equal(_templateType, TemplateType.Stereotype);
                  if (!_equals) {
                    _and = false;
                  } else {
                    String _stereotype = it.getStereotype();
                    boolean _equals_1 = _stereotype.equals(stereotype);
                    _and = _equals_1;
                  }
                  return Boolean.valueOf(_and);
                }
              };
              Iterable<TemplateMap> _filter_1 = IterableExtensions.<TemplateMap>filter(_mapping_1, _function_1);
              for (final TemplateMap mapping_1 : _filter_1) {
                try {
                  String _name_3 = iClass.getName();
                  String _plus_7 = (_name_3 + ".");
                  String _fileExtension_1 = mapping_1.getFileExtension();
                  String _plus_8 = (_plus_7 + _fileExtension_1);
                  Path _resolve_2 = targetPath.resolve(_plus_8);
                  String _templateFile_1 = mapping_1.getTemplateFile();
                  Path _resolve_3 = templatePath.resolve(_templateFile_1);
                  generator.doGenerate(map, _resolve_2, _resolve_3);
                } catch (final Throwable _t_1) {
                  if (_t_1 instanceof Exception) {
                    final Exception e_1 = (Exception)_t_1;
                    boolean _matched_1 = false;
                    if (!_matched_1) {
                      if (Objects.equal(e_1, MissingPropertyException.class)) {
                        _matched_1=true;
                        JFrame _frame_2 = utils.getFrame();
                        String _message_2 = e_1.getMessage();
                        String _plus_9 = ("Cannot found property :" + _message_2);
                        String _plus_10 = (_plus_9 + ". model : ");
                        String _name_4 = iClass.getName();
                        String _plus_11 = (_plus_10 + _name_4);
                        JOptionPane.showMessageDialog(_frame_2, _plus_11);
                      }
                    }
                    if (!_matched_1) {
                      JFrame _frame_3 = utils.getFrame();
                      String _message_3 = e_1.getMessage();
                      String _plus_12 = (_message_3 + ".\n in model : ");
                      String _name_5 = iClass.getName();
                      String _plus_13 = (_plus_12 + _name_5);
                      JOptionPane.showMessageDialog(_frame_3, _plus_13);
                    }
                    return;
                  } else {
                    throw Exceptions.sneakyThrow(_t_1);
                  }
                }
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
