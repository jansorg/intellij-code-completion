package dev.ja.samples.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PlainTextTokenTypes;

/**
 * @author jansorg
 */
public class StringLiteralDictionaryContributor extends CompletionContributor {
    public StringLiteralDictionaryContributor() {
        // completions for plain text files
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(PlainTextTokenTypes.PLAIN_TEXT),
                new DictionaryCompletionProvider(false));

        // completions for content of string literals
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().with(new StringLiteralPattern()),
                new DictionaryCompletionProvider(false));

        // always suggest when invoked manually
        extend(CompletionType.BASIC,
                PlatformPatterns.not(PlatformPatterns.alwaysFalse()),
                new DictionaryCompletionProvider(true));
    }
}
