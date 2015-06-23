package jp.swest.ledcamp.setting

import java.io.File
import java.util.HashMap
import java.util.Map
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.XmlTransient
import org.eclipse.xtend.lib.annotations.Accessors
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.util.zip.ZipFile
import java.io.IOException

class SettingManager extends HashMap<String, GenerateSetting> {
    private static SettingManager instance;
    @XmlTransient @Accessors private static String settingFilePath
    @Accessors private GenerateSetting currentSetting

    private new() {
        super()
    }

    static def getInstance() {
        if (instance == null) {
            instance = new SettingManager
        }
        return instance
    }
    
    def save() {
        JAXB.marshal(instance, new File(settingFilePath))
    }

    def load() {
        // load property file
        val userFolder = System.getProperty("user.home")
        val m2tPluginFolderPath = userFolder + "/.astah/plugins/m2t/"
        settingFilePath = m2tPluginFolderPath + "m2tsetting.xml"
        val asgenPluginFolder = new File(m2tPluginFolderPath);
        if (!asgenPluginFolder.exists()) {  // install setting files.
            try {
                asgenPluginFolder.getParentFile().mkdirs();
                val zipFile = File.createTempFile("astahm2t", "templeatezip");
                var BufferedInputStream bis = null;
                var BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("m2t.zip"));
                    bos = new BufferedOutputStream(new FileOutputStream(zipFile));
                    writeFile(bis, bos);
                } finally {
                    bis.close();
                    bos.close();
                }
                unzip(new ZipFile(zipFile), asgenPluginFolder.getParent());
            } catch (IOException ioe) {
                System.err.println("cannot create template folders.");
            }
        }
        try {
            var settings = JAXB.unmarshal(new File(settingFilePath), SettingManager)
            instance.clear
            instance.putAll(settings)
        } catch (Exception e) {
            e.printStackTrace
            // load fail. 
        }
    }

    def getMap() {
        return instance as Map<String, GenerateSetting>
    }

    def setMap(Map<String, GenerateSetting> settings) {
        putAll(settings)
    }

    private def unzip(ZipFile zipFile, String path) throws IOException{
        val entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement();
            val extractFile = new File(path, entry.getName());
            if (entry.isDirectory()) {
                extractFile.mkdirs();
            } else {
                var BufferedInputStream bis = null
                var BufferedOutputStream bos = null
                try {
                    bis = new BufferedInputStream(zipFile.getInputStream(entry))
                    bos = new BufferedOutputStream(new FileOutputStream(extractFile))
                    writeFile(bis, bos)
                } finally {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                }
            }
        }
    }

    private def writeFile(BufferedInputStream bis, BufferedOutputStream bos) throws IOException{
        var available = 0;
        while ((available = bis.available()) > 0) {
            val byte[] bs = newByteArrayOfSize(available)
            bis.read(bs);
            bos.write(bs);
        }
    }

/*
    private def testSetting(){
        var setting = new GenerateSetting
        setting.templateID = "test"
        setting.templateEngine = "groovy"
        setting.templatePath = "C:/Users/hosoai/git/astahm2t-github/astahm2t/template/"
        setting.targetPath = "C:/Users/hosoai/git/astahm2t-github/astahm2t/out/"
        setting.mapping.put("cpp", new TemplateMap("cpp", GenerateType.Default, "cpp.template", null, "cpp"))
        setting.mapping.put("header", new TemplateMap("header", GenerateType.Default, "header.template", null, "h"))
        setting.mapping.put("ino", new TemplateMap("ino", GenerateType.Global, "arduino.template", null, "ino"))
        put("test", setting)
    }*/
}
