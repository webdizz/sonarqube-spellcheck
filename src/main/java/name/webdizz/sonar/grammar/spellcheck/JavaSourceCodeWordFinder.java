package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.event.AbstractWordFinder;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordNotFoundException;
import name.webdizz.sonar.grammar.GrammarPlugin;
import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;

public class JavaSourceCodeWordFinder extends AbstractWordFinder implements BatchExtension {
    private boolean inComment;
    private int minimumWordLength;
    private Settings settings;

    public JavaSourceCodeWordFinder() {
        super();
    }

    public JavaSourceCodeWordFinder(final String searchText, final Settings settings) {
        super(searchText);
        this.settings=settings;
    }

    /**
     * This method scans the text from the end of the last word,  and returns a
     * new Word object corresponding to the next word.
     *
     * @return the next word.
     * @throws com.swabunga.spell.event.WordNotFoundException search string contains no more words.
     */
    public Word next() {
        if (settings != null) {
            minimumWordLength = settings.getInt(GrammarPlugin.MIN_WORD_LENGTH);
        }

        if (nextWord == null) {
            throw new WordNotFoundException("No more words found.");
        }

        currentWord.copy(nextWord);

        int current = sentenceIterator.current();
        setSentenceIterator(currentWord);

        int i = currentWord.getEnd();
        boolean finished = false;
        boolean started = false;
        boolean wasPackageSeparator = false;
        boolean wasUnderscoreSeparator = false;

        search:
        while (i < text.length() && !finished) {

            i = ignore(i, '@');
            i = ignore(i, "<code>", "</code>");
            i = ignore(i, "<CODE>", "</CODE>");
            i = ignore(i, '<', '>');

            if (i >= text.length()) break search;
            if (i == text.length() - 1) {
                break;
            }
            char currentLetter = text.charAt(i);
            if (!inComment) {
                //Reset on new line.

                //Find words.
                if (currentLetter == '\n') {
                    inComment = false;
                    i++;
                    continue search;
                } else if (isPackageSeparator(i) || isUnderscoreSeparator(i)) {
                    i++;
                    continue search;
                } else if (!isWordChar(i)) {
                    i++;
                    continue search;
                }
                while (i < text.length()) {
                    if (!started && isWordChar(i)) {
                        nextWord.setStart(i);
                        started = true;
                    } else if (started && !isWordChar(i) && text.substring(nextWord.getStart(), i).length() == 1) {
                        // ignore variables like i
                        started = false;
                        i++;
                        break;
                    } else if (started && Character.isUpperCase(text.charAt(i)) && nextWord.getStart() < i && isCamelCase(i)) {
                        //camel case
                        nextWord.setText(text.substring(nextWord.getStart(), i));
                        finished = true;
                        break search;
                    } else if (started && !isWordChar(i)) {
                        nextWord.setText(text.substring(nextWord.getStart(), i));
                        finished = true;
                        break search;
                    } else if (started && isPackageSeparator(i)) {
                        nextWord.setText(text.substring(nextWord.getStart(), i));
                        finished = false;
                        wasPackageSeparator = true;
                        i++;
                        break search;
                    } else if (started && isUnderscoreSeparator(i)) {
                        nextWord.setText(text.substring(nextWord.getStart(), i));
                        finished = false;
                        wasUnderscoreSeparator = true;
                        i++;
                        break search;
                    }

                    currentLetter = text.charAt(i++);
                }

            } else if (currentLetter == '*') {
                inComment = true;
                i++;
            } else {
                i++;
            }
        }

        if (!started) {
            nextWord = null;
        } else if (!finished && !wasPackageSeparator && i == text.length() - 1 && isWordChar(i)) {
            nextWord.setText(text.substring(nextWord.getStart(), i + 1));
        } else if (!finished && wasUnderscoreSeparator) {
            nextWord.setText(text.substring(nextWord.getStart(), i - 1));
        } else if (!finished && !wasPackageSeparator) {
            nextWord.setText(text.substring(nextWord.getStart(), i));
        } else if (!finished && wasPackageSeparator) {
            nextWord.setText(text.substring(nextWord.getStart(), i - 1));
        }

        if (isWordLessThenMinLenght(currentWord)) {
            currentWord.setText("");
        }

        return currentWord;
    }

    protected void init() {
        super.init();
        inComment = false;
    }

    @Override
    protected boolean isWordChar(final int position) {
        boolean isPartOfWord = super.isWordChar(position);
        if (!isPartOfWord && (text.charAt(position) == '(' || text.charAt(position) == ')' || text.charAt(position) == ';' || text.charAt(position) == '_')
                || Character.isDigit(text.charAt(position))) {
            isPartOfWord = false;
        }
        return isPartOfWord;
    }

    private boolean isCamelCase(final int position) {
        boolean result = false;
        if (position > 1 && text.length() > 2 && position < text.length() - 1) {
            char curr = text.charAt(position);
            char before = text.charAt(position - 1);
            char after = text.charAt(position + 1);
            result = Character.isLetterOrDigit(before)
                    && (Character.isLowerCase(before) || Character.isUpperCase(before))
                    && Character.isLetterOrDigit(after) && Character.isLowerCase(after);
        }
        return result;
    }

    private boolean isUnderscoreSeparator(final int position) {
        boolean result = false;
        if (position > 1 && text.length() > 2 && position < text.length() - 1) {
            char curr = text.charAt(position);
            char before = text.charAt(position - 1);
            char after = text.charAt(position + 1);
            result = Character.isLetterOrDigit(before) && Character.isLetterOrDigit(after) && curr == '_';
        }
        return result;
    }

    private boolean isPackageSeparator(final int position) {
        if (position > 1 && text.length() > 2 && position < text.length() - 1) {
            char curr = text.charAt(position);
            char before = text.charAt(position - 1);
            char after = text.charAt(position + 1);
            return Character.isLetterOrDigit(before) && Character.isLetterOrDigit(after) && curr == '.';
        }
        return false;
    }


    private boolean isWordLessThenMinLenght(final Word word) {
        return word.length() < minimumWordLength;
    }

    public void setMinimumWordLength(int minimumWordLength) {
        this.minimumWordLength = minimumWordLength;
    }

    private boolean isDigit(final int position) {
        if (position > 1 && text.length() > 2 && position < text.length()) {
            return Character.isDigit(text.charAt(position));
        }
        return false;

    }
}
