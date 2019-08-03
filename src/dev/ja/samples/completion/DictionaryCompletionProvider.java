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

        List<CompressedDictionary> dicts = Dictionary.get();
        if (dicts == null || dicts.isEmpty()) {
            return;
        }

        String prefix = result.getPrefixMatcher().getPrefix();
        if (prefix.isEmpty()) {
            return;
        }

        int length = prefix.length();
        char firstChar = prefix.charAt(0);
        boolean isUppercase = Character.isUpperCase(firstChar);

        CompletionResultSet dictResult = result.caseInsensitive();
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
