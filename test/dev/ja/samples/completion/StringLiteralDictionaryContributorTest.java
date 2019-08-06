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
        Dictionary.load();
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