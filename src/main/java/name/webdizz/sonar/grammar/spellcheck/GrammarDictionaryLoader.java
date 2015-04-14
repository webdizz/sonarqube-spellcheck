package name.webdizz.sonar.grammar.spellcheck;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import name.webdizz.sonar.grammar.GrammarPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GrammarDictionaryLoader
{

  private static final Logger LOGGER = LoggerFactory.getLogger( GrammarDictionaryLoader.class );

  private final Lock locker = new ReentrantLock();

  private Settings settings;

  private AtomicReference<SpellDictionary> dictionary = new AtomicReference<SpellDictionary>();

  public GrammarDictionaryLoader( final Settings settings )
  {
    this.settings = settings;
  }

  public SpellDictionary load()
  {
    String dictionaryPath = settings.getString( GrammarPlugin.DICTIONARY );
    SpellDictionary spellDictionary = dictionary.get();
    try
    {
      locker.lock();
      if( null == spellDictionary )
      {
        if( isDictionaryExists( dictionaryPath ) )
          spellDictionary = loadSpellDictionary( dictionaryPath );
        else
        {
          dictionaryPath = "/" + GrammarChecker.DEFAULT_DICT_PATH;
          spellDictionary = loadSpellDictionary( dictionaryPath );
        }
      }
      return spellDictionary;
    }
    catch( FileNotFoundException e )
    {
      throw new UnableToLoadDictionary( "There is no file with dictionary.", e );
    }
    finally
    {
      locker.unlock();
    }
  }

  private boolean isDictionaryExists( String dictionaryPath )
  {
    return !Strings.isNullOrEmpty( dictionaryPath ) && new File( dictionaryPath ).exists();
  }

  private SpellDictionary loadSpellDictionary( String dictionaryPath ) throws FileNotFoundException
  {
    BufferedReader dictionaryReader = Files.newReader( new File( dictionaryPath ), Charsets.UTF_8 );
    SpellDictionary spellDictionary = getSpellDictionary( dictionaryReader, dictionaryPath );
    return spellDictionary;
  }

  private SpellDictionary getSpellDictionary( final Reader dictionaryReader, final String dictionaryPath )
  {
    try
    {
      dictionary.set( new SpellDictionaryHashMap( dictionaryReader ) );
      LOGGER.info( "Dictionary was loaded." );
    }
    catch( IOException e )
    {
      throw new UnableToLoadDictionary( String.format( "Unable to load dictionary from %s", dictionaryPath ), e );
    }
    return dictionary.get();
  }

  static class UnableToLoadDictionary extends RuntimeException
  {

    UnableToLoadDictionary( final String message, final Throwable cause )
    {
      super( message, cause );
    }
  }
}
