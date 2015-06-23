package jp.swest.ledcamp.setting;

@SuppressWarnings("all")
public interface Consumer<T extends Object> {
  public abstract void accespt(final T a);
}
