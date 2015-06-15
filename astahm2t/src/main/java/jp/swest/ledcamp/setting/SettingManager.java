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
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlTransient;
import jp.swest.ledcamp.setting.GenerateSetting;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class SettingManager extends HashMap<String, GenerateSetting> {
  private static SettingManager instance;
  
  @XmlTransient
  @Accessors
  private static String settingFilePath;
  
  private SettingManager() {
    super();
  }
  
  public static SettingManager getInstance() {
    boolean _equals = Objects.equal(SettingManager.instance, null);
    if (_equals) {
      SettingManager _settingManager = new SettingManager();
      SettingManager.instance = _settingManager;
    }
    return SettingManager.instance;
  }
  
  public void save() {
    File _file = new File(SettingManager.settingFilePath);
    JAXB.marshal(SettingManager.instance, _file);
  }
  
  public void load() {
    final String userFolder = System.getProperty("user.home");
    final String m2tPluginFolderPath = (userFolder + "/.astah/plugins/m2t/");
    SettingManager.settingFilePath = (m2tPluginFolderPath + "m2tsetting.xml");
    final File asgenPluginFolder = new File(m2tPluginFolderPath);
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
    try {
      File _file = new File(SettingManager.settingFilePath);
      SettingManager settings = JAXB.<SettingManager>unmarshal(_file, SettingManager.class);
      SettingManager.instance.clear();
      SettingManager.instance.putAll(settings);
    } catch (final Throwable _t_1) {
      if (_t_1 instanceof Exception) {
        final Exception e = (Exception)_t_1;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t_1);
      }
    }
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
  public static String getSettingFilePath() {
    return SettingManager.settingFilePath;
  }
  
  public static void setSettingFilePath(final String settingFilePath) {
    SettingManager.settingFilePath = settingFilePath;
  }
}
