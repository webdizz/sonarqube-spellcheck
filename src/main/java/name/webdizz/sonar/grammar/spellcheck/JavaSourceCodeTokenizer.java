package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.WordFinder;

public class JavaSourceCodeTokenizer extends StringWordTokenizer {

    public JavaSourceCodeTokenizer(final String s, final WordFinder finder) {
        super(s, finder);
    }
}
