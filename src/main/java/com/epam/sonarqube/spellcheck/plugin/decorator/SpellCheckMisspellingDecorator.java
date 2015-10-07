package com.epam.sonarqube.spellcheck.plugin.decorator;

import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.MeasureUtils;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.ResourceUtils;

import com.epam.sonarqube.spellcheck.plugin.PluginParameter;
import com.epam.sonarqube.spellcheck.plugin.metric.SpellCheckMetrics;

public class SpellCheckMisspellingDecorator implements Decorator {

    private final FileSystem fileSystem;
    
    public SpellCheckMisspellingDecorator(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        // This decorator is executed only when there are Java files
        return fileSystem.hasFiles(fileSystem.predicates().hasLanguage(PluginParameter.PROFILE_LANGUAGE));
    }

    @Override
    public void decorate(Resource resource, DecoratorContext context) {
        // This method is executed on the whole tree of resources.
        // Bottom-up navigation : Files -> Dirs -> modules -> project
        if (!ResourceUtils.isFile(resource)) {
            // we sum MISSPELLING metric values on resources different than file.
            context.saveMeasure(
                    SpellCheckMetrics.MISSPELLING,
                    MeasureUtils.sum(true, context.getChildrenMeasures(SpellCheckMetrics.MISSPELLING)));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}