package dev.ja.samples.completion;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jansorg
 */
public class StringLiteralDictionaryContributorTest extends LightPlatformCodeInsightFixture4TestCase {
    @Before
    public void loadDicts() {
        // preloadingActivity don't seem to be run in tests
        Dictionary.load();
    }

    @Test
    public void noEmptyPrefix() {
        myFixture.configureByText("test.txt", "");
        Assert.assertEquals("expected no completions for an empty file", 0, myFixture.completeBasic().length);

        // whitespace suffix
        myFixture.configureByText("test.txt", "foo");
        myFixture.type(" ");
        Assert.assertEquals("expected no completions after whitespace", 0, myFixture.completeBasic().length);
    }

    @Test
    public void completions() {
        myFixture.configureByText("test.txt", "");

        myFixture.type("ob");
        Assert.assertEquals(314, myFixture.completeBasic().length);

        // ob -> obfus
        myFixture.type("fus");
        Assert.assertEquals(8, myFixture.completeBasic().length);
    }
}