package com.epam.sonarqube.grammar.plugin.exceptions;

public class UnableToLoadDictionary extends RuntimeException{

  public UnableToLoadDictionary( final String message, final Throwable cause )  {
    super( message, cause );
  }
}
