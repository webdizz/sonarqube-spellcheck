package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.event.SpellChecker;
import name.webdizz.sonar.grammar.utils.SpellCheckerUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaSourceCodeWordFinderTest
{
  @Test
  public void shouldCheckCamelCaseNameAndReturnMinusOneThatMeansNoErrorTest() throws Exception
  {
    String testLine = "myWrongCamelTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals(-1, errorsSize);
  }

  @Test
  public void shouldCheckCamelCaseNameAndReturnOneErrorTest() throws Exception
  {
    String testLine = "myWrongCameeelTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals(1, errorsSize);
  }

  @Test
  public void shouldCheckCamelCaseNameAndReturnThreeErrorTest() throws Exception
  {
    String testLine = "myyWrongCameeelNameeTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals(3, errorsSize);
  }

  @Test
  public void shouldCheckMixedNameAndReturnMinusOneThatMeansNoErrorTest() throws Exception
  {
    String testLine = "myWrong111CameeelTest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( -1, errorsSize );
  }

  @Test
  public void shouldCheckMixedNameAndReturnTwoErrorTest() throws Exception
  {
    String testLine = "myyWrong111CameeelTeest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( 2, errorsSize );
  }

  @Test
  public void shouldCheckMixedNameAndReturnTreeErrorTest() throws Exception
  {
    String testLine = "myyWrong111NaameCameeelTeest";
    int errorsSize = getErrorsSize( testLine );
    assertEquals( 3, errorsSize );
  }

  private int getErrorsSize( String testLine )
  {
    SpellChecker spellChecker = SpellCheckerUtil.getInstance();
    JavaSourceCodeTokenizer sourceCodeTokenizer = new JavaSourceCodeTokenizer( testLine, new JavaSourceCodeWordFinder() );
    return spellChecker.checkSpelling( sourceCodeTokenizer );
  }
}