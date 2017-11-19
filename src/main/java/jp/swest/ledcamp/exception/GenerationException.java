package jp.swest.ledcamp.exception;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class GenerationException extends Exception {
  private List<Exception> exceptions = new ArrayList<Exception>();
  
  private static GenerationException instance;
  
  public static GenerationException getInstance() {
    if ((GenerationException.instance == null)) {
      GenerationException _generationException = new GenerationException();
      GenerationException.instance = _generationException;
    }
    return GenerationException.instance;
  }
  
  public boolean addException(final Exception e) {
    return this.exceptions.add(e);
  }
  
  public List<Exception> getExcetpions() {
    return this.exceptions;
  }
}
