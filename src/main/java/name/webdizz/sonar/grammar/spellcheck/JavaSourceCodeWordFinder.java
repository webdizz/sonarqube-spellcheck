package name.webdizz.sonar.grammar.spellcheck;

import com.swabunga.spell.event.AbstractWordFinder;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordNotFoundException;

public class JavaSourceCodeWordFinder extends AbstractWordFinder {
    private boolean inComment;

    public JavaSourceCodeWordFinder() {
        super();
    }

    public JavaSourceCodeWordFinder(final String searchText) {
        super(searchText);
    }

    /**
     * This method scans the text from the end of the last word,  and returns a
     * new Word object corresponding to the next word.
     *
     * @return the next word.
     * @throws com.swabunga.spell.event.WordNotFoundException
     *          search string contains no more words.
     */
    public Word next() {

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
                } else if (isPackageSeparator(i)) {
                    i++;
                    continue search;
                } else if (!isWordChar(i)) {
                    i++;
                    continue search;
                }
                while (i < text.length() - 1) {
                    if (!started && isWordChar(i)) {
                        nextWord.setStart(i);
                        started = true;
                    } else if (started && !isWordChar(i) && text.substring(nextWord.getStart(), i).length() == 1) {
                        // ignore variables like i
                        started = false;
                        i++;
                        break;
                    } else if (started && Character.isUpperCase(text.charAt(i)) && nextWord.getStart() < i) {
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
                    }

                    currentLetter = text.charAt(++i);
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
        } else if (!finished && !wasPackageSeparator) {
            nextWord.setText(text.substring(nextWord.getStart(), i));
        } else if (!finished && wasPackageSeparator) {
            nextWord.setText(text.substring(nextWord.getStart(), i - 1));
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
        if (!isPartOfWord && (text.charAt(position) == '(' || text.charAt(position) == ')' || text.charAt(position) == ';')) {
            isPartOfWord = false;
        }
        return isPartOfWord;
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
}
