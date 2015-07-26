package jp.swest.ledcamp.exception

import java.lang.Exception
import java.util.ArrayList
import java.util.List

class GenerationException extends Exception{
	List<Exception> exceptions = new ArrayList
	static GenerationException instance;
	static def getInstance(){
		if(instance==null){
			instance = new GenerationException
		}
		return instance
	}
	def addException(Exception e){
		exceptions.add(e)
	}
	def getExcetpions(){
		return exceptions
	}
}