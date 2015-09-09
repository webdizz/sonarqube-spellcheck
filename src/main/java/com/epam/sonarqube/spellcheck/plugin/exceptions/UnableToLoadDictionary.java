package com.epam.sonarqube.spellcheck.plugin.exceptions;

public class UnableToLoadDictionary extends RuntimeException{

  public UnableToLoadDictionary( final String message, final Throwable cause )  {
    super( message, cause );
  }
}
