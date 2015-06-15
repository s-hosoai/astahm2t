package jp.swest.ledcamp.generator

import java.util.Map

interface ITemplateEngine {
    def void doGenerate(Map<String, Object> map, String output, String templateFile)
}