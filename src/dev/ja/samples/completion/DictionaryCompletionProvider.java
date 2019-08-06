package dev.ja.samples.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.spellchecker.compress.CompressedDictionary;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jansorg
 */
class DictionaryCompletionProvider extends CompletionProvider<CompletionParameters> {
    private final boolean onlyManual;

    /**
     * @param onlyManual if true, then completions are only returned when the user manually requested it
     */
    DictionaryCompletionProvider(boolean onlyManual) {
        this.onlyManual = onlyManual;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        if (parameters.isAutoPopup() && onlyManual) {
            return;
        }

        String prefix = result.getPrefixMatcher().getPrefix();
        if (prefix.isEmpty()) {
            return;
        }

        // make sure that our prefix is the last word
        // for plain text file, all the content up to the caret is the prefix
        // we don't want that, because we're only completing a single word
        CompletionResultSet dictResult;
        int lastSpace = prefix.lastIndexOf(' ');
        if (lastSpace >= 0 && lastSpace < prefix.length() - 1) {
            prefix = prefix.substring(lastSpace + 1);
            dictResult = result.withPrefixMatcher(prefix);
        } else {
            dictResult = result;
        }

        int length = prefix.length();
        char firstChar = prefix.charAt(0);
        boolean isUppercase = Character.isUpperCase(firstChar);

        List<CompressedDictionary> dicts = Dictionary.get();
        if (dicts == null || dicts.isEmpty()) {
            return;
        }

        for (CompressedDictionary dict : dicts) {
            dict.getWords(Character.toLowerCase(firstChar), length, length + 20, word -> {
                ProgressManager.checkCanceled();

                LookupElementBuilder element;
                if (isUppercase) {
                    element = LookupElementBuilder.create(word.substring(0, 1).toUpperCase() + word.substring(1));
                } else {
                    element = LookupElementBuilder.create(word);
                }
                dictResult.addElement(element);
            });
        }
    }
}
