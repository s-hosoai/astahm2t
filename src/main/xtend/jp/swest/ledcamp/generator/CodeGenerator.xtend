package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.exception.InvalidUsingException
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException
import java.awt.HeadlessException
import java.io.IOException
import java.util.HashMap
import javax.swing.JOptionPane
import jp.swest.ledcamp.setting.SettingManager
import jp.swest.ledcamp.setting.TemplateType
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.LinkOption
import java.nio.file.Path
import java.util.Map
import java.nio.file.CopyOption
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import java.nio.file.StandardCopyOption
import difflib.DiffUtils
import difflib.PatchFailedException

class CodeGenerator {
    static val PREV_GENDIR = "prevGen"
    static val TEMP_GENDIR = "gen"

    static def generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {

        // code generate
        val generator = new GroovyGenerator
        val settingManager = SettingManager.getInstance
        val setting = settingManager.currentSetting
        val map = new HashMap<String, Object>
        val utils = new GeneratorUtils
        val templatePath = Paths.get(setting.templatePath)
        val targetPath = Paths.get(setting.targetPath)
        val temporalTargetRoot = Paths.get(settingManager.m2tPluginFolderPath).resolve("projects").resolve(
            utils.astahProjectName)
        val temporalTargetPath = temporalTargetRoot.resolve(TEMP_GENDIR)
        val prevTemporalTargetPath = temporalTargetRoot.resolve(PREV_GENDIR)
        if (!Files.exists(prevTemporalTargetPath)) {
            Files.createDirectories(prevTemporalTargetPath)
        }

        // generate to temporal folder
        if (!Files.exists(temporalTargetPath, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(temporalTargetPath)
        }
        for (iClass : utils.classes) {
            utils.iclass = iClass
            utils.statemachine = utils.statemachines.get(iClass)
            map.put("u", utils)
            if (iClass.stereotypes.size == 0) { // Defualt generate
                for (mapping : setting.mapping.filter[it.templateType == TemplateType::Default]) {
                    generator.doGenerate(map, temporalTargetPath.resolve(iClass.name + "." + mapping.fileExtension),
                        templatePath.resolve(mapping.templateFile))
                }
            } else { // stereotype Generate
                for (stereotype : iClass.stereotypes) {
                    for (mapping : setting.mapping.filter[
                        it.templateType == TemplateType::Stereotype && it.stereotype.equals(stereotype)]) {
                        generator.doGenerate(map, temporalTargetPath.resolve(iClass.name + "." + mapping.fileExtension),
                            templatePath.resolve(mapping.templateFile))
                    }
                }
            }
        }
        map.put("u", utils)
        for (mapping : setting.mapping.filter[v|v.templateType == TemplateType::Global]) {
            generator.doGenerate(map, temporalTargetPath.resolve(mapping.fileName),
                templatePath.resolve(mapping.templateFile))
        }

        // check conflict
        Files.walkFileTree(temporalTargetPath,
            new ConflictCheckVisitor(targetPath, temporalTargetRoot, prevTemporalTargetPath))

        Files.walkFileTree(prevTemporalTargetPath, new DeleteDirVisitor);
        Files.move(temporalTargetPath, prevTemporalTargetPath)
        JOptionPane.showMessageDialog(utils.frame, "Generate Finish")
    }

    static class DeleteDirVisitor extends SimpleFileVisitor<Path> {
        override visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file)
            return FileVisitResult.CONTINUE
        }

        override postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (exc == null) {
                Files.delete(dir)
                return FileVisitResult.CONTINUE
            }
            throw exc
        }
    }

    static class ConflictCheckVisitor extends SimpleFileVisitor<Path> {
        Path targetPath
        Path temporalPath
        Path prevTempPath

        new(Path targetPath, Path temporalPath, Path prevTemp) {
            this.targetPath = targetPath
            this.temporalPath = temporalPath
            this.prevTempPath = prevTemp
        }

        override visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            val targetFile = targetPath.resolve(temporalPath.relativize(file))
            var hasConflict = false
            var Path mergedFile = null
            if (Files.exists(targetFile)) {
                println(file.fileName + " already Exist")
                println(file.fileName + " not same")
                val prevTempFile = prevTempPath.resolve(temporalPath.relativize(file))
                val prev_target_diff = DiffUtils.diff(Files.readAllLines(prevTempFile), Files.readAllLines(targetFile))
                if (prev_target_diff.deltas.length > 0) {
                    // hasConflict!
                    println(file.fileName + " has Conflict! : " + prev_target_diff.deltas.get(0))
                    val prev_gen_diff = DiffUtils.diff(Files.readAllLines(prevTempFile), Files.readAllLines(file))
                    prev_target_diff.deltas.forEach[
                        prev_gen_diff.addDelta(it)
                    ]
                    try{
                        val mergedList = DiffUtils.patch(Files.readAllLines(prevTempFile), prev_gen_diff)
                        Files.write(file, mergedList)
                    }catch(PatchFailedException e){
                          System.err.println(file + " has Conflict!"+e.message)
//                        addConflict
                    }
                }
            }
            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING)
            return FileVisitResult.CONTINUE
        }

        override preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            val targetDir = targetPath.resolve(temporalPath.relativize(dir))
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir)
            }
            return FileVisitResult.CONTINUE
        }
    }

    def static void main(String[] args) {
        val tempRoot = Paths.get("C:/Users/hosoai/.astah/plugins/m2t/projects/JavaSample/gen/")
        val prevTempRoot = Paths.get("C:/Users/hosoai/.astah/plugins/m2t/projects/JavaSample/prevGen/")
        val targetPath = Paths.get("C:/Users/hosoai/.astah/plugins/m2t/target/JavaSample/")
        Files.walkFileTree(tempRoot, new ConflictCheckVisitor(targetPath, tempRoot, prevTempRoot))
        Files.walkFileTree(prevTempRoot, new DeleteDirVisitor);
        Files.move(tempRoot, prevTempRoot)
    }
}
