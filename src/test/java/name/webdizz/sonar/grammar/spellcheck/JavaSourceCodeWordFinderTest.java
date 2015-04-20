package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.event.SpellChecker;
import name.webdizz.sonar.grammar.utils.SpellCheckerUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class JavaSourceCodeWordFinderTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger( JavaSourceCodeWordFinderTest.class );

  @Test
  public void shouldCheckCamelCaseNameAndReturnNoErrorTest() throws Exception
  {
    LOGGER.info( "Start shouldCheckCamelCaseNameAndReturnNoErrorTest " );
    String testLine = "myWrongCamelTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( -1, errorsSize );
    LOGGER.info( "Done" );
  }

  @Test
  public void shouldCheckCamelCaseNameAndReturnOneErrorTest() throws Exception
  {
    LOGGER.info( "Start shouldCheckCamelCaseNameAndReturnOneErrorTest " );
    String testLine = "myWrongCameeelTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( 1, errorsSize );
    LOGGER.info( "Done" );
  }

  @Test
  public void shouldCheckCamelCaseNameAndReturnThreeErrorTest() throws Exception
  {
    LOGGER.info( "Start shouldCheckCamelCaseNameAndReturnThreeErrorTest " );
    String testLine = "myyWrongCameeelNameeTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( 3, errorsSize );
    LOGGER.info( "Done" );
  }

  @Test
  public void shouldCheckMixedNameAndReturnNoErrorTest() throws Exception
  {
    LOGGER.info( "Start shouldCheckMixedNameAndReturnNoErrorTest " );
    String testLine = "myWrong111CameeelTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( -1, errorsSize );
    LOGGER.info( "Done" );
  }

  @Test
  public void shouldCheckMixedNameAndReturnTwoErrorTest() throws Exception
  {
    LOGGER.info( "Start shouldCheckMixedNameAndReturnTwoErrorTest " );
    String testLine = "myyWrong111CameeelTeest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( 2, errorsSize );
    LOGGER.info( "Done" );
  }

  @Test
  public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception
  {
    LOGGER.info( "Start shouldCheckMixedNameAndReturnTreeErrorTest " );
    String testLine = "myyWrong111NaameCameeelTeest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( 3, errorsSize );
    LOGGER.info( "Done" );
  }

  private int getErrorsSize( String testLine )
  {
    SpellChecker spellChecker = SpellCheckerUtil.getInstance();
    JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer( testLine, new JavaSourceCodeWordFinder() );
    return spellChecker.checkSpelling( sourceCodeTokenizer );
  }
}