package name.webdizz.sonar.grammar.exceptions;

public class UnableToLoadDictionary extends RuntimeException
{

  public UnableToLoadDictionary( final String message, final Throwable cause )
  {
    super( message, cause );
  }
}
