package name.webdizz.sonar.grammar.sensor;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import name.webdizz.sonar.grammar.PluginParameter;
import name.webdizz.sonar.grammar.spellcheck.GrammarChecker;
import name.webdizz.sonar.grammar.spellcheck.GrammarDictionaryLoader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The sensor for project files
 */
public class GrammarIssuesSensor implements Sensor
{

  private static final Logger LOGGER = LoggerFactory.getLogger( GrammarIssuesSensor.class );

  private final FileSystem fileSystem;
  private final ResourcePerspectives perspectives;
  private final GrammarChecker grammarChecker;
  private final Lock wrapperLock = new ReentrantLock();
  private GrammarIssuesWrapper templateWrapper = null;

  /**
   * Use of IoC to get FileSystem
   *
   * @param fileSystem
   * @param perspectives
   * @param settings
   */
  public GrammarIssuesSensor( FileSystem fileSystem, ResourcePerspectives perspectives, final Settings settings )
  {
    this.fileSystem = fileSystem;
    this.perspectives = perspectives;
    this.grammarChecker = new GrammarChecker( new GrammarDictionaryLoader( settings ) );
    if( LOGGER.isDebugEnabled() )
      LOGGER.debug( "Created the bean of grammar sensor." );
  }

  @Override
  public String toString()
  {
    return "Grammar Issues";
  }

  @Override
  public void analyse( Project module, SensorContext context )
  {
    if( LOGGER.isDebugEnabled() )
      logWhenDoAnalyse( module, context );

    grammarChecker.initialize();

    if( LOGGER.isDebugEnabled() )
      LOGGER.debug( "Checking {}-files.", PluginParameter.PROFILE_LANGUAGE );

    for( final InputFile inputFile : fileSystem.inputFiles( fileSystem.predicates().hasLanguage( PluginParameter.PROFILE_LANGUAGE ) ) )
      processInputFile( inputFile );
  }

  private void logWhenDoAnalyse( Project module, SensorContext context )
  {
    Object[] arguments = new Object[] { module.getName(), module.getKey(), module.getDescription() };
    LOGGER.debug( "Module name={} key={} description=\"{}\"", arguments );
    LOGGER.debug( "SensorContext {}", context.toString() );
    LOGGER.debug( "Initialize the GrammarChecker." );
  }

  @Override
  public boolean shouldExecuteOnProject( Project project )
  {
    // This sensor is executed only when there are Java files
    return fileSystem.hasFiles( fileSystem.predicates().hasLanguage( PluginParameter.PROFILE_LANGUAGE ) );
  }

  private void processInputFile( final InputFile inputFile )
  {
    int lineNumber = 1;
    try
    {
      final List<String> readLines = Files.readLines( inputFile.file(), Charsets.UTF_8 );
      for( final String line : readLines )
      {
        if( LOGGER.isDebugEnabled() )
          logWhenDoInputFile( inputFile, lineNumber, readLines, line );
        processInputCurrentLine( line, inputFile, lineNumber );
        lineNumber++;
      }
    }
    catch( IOException ex )
    {
      LOGGER.error( "Can't read data from file " + inputFile.absolutePath(), ex );
    }
  }

  private void logWhenDoInputFile( InputFile inputFile, int lineNumber, List<String> readLines, String line )
  {
    LOGGER.debug( "Processing input-file {}", inputFile.toString() );
    LOGGER.debug( "Has read code-lines from {}\nHave read {} lines.", inputFile.toString(), readLines.size() );
    LOGGER.debug( "Processing {}:\"{}\"", lineNumber, line );
  }

  private void processInputCurrentLine( final String line, final InputFile inputFile, int lineNumber )
  {
    if( !StringUtils.isEmpty( line ) )
    {
      final GrammarIssuesWrapper lineWrapper = createWrapper( inputFile, line, lineNumber );
      if( LOGGER.isDebugEnabled() )
        logWhenDoInputLine( line, inputFile, lineNumber, lineWrapper );

      grammarChecker.checkSpelling( line, new GrammarViolationTrigger( lineWrapper ) );

      if( LOGGER.isDebugEnabled() )
        LOGGER.debug( "End check line \"{}\"", line );
    }
    else
    {
      if( LOGGER.isDebugEnabled() )
        LOGGER.debug( "Skipped empty line #{} in {}", lineNumber, inputFile.toString() );
    }
  }

  private void logWhenDoInputLine( String line, InputFile inputFile, int lineNumber, GrammarIssuesWrapper lineWrapper )
  {
    final Object[] arguments = new Object[] { lineNumber, line, inputFile.toString() };
    LOGGER.debug( "Prepared issues-wrapper for \n {}:\"{}\"\nin{}", arguments );
    LOGGER.debug( lineWrapper.toString() );
    LOGGER.debug( "Begin check line \"{}\"", line );
  }

  private GrammarIssuesWrapper createWrapper( final InputFile resource, final String line, final int lineNumber )
  {
    wrapperLock.lock();
    try
    {
      return (templateWrapper == null)
              ? templateWrapper = buildGrammarIssuesWrapperIfNull( resource, line, lineNumber )
              : buildGrammarIssuesWrapper( resource, line, lineNumber );
    }
    finally
    {
      wrapperLock.unlock();
    }
  }

  private GrammarIssuesWrapper buildGrammarIssuesWrapperIfNull( InputFile resource, String line, int lineNumber )
  {
    return GrammarIssuesWrapper.builder()
    .setInputFile( resource )
    .setLine( line )
    .setLineNumber( lineNumber )
    .setPerspectives( perspectives )
    .setRuleKey( RuleKey.of( PluginParameter.REPOSITORY_KEY, PluginParameter.SONAR_GRAMMAR_RULE_KEY ) )
    .build();
  }

  private GrammarIssuesWrapper buildGrammarIssuesWrapper( InputFile resource, String line, int lineNumber )
  {
    return GrammarIssuesWrapper.builder( templateWrapper )
            .setInputFile(resource)
            .setLine(line)
            .setLineNumber(lineNumber)
            .build();
  }
}
