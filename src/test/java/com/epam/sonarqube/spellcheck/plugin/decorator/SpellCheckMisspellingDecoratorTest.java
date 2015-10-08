package com.epam.sonarqube.spellcheck.plugin.decorator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;

import com.epam.sonarqube.spellcheck.plugin.decorator.SpellCheckMisspellingDecorator;

@RunWith(MockitoJUnitRunner.class)
public class SpellCheckMisspellingDecoratorTest {
    private FileSystem fileSystem = createFileSystem();
    
    @InjectMocks
    private SpellCheckMisspellingDecorator spellCheckMisspellingDecorator;

    @Mock
    private DecoratorContext context;
    
    @Mock
    private Resource resource;
    
    @Mock
    private Project project;
    
    @Test
    public void shouldNotSaveMeasureWhenProcessFilesDuringDecorating() throws Exception {
        when(resource.getScope()).thenReturn(Scopes.FILE);
        
        spellCheckMisspellingDecorator.decorate(resource, context);
        verify(resource, atLeastOnce()).getScope();
        verify(context, times(0)).saveMeasure(any(Metric.class), any(Double.class));
    }
    
    @Test
    public void shouldSaveMeasureWhenProcessDirectoryDuringDecorating() throws Exception {
        when(resource.getScope()).thenReturn(Scopes.DIRECTORY);
        
        spellCheckMisspellingDecorator.decorate(resource, context);
        verify(resource, atLeastOnce()).getScope();
        verify(context, atLeastOnce()).saveMeasure(any(Metric.class), any(Double.class));
    }
    
    @Test
    public void shouldSaveMeasureWhenProcessProjectDuringDecorating() throws Exception {
        when(resource.getScope()).thenReturn(Scopes.PROJECT);
        
        spellCheckMisspellingDecorator.decorate(resource, context);
        verify(resource, atLeastOnce()).getScope();
        verify(context, atLeastOnce()).saveMeasure(any(Metric.class), any(Double.class));
    }
    
    @Test
    public void shouldCallHasFilesWhileDecidingWhichFilesToDecorate() {
        when(fileSystem.hasFiles(any(FilePredicate.class))).thenReturn(true);

        spellCheckMisspellingDecorator.shouldExecuteOnProject(project);

        verify(fileSystem, atLeastOnce()).hasFiles(any(FilePredicate.class));
    }
    
    @Test
    public void shouldReturnCorrectToStringOfSpellCheckIssuesSensorClass() throws Exception {
        String expectedToStringValue = "SpellCheckMisspellingDecorator";

        assertEquals(expectedToStringValue, spellCheckMisspellingDecorator.toString());
    }
    
    private DefaultFileSystem createFileSystem() {
        File sourceFile = new File("src/main/java/com/epam/sonarqube/spellcheck/plugin");
        return spy(new DefaultFileSystem(sourceFile));
    }
    
}
