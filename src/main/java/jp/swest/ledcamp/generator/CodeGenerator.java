package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.model.IClass;
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
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
      if ((exc == null)) {
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
    
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
      final Path targetFile = this.targetPath.resolve(this.temporalPath.resolve(CodeGenerator.TEMP_GENDIR).relativize(file));
      final Path prevTempFile = this.prevTempPath.resolve(this.temporalPath.resolve(CodeGenerator.TEMP_GENDIR).relativize(file));
      if ((Files.exists(targetFile) && Files.exists(prevTempFile))) {
        final Patch<String> prev_target_diff = DiffUtils.<String>diff(Files.readAllLines(prevTempFile), Files.readAllLines(targetFile));
        int _length = ((Object[])Conversions.unwrapArray(prev_target_diff.getDeltas(), Object.class)).length;
        boolean _greaterThan = (_length > 0);
        if (_greaterThan) {
          final Patch<String> prev_gen_diff = DiffUtils.<String>diff(Files.readAllLines(prevTempFile), Files.readAllLines(file));
          final Consumer<Delta<String>> _function = new Consumer<Delta<String>>() {
            @Override
            public void accept(final Delta<String> it) {
              prev_gen_diff.addDelta(it);
            }
          };
          prev_target_diff.getDeltas().forEach(_function);
          try {
            final List<String> mergedList = DiffUtils.<String>patch(Files.readAllLines(prevTempFile), prev_gen_diff);
            Files.write(file, mergedList);
          } catch (final Throwable _t) {
            if (_t instanceof PatchFailedException) {
              final PatchFailedException e = (PatchFailedException)_t;
              GenerationException.getInstance().addException(e);
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
      Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
      return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
      final Path targetDir = this.targetPath.resolve(this.temporalPath.relativize(dir));
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
    try {
      GenerationException.getInstance().getExcetpions().clear();
      final GroovyGenerator generator = new GroovyGenerator();
      final SettingManager settingManager = SettingManager.getInstance();
      final GenerateSetting setting = settingManager.getCurrentSetting();
      final HashMap<String, Object> map = new HashMap<String, Object>();
      final GeneratorUtils utils = new GeneratorUtils();
      final Path templatePath = Paths.get(setting.getTemplatePath());
      final Path targetPath = Paths.get(setting.getTargetPath());
      final Path temporalTargetRoot = Paths.get(settingManager.getM2tPluginFolderPath()).resolve("projects").resolve(
        utils.getAstahProjectName());
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
            GenerationException.getInstance().addException(e);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
      boolean _isUse3wayMerge = settingManager.isUse3wayMerge();
      boolean _not_1 = (!_isUse3wayMerge);
      if (_not_1) {
        CodeGenerator.DeleteDirVisitor _deleteDirVisitor = new CodeGenerator.DeleteDirVisitor();
        Files.walkFileTree(temporalTargetPath, _deleteDirVisitor);
      }
      boolean _exists_1 = Files.exists(temporalTargetPath, LinkOption.NOFOLLOW_LINKS);
      boolean _not_2 = (!_exists_1);
      if (_not_2) {
        try {
          Files.createDirectories(temporalTargetPath);
        } catch (final Throwable _t_1) {
          if (_t_1 instanceof Exception) {
            final Exception e_1 = (Exception)_t_1;
            GenerationException.getInstance().addException(e_1);
          } else {
            throw Exceptions.sneakyThrow(_t_1);
          }
        }
      }
      List<IClass> _classes = utils.getClasses();
      for (final IClass iClass : _classes) {
        {
          utils.setIclass(iClass);
          utils.setStatemachine(utils.getStatemachines().get(iClass));
          map.put("u", utils);
          int _size = ((List<String>)Conversions.doWrapArray(iClass.getStereotypes())).size();
          boolean _equals = (_size == 0);
          if (_equals) {
            final Function1<TemplateMap, Boolean> _function = new Function1<TemplateMap, Boolean>() {
              @Override
              public Boolean apply(final TemplateMap it) {
                TemplateType _templateType = it.getTemplateType();
                return Boolean.valueOf(Objects.equal(_templateType, TemplateType.Default));
              }
            };
            Iterable<TemplateMap> _filter = IterableExtensions.<TemplateMap>filter(setting.getMapping(), _function);
            for (final TemplateMap mapping : _filter) {
              try {
                boolean _contains = mapping.getFileExtension().contains("#");
                boolean _not_3 = (!_contains);
                if (_not_3) {
                  String _name = iClass.getName();
                  String _plus = (_name + ".");
                  String _fileExtension = mapping.getFileExtension();
                  String _plus_1 = (_plus + _fileExtension);
                  generator.doGenerate(map, 
                    temporalTargetPath.resolve(_plus_1), 
                    templatePath.resolve(mapping.getTemplateFile()));
                } else {
                  generator.doGenerate(map, 
                    temporalTargetPath.resolve(mapping.getFileExtension().replace("#", iClass.getName())), 
                    templatePath.resolve(mapping.getTemplateFile()));
                }
              } catch (final Throwable _t_2) {
                if (_t_2 instanceof Exception) {
                  final Exception e_2 = (Exception)_t_2;
                  GenerationException.getInstance().addException(e_2);
                } else {
                  throw Exceptions.sneakyThrow(_t_2);
                }
              }
            }
          } else {
            String[] _stereotypes = iClass.getStereotypes();
            for (final String stereotype : _stereotypes) {
              final Function1<TemplateMap, Boolean> _function_1 = new Function1<TemplateMap, Boolean>() {
                @Override
                public Boolean apply(final TemplateMap it) {
                  return Boolean.valueOf((Objects.equal(it.getTemplateType(), TemplateType.Stereotype) && it.getStereotype().equals(stereotype)));
                }
              };
              Iterable<TemplateMap> _filter_1 = IterableExtensions.<TemplateMap>filter(setting.getMapping(), _function_1);
              for (final TemplateMap mapping_1 : _filter_1) {
                try {
                  boolean _contains_1 = mapping_1.getFileExtension().contains("#");
                  boolean _not_4 = (!_contains_1);
                  if (_not_4) {
                    String _name_1 = iClass.getName();
                    String _plus_2 = (_name_1 + ".");
                    String _fileExtension_1 = mapping_1.getFileExtension();
                    String _plus_3 = (_plus_2 + _fileExtension_1);
                    generator.doGenerate(map, 
                      temporalTargetPath.resolve(_plus_3), 
                      templatePath.resolve(mapping_1.getTemplateFile()));
                  } else {
                    generator.doGenerate(map, 
                      temporalTargetPath.resolve(mapping_1.getFileExtension().replace("#", iClass.getName())), 
                      templatePath.resolve(mapping_1.getTemplateFile()));
                  }
                } catch (final Throwable _t_3) {
                  if (_t_3 instanceof Exception) {
                    final Exception e_3 = (Exception)_t_3;
                    GenerationException.getInstance().addException(e_3);
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
      final Function1<TemplateMap, Boolean> _function = new Function1<TemplateMap, Boolean>() {
        @Override
        public Boolean apply(final TemplateMap v) {
          TemplateType _templateType = v.getTemplateType();
          return Boolean.valueOf(Objects.equal(_templateType, TemplateType.Global));
        }
      };
      Iterable<TemplateMap> _filter = IterableExtensions.<TemplateMap>filter(setting.getMapping(), _function);
      for (final TemplateMap mapping : _filter) {
        try {
          generator.doGenerate(map, temporalTargetPath.resolve(mapping.getFileName()), 
            templatePath.resolve(mapping.getTemplateFile()));
        } catch (final Throwable _t_2) {
          if (_t_2 instanceof Exception) {
            final Exception e_2 = (Exception)_t_2;
            GenerationException.getInstance().addException(e_2);
          } else {
            throw Exceptions.sneakyThrow(_t_2);
          }
        }
      }
      boolean _isUse3wayMerge_1 = settingManager.isUse3wayMerge();
      if (_isUse3wayMerge_1) {
        try {
          CodeGenerator.ConflictCheckVisitor _conflictCheckVisitor = new CodeGenerator.ConflictCheckVisitor(targetPath, temporalTargetRoot, prevTemporalTargetPath);
          Files.walkFileTree(temporalTargetPath, _conflictCheckVisitor);
          CodeGenerator.DeleteDirVisitor _deleteDirVisitor_1 = new CodeGenerator.DeleteDirVisitor();
          Files.walkFileTree(prevTemporalTargetPath, _deleteDirVisitor_1);
          Files.deleteIfExists(prevTemporalTargetPath);
          Files.move(temporalTargetPath, prevTemporalTargetPath);
        } catch (final Throwable _t_3) {
          if (_t_3 instanceof Exception) {
            final Exception e_3 = (Exception)_t_3;
            GenerationException.getInstance().addException(e_3);
          } else {
            throw Exceptions.sneakyThrow(_t_3);
          }
        }
      } else {
        Files.move(temporalTargetPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
      }
      int _size = GenerationException.getInstance().getExcetpions().size();
      boolean _notEquals = (_size != 0);
      if (_notEquals) {
        throw GenerationException.getInstance();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
