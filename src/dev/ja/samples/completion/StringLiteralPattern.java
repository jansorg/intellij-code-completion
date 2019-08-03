package dev.ja.samples.completion;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.patterns.InitialPatternCondition;
import com.intellij.patterns.ObjectPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.Nullable;

/**
 * Pattern which only accepts PsiElements, which either are a string literal or a comment of the current language.
 * It also accepts if the element's parent matches these conditions.
 *
 * @author jansorg
 */
class StringLiteralPattern extends ObjectPattern<PsiElement, StringLiteralPattern> {
    StringLiteralPattern() {
        super(new InitialPatternCondition<PsiElement>(PsiElement.class) {
            @Override
            public boolean accepts(@Nullable Object o, ProcessingContext context) {
                if (!(o instanceof PsiElement)) {
                    return false;
                }

                PsiElement psi = (PsiElement) o;

                Language language = PsiUtilCore.findLanguageFromElement(psi);
                ParserDefinition definition = LanguageParserDefinitions.INSTANCE.forLanguage(language);
                if (definition == null) {
                    return false;
                }

                // suggest completions in string and comment literals
                TokenSet tokens = TokenSet.orSet(
                        definition.getStringLiteralElements(),
                        definition.getCommentTokens());

                ASTNode node = psi.getNode();
                if (node == null) {
                    return false;
                }

                if (tokens.contains(node.getElementType())) {
                    return true;
                }

                node = node.getTreeParent();
                return node != null && tokens.contains(node.getElementType());
            }
        });
    }
}
