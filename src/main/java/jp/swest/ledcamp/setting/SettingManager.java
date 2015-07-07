package jp.swest.ledcamp.setting;

import com.google.common.base.Objects;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlTransient;
import jp.swest.ledcamp.setting.GenerateSetting;
import jp.swest.ledcamp.setting.TemplateEngine;
import jp.swest.ledcamp.setting.TemplateMap;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class SettingManager extends HashMap<String, GenerateSetting> {
  private static SettingManager instance;
  
  @XmlTransient
  @Accessors
  private String settingFilePath;
  
  @Accessors
  private GenerateSetting currentSetting;
  
  @XmlTransient
  private String userFolder = System.getProperty("user.home");
  
  @XmlTransient
  @Accessors
  private String m2tPluginFolderPath = (this.userFolder + "/.astah/plugins/m2t/");
  
  @XmlTransient
  @Accessors
  private String currentAstahFileName;
  
  private SettingManager() {
    super();
    this.settingFilePath = (this.m2tPluginFolderPath + "m2tsetting.xml");
  }
  
  public static SettingManager getInstance() {
    boolean _equals = Objects.equal(SettingManager.instance, null);
    if (_equals) {
      SettingManager _settingManager = new SettingManager();
      SettingManager.instance = _settingManager;
      SettingManager.instance.load();
    }
    return SettingManager.instance;
  }
  
  public void save() {
    File _file = new File(this.settingFilePath);
    JAXB.marshal(SettingManager.instance, _file);
  }
  
  public GenerateSetting load() {
    GenerateSetting _xblockexpression = null;
    {
      String _property = System.getProperty("user.home");
      this.userFolder = _property;
      this.m2tPluginFolderPath = (this.userFolder + "/.astah/plugins/m2t/");
      this.settingFilePath = (this.m2tPluginFolderPath + "m2tsetting.xml");
      final File asgenPluginFolder = new File(this.m2tPluginFolderPath);
      boolean _exists = asgenPluginFolder.exists();
      boolean _not = (!_exists);
      if (_not) {
        try {
          File _parentFile = asgenPluginFolder.getParentFile();
          _parentFile.mkdirs();
          final File zipFile = File.createTempFile("astahm2t", "templeatezip");
          BufferedInputStream bis = null;
          BufferedOutputStream bos = null;
          try {
            Class<? extends SettingManager> _class = this.getClass();
            ClassLoader _classLoader = _class.getClassLoader();
            InputStream _resourceAsStream = _classLoader.getResourceAsStream("m2t.zip");
            BufferedInputStream _bufferedInputStream = new BufferedInputStream(_resourceAsStream);
            bis = _bufferedInputStream;
            FileOutputStream _fileOutputStream = new FileOutputStream(zipFile);
            BufferedOutputStream _bufferedOutputStream = new BufferedOutputStream(_fileOutputStream);
            bos = _bufferedOutputStream;
            this.writeFile(bis, bos);
          } finally {
            bis.close();
            bos.close();
          }
          ZipFile _zipFile = new ZipFile(zipFile);
          String _parent = asgenPluginFolder.getParent();
          this.unzip(_zipFile, _parent);
        } catch (final Throwable _t) {
          if (_t instanceof IOException) {
            final IOException ioe = (IOException)_t;
            System.err.println("cannot create template folders.");
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
      GenerateSetting _xtrycatchfinallyexpression = null;
      try {
        GenerateSetting _xblockexpression_1 = null;
        {
          final File settingFile = new File(this.settingFilePath);
          GenerateSetting _xifexpression = null;
          boolean _exists_1 = settingFile.exists();
          if (_exists_1) {
            GenerateSetting _xblockexpression_2 = null;
            {
              File _file = new File(this.settingFilePath);
              final SettingManager settings = JAXB.<SettingManager>unmarshal(_file, SettingManager.class);
              SettingManager.instance.clear();
              SettingManager.instance.putAll(settings);
              _xblockexpression_2 = SettingManager.instance.currentSetting = settings.currentSetting;
            }
            _xifexpression = _xblockexpression_2;
          } else {
            this.createDefaultSetting();
            this.save();
          }
          _xblockexpression_1 = _xifexpression;
        }
        _xtrycatchfinallyexpression = _xblockexpression_1;
      } catch (final Throwable _t_1) {
        if (_t_1 instanceof Exception) {
          final Exception e = (Exception)_t_1;
          e.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t_1);
        }
      }
      _xblockexpression = _xtrycatchfinallyexpression;
    }
    return _xblockexpression;
  }
  
  private GenerateSetting createDefaultSetting() {
    GenerateSetting _xblockexpression = null;
    {
      final GenerateSetting sampleGenerateSetting = new GenerateSetting();
      File _file = new File(this.userFolder);
      String _absolutePath = _file.getAbsolutePath();
      sampleGenerateSetting.setTargetPath(_absolutePath);
      File _file_1 = new File((this.m2tPluginFolderPath + "templates/"));
      String _absolutePath_1 = _file_1.getAbsolutePath();
      sampleGenerateSetting.setTemplatePath(_absolutePath_1);
      sampleGenerateSetting.setTemplateEngine(TemplateEngine.Groovy);
      HashSet<TemplateMap> _mapping = sampleGenerateSetting.getMapping();
      TemplateMap _newDefaultTemplateMap = TemplateMap.newDefaultTemplateMap("cpp.template", "cpp");
      _mapping.add(_newDefaultTemplateMap);
      HashSet<TemplateMap> _mapping_1 = sampleGenerateSetting.getMapping();
      TemplateMap _newDefaultTemplateMap_1 = TemplateMap.newDefaultTemplateMap("header.template", "h");
      _mapping_1.add(_newDefaultTemplateMap_1);
      HashSet<TemplateMap> _mapping_2 = sampleGenerateSetting.getMapping();
      TemplateMap _newGlobalTemplateMap = TemplateMap.newGlobalTemplateMap("arduino.template", "Sketch.cpp");
      _mapping_2.add(_newGlobalTemplateMap);
      SettingManager.instance.put("sample", sampleGenerateSetting);
      _xblockexpression = SettingManager.instance.currentSetting = sampleGenerateSetting;
    }
    return _xblockexpression;
  }
  
  public Map<String, GenerateSetting> getMap() {
    return ((Map<String, GenerateSetting>) SettingManager.instance);
  }
  
  public void setMap(final Map<String, GenerateSetting> settings) {
    this.putAll(settings);
  }
  
  private void unzip(final ZipFile zipFile, final String path) throws IOException {
    final Enumeration<? extends ZipEntry> entries = zipFile.entries();
    while (entries.hasMoreElements()) {
      {
        final ZipEntry entry = entries.nextElement();
        String _name = entry.getName();
        final File extractFile = new File(path, _name);
        boolean _isDirectory = entry.isDirectory();
        if (_isDirectory) {
          extractFile.mkdirs();
        } else {
          BufferedInputStream bis = null;
          BufferedOutputStream bos = null;
          try {
            InputStream _inputStream = zipFile.getInputStream(entry);
            BufferedInputStream _bufferedInputStream = new BufferedInputStream(_inputStream);
            bis = _bufferedInputStream;
            FileOutputStream _fileOutputStream = new FileOutputStream(extractFile);
            BufferedOutputStream _bufferedOutputStream = new BufferedOutputStream(_fileOutputStream);
            bos = _bufferedOutputStream;
            this.writeFile(bis, bos);
          } finally {
            boolean _notEquals = (!Objects.equal(bis, null));
            if (_notEquals) {
              bis.close();
            }
            boolean _notEquals_1 = (!Objects.equal(bos, null));
            if (_notEquals_1) {
              bos.close();
            }
          }
        }
      }
    }
  }
  
  private void writeFile(final BufferedInputStream bis, final BufferedOutputStream bos) throws IOException {
    int available = 0;
    while (((available = bis.available()) > 0)) {
      {
        final byte[] bs = new byte[available];
        bis.read(bs);
        bos.write(bs);
      }
    }
  }
  
  @Pure
  public String getSettingFilePath() {
    return this.settingFilePath;
  }
  
  public void setSettingFilePath(final String settingFilePath) {
    this.settingFilePath = settingFilePath;
  }
  
  @Pure
  public GenerateSetting getCurrentSetting() {
    return this.currentSetting;
  }
  
  public void setCurrentSetting(final GenerateSetting currentSetting) {
    this.currentSetting = currentSetting;
  }
  
  @Pure
  public String getM2tPluginFolderPath() {
    return this.m2tPluginFolderPath;
  }
  
  public void setM2tPluginFolderPath(final String m2tPluginFolderPath) {
    this.m2tPluginFolderPath = m2tPluginFolderPath;
  }
  
  @Pure
  public String getCurrentAstahFileName() {
    return this.currentAstahFileName;
  }
  
  public void setCurrentAstahFileName(final String currentAstahFileName) {
    this.currentAstahFileName = currentAstahFileName;
  }
}
