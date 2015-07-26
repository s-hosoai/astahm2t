package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.google.common.base.Objects;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import jp.swest.ledcamp.exception.GenerationException;
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
  public static class DeleteDirVisitor extends SimpleFileVisitor<Path> {
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }
    
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
      boolean _equals = Objects.equal(exc, null);
      if (_equals) {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
      throw exc;
    }
  }
  
  public static class ConflictCheckVisitor extends SimpleFileVisitor<Path> {
    private Path targetPath;
    
    private Path temporalPath;
    
    private Path prevTempPath;
    
    public ConflictCheckVisitor(final Path targetPath, final Path temporalPath, final Path prevTemp) {
      this.targetPath = targetPath;
      this.temporalPath = temporalPath;
      this.prevTempPath = prevTemp;
    }
    
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
      Path _resolve = this.temporalPath.resolve(CodeGenerator.TEMP_GENDIR);
      Path _relativize = _resolve.relativize(file);
      final Path targetFile = this.targetPath.resolve(_relativize);
      Path _resolve_1 = this.temporalPath.resolve(CodeGenerator.TEMP_GENDIR);
      Path _relativize_1 = _resolve_1.relativize(file);
      final Path prevTempFile = this.prevTempPath.resolve(_relativize_1);
      boolean _and = false;
      boolean _exists = Files.exists(targetFile);
      if (!_exists) {
        _and = false;
      } else {
        boolean _exists_1 = Files.exists(prevTempFile);
        _and = _exists_1;
      }
      if (_and) {
        List<String> _readAllLines = Files.readAllLines(prevTempFile);
        List<String> _readAllLines_1 = Files.readAllLines(targetFile);
        final Patch<String> prev_target_diff = DiffUtils.<String>diff(_readAllLines, _readAllLines_1);
        List<Delta<String>> _deltas = prev_target_diff.getDeltas();
        int _length = ((Object[])Conversions.unwrapArray(_deltas, Object.class)).length;
        boolean _greaterThan = (_length > 0);
        if (_greaterThan) {
          List<String> _readAllLines_2 = Files.readAllLines(prevTempFile);
          List<String> _readAllLines_3 = Files.readAllLines(file);
          final Patch<String> prev_gen_diff = DiffUtils.<String>diff(_readAllLines_2, _readAllLines_3);
          List<Delta<String>> _deltas_1 = prev_target_diff.getDeltas();
          final Consumer<Delta<String>> _function = new Consumer<Delta<String>>() {
            public void accept(final Delta<String> it) {
              prev_gen_diff.addDelta(it);
            }
          };
          _deltas_1.forEach(_function);
          try {
            List<String> _readAllLines_4 = Files.readAllLines(prevTempFile);
            final List<String> mergedList = DiffUtils.<String>patch(_readAllLines_4, prev_gen_diff);
            Files.write(file, mergedList);
          } catch (final Throwable _t) {
            if (_t instanceof PatchFailedException) {
              final PatchFailedException e = (PatchFailedException)_t;
              GenerationException _instance = GenerationException.getInstance();
              _instance.addException(e);
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
      Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
      return FileVisitResult.CONTINUE;
    }
    
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
      Path _relativize = this.temporalPath.relativize(dir);
      final Path targetDir = this.targetPath.resolve(_relativize);
      boolean _exists = Files.exists(targetDir);
      boolean _not = (!_exists);
      if (_not) {
        Files.createDirectories(targetDir);
      }
      return FileVisitResult.CONTINUE;
    }
  }
  
  private final static String PREV_GENDIR = "prevGen";
  
  private final static String TEMP_GENDIR = "gen";
  
  public static void generate() throws GenerationException {
    GenerationException _instance = GenerationException.getInstance();
    List<Exception> _excetpions = _instance.getExcetpions();
    _excetpions.clear();
    final GroovyGenerator generator = new GroovyGenerator();
    final SettingManager settingManager = SettingManager.getInstance();
    final GenerateSetting setting = settingManager.getCurrentSetting();
    final HashMap<String, Object> map = new HashMap<String, Object>();
    final GeneratorUtils utils = new GeneratorUtils();
    String _templatePath = setting.getTemplatePath();
    final Path templatePath = Paths.get(_templatePath);
    String _targetPath = setting.getTargetPath();
    final Path targetPath = Paths.get(_targetPath);
    String _m2tPluginFolderPath = settingManager.getM2tPluginFolderPath();
    Path _get = Paths.get(_m2tPluginFolderPath);
    Path _resolve = _get.resolve("projects");
    String _astahProjectName = utils.getAstahProjectName();
    final Path temporalTargetRoot = _resolve.resolve(_astahProjectName);
    final Path temporalTargetPath = temporalTargetRoot.resolve(CodeGenerator.TEMP_GENDIR);
    final Path prevTemporalTargetPath = temporalTargetRoot.resolve(CodeGenerator.PREV_GENDIR);
    boolean _exists = Files.exists(prevTemporalTargetPath);
    boolean _not = (!_exists);
    if (_not) {
      try {
        Files.createDirectories(prevTemporalTargetPath);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          GenerationException _instance_1 = GenerationException.getInstance();
          _instance_1.addException(e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    boolean _exists_1 = Files.exists(temporalTargetPath, LinkOption.NOFOLLOW_LINKS);
    boolean _not_1 = (!_exists_1);
    if (_not_1) {
      try {
        Files.createDirectories(temporalTargetPath);
      } catch (final Throwable _t_1) {
        if (_t_1 instanceof Exception) {
          final Exception e_1 = (Exception)_t_1;
          GenerationException _instance_2 = GenerationException.getInstance();
          _instance_2.addException(e_1);
        } else {
          throw Exceptions.sneakyThrow(_t_1);
        }
      }
    }
    List<IClass> _classes = utils.getClasses();
    for (final IClass iClass : _classes) {
      {
        utils.setIclass(iClass);
        HashMap<IClass, IStateMachine> _statemachines = utils.getStatemachines();
        IStateMachine _get_1 = _statemachines.get(iClass);
        utils.setStatemachine(_get_1);
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
              Path _resolve_1 = temporalTargetPath.resolve(_plus_1);
              String _templateFile = mapping.getTemplateFile();
              Path _resolve_2 = templatePath.resolve(_templateFile);
              generator.doGenerate(map, _resolve_1, _resolve_2);
            } catch (final Throwable _t_2) {
              if (_t_2 instanceof Exception) {
                final Exception e_2 = (Exception)_t_2;
                GenerationException _instance_3 = GenerationException.getInstance();
                _instance_3.addException(e_2);
              } else {
                throw Exceptions.sneakyThrow(_t_2);
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
                String _name_1 = iClass.getName();
                String _plus_2 = (_name_1 + ".");
                String _fileExtension_1 = mapping_1.getFileExtension();
                String _plus_3 = (_plus_2 + _fileExtension_1);
                Path _resolve_3 = temporalTargetPath.resolve(_plus_3);
                String _templateFile_1 = mapping_1.getTemplateFile();
                Path _resolve_4 = templatePath.resolve(_templateFile_1);
                generator.doGenerate(map, _resolve_3, _resolve_4);
              } catch (final Throwable _t_3) {
                if (_t_3 instanceof Exception) {
                  final Exception e_3 = (Exception)_t_3;
                  GenerationException _instance_4 = GenerationException.getInstance();
                  _instance_4.addException(e_3);
                } else {
                  throw Exceptions.sneakyThrow(_t_3);
                }
              }
            }
          }
        }
      }
    }
    map.put("u", utils);
    HashSet<TemplateMap> _mapping = setting.getMapping();
    final Function1<TemplateMap, Boolean> _function = new Function1<TemplateMap, Boolean>() {
      public Boolean apply(final TemplateMap v) {
        TemplateType _templateType = v.getTemplateType();
        return Boolean.valueOf(Objects.equal(_templateType, TemplateType.Global));
      }
    };
    Iterable<TemplateMap> _filter = IterableExtensions.<TemplateMap>filter(_mapping, _function);
    for (final TemplateMap mapping : _filter) {
      try {
        String _fileName = mapping.getFileName();
        Path _resolve_1 = temporalTargetPath.resolve(_fileName);
        String _templateFile = mapping.getTemplateFile();
        Path _resolve_2 = templatePath.resolve(_templateFile);
        generator.doGenerate(map, _resolve_1, _resolve_2);
      } catch (final Throwable _t_2) {
        if (_t_2 instanceof Exception) {
          final Exception e_2 = (Exception)_t_2;
          GenerationException _instance_3 = GenerationException.getInstance();
          _instance_3.addException(e_2);
        } else {
          throw Exceptions.sneakyThrow(_t_2);
        }
      }
    }
    try {
      CodeGenerator.ConflictCheckVisitor _conflictCheckVisitor = new CodeGenerator.ConflictCheckVisitor(targetPath, temporalTargetRoot, prevTemporalTargetPath);
      Files.walkFileTree(temporalTargetPath, _conflictCheckVisitor);
      CodeGenerator.DeleteDirVisitor _deleteDirVisitor = new CodeGenerator.DeleteDirVisitor();
      Files.walkFileTree(prevTemporalTargetPath, _deleteDirVisitor);
      Files.deleteIfExists(prevTemporalTargetPath);
      Files.move(temporalTargetPath, prevTemporalTargetPath);
    } catch (final Throwable _t_3) {
      if (_t_3 instanceof Exception) {
        final Exception e_3 = (Exception)_t_3;
        GenerationException _instance_4 = GenerationException.getInstance();
        _instance_4.addException(e_3);
      } else {
        throw Exceptions.sneakyThrow(_t_3);
      }
    }
    GenerationException _instance_5 = GenerationException.getInstance();
    List<Exception> _excetpions_1 = _instance_5.getExcetpions();
    int _size = _excetpions_1.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      throw GenerationException.getInstance();
    }
  }
  
  public static void main(final String[] args) {
    try {
      final Path tempRoot = Paths.get("C:/Users/hosoai/.astah/plugins/m2t/projects/JavaSample/gen/");
      final Path prevTempRoot = Paths.get("C:/Users/hosoai/.astah/plugins/m2t/projects/JavaSample/prevGen/");
      final Path targetPath = Paths.get("C:/Users/hosoai/.astah/plugins/m2t/target/JavaSample/");
      CodeGenerator.ConflictCheckVisitor _conflictCheckVisitor = new CodeGenerator.ConflictCheckVisitor(targetPath, tempRoot, prevTempRoot);
      Files.walkFileTree(tempRoot, _conflictCheckVisitor);
      CodeGenerator.DeleteDirVisitor _deleteDirVisitor = new CodeGenerator.DeleteDirVisitor();
      Files.walkFileTree(prevTempRoot, _deleteDirVisitor);
      Files.move(tempRoot, prevTempRoot);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
