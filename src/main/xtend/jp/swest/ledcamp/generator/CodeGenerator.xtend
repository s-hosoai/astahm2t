package jp.swest.ledcamp.generator

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import java.util.HashMap
import jp.swest.ledcamp.exception.GenerationException
import jp.swest.ledcamp.setting.SettingManager
import jp.swest.ledcamp.setting.TemplateType
import org.zeroturnaround.zip.ZipUtil

class CodeGenerator {
    static val PREV_GENDIR = "prevGen"
    static val TEMP_GENDIR = "gen"
    static val uploadURL = "http://mdd-compile.shinshu-u.ac.jp/upload"

    static def generate() throws GenerationException {

        // code generate
        GenerationException::instance.excetpions.clear

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
            try {
                Files.createDirectories(prevTemporalTargetPath)
            } catch (Exception e) {
                GenerationException::instance.addException(e)
            }
        }

        // generate to temporal folder
        if (!Files.exists(temporalTargetPath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(temporalTargetPath)
            } catch (Exception e) {
                GenerationException::instance.addException(e)
            }
        }
        for (iClass : utils.classes) {
            utils.iclass = iClass
            utils.statemachine = utils.statemachines.get(iClass)
            map.put("u", utils)
            if (iClass.stereotypes.size == 0) { // Defualt generate
                for (mapping : setting.mapping.filter[it.templateType == TemplateType::Default]) {
                    try {
                        if (!mapping.fileExtension.contains("#")) {
                            generator.doGenerate(map,
                                temporalTargetPath.resolve(iClass.name + "." + mapping.fileExtension),
                                templatePath.resolve(mapping.templateFile))
                        } else {
                            generator.doGenerate(map,
                                temporalTargetPath.resolve(mapping.fileExtension.replace("#", iClass.name)),
                                templatePath.resolve(mapping.templateFile))
                        }
                    } catch (Exception e) {
                        GenerationException::instance.addException(e)
                    }
                }
            } else { // stereotype Generate
                for (stereotype : iClass.stereotypes) {
                    for (mapping : setting.mapping.filter [
                        it.templateType == TemplateType::Stereotype && it.stereotype.equals(stereotype)
                    ]) {
                        try {
                            if (!mapping.fileExtension.contains("#")) {
                                generator.doGenerate(map,
                                    temporalTargetPath.resolve(iClass.name + "." + mapping.fileExtension),
                                    templatePath.resolve(mapping.templateFile))
                            } else {
                                generator.doGenerate(map,
                                    temporalTargetPath.resolve(mapping.fileExtension.replace("#", iClass.name)),
                                    templatePath.resolve(mapping.templateFile))
                            }
                        } catch (Exception e) {
                            GenerationException::instance.addException(e)
                        }
                    }
                }
            }
        }

        map.put("u", utils)
        for (mapping : setting.mapping.filter[v|v.templateType == TemplateType::Global]) {
            try {
                generator.doGenerate(map, temporalTargetPath.resolve(mapping.fileName),
                    templatePath.resolve(mapping.templateFile))
            } catch (Exception e) {
                GenerationException::instance.addException(e)
            }
        }

        // 3way merge and check conflict
        try {
            Files.walkFileTree(temporalTargetPath,
                new ConflictCheckVisitor(targetPath, temporalTargetRoot, prevTemporalTargetPath))
            Files.walkFileTree(prevTemporalTargetPath, new DeleteDirVisitor);
            Thread.sleep(200); // wait for file system
            Files.deleteIfExists(prevTemporalTargetPath)
            Files.move(temporalTargetPath, prevTemporalTargetPath)
        } catch (Exception e) {
            GenerationException::instance.addException(e)
        }

        if (GenerationException::instance.excetpions.size != 0) {
            throw GenerationException::instance
        }
    }

    def static transferToCompilerServer() {
        val setting = SettingManager.instance.currentSetting
        val targetPath = Paths.get(setting.targetPath)
        val uploadURL = "http://mdd-compile.shinshu-u.ac.jp/upload"
        val tempPath = Files.createTempDirectory("astahm2t")
        val zipPath = tempPath.resolve("temp.zip")
        ZipUtil.pack(targetPath.toFile, zipPath.toFile)
        val dlURL = FileUploader.fileUpload(uploadURL, zipPath.toFile)
        Thread.sleep(3000)
        FileUploader.fileDownload(dlURL, Paths.get(setting.targetPath))
        Files.delete(zipPath)
        Files.delete(tempPath)
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
            val targetFile = targetPath.resolve(temporalPath.resolve(TEMP_GENDIR).relativize(file))
            val prevTempFile = prevTempPath.resolve(temporalPath.resolve(TEMP_GENDIR).relativize(file))
//            if (Files.exists(targetFile) && Files.exists(prevTempFile)) {
//                val prev_target_diff = DiffUtils.diff(Files.readAllLines(prevTempFile), Files.readAllLines(targetFile))
//                if (prev_target_diff.deltas.length > 0) {
//                    val prev_gen_diff = DiffUtils.diff(Files.readAllLines(prevTempFile), Files.readAllLines(file))
//                    prev_target_diff.deltas.forEach [
//                        prev_gen_diff.addDelta(it)
//                    ]
//                    try {
//                        val mergedList = DiffUtils.patch(Files.readAllLines(prevTempFile), prev_gen_diff)
//                        Files.write(file, mergedList)
//                    } catch (PatchFailedException e) {
//                        GenerationException::instance.addException(e)
//                    }
//                }
//            }
            if (!Files.exists(targetFile.parent)) {
                Files.createDirectories(targetFile.parent)
            }
            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING)
            return FileVisitResult.CONTINUE
        }

        override preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }
            return FileVisitResult.CONTINUE
        }
    }
}
