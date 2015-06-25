package jp.swest.ledcamp.xtendhelper;

@SuppressWarnings("all")
public interface Consumer<T extends Object> {
  public abstract void accespt(final T a);
}
