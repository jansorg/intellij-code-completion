package dev.ja.samples.completion;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.testFramework.fixtures.CompletionAutoPopupTester;
import org.junit.Assert;

/**
 * @author jansorg
 */
public class StringLiteralDictionaryAutoPopupContributorTest extends BasePlatformTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Dictionary.load();
    }

    @Override
    protected boolean runInDispatchThread() {
        return false;
    }

    public void testAutoPopupCompletions() {
        CompletionAutoPopupTester tester = new CompletionAutoPopupTester(myFixture);
        tester.runWithAutoPopupEnabled(() -> {
            myFixture.configureByText("test.txt", "");

            tester.typeWithPauses("ob");

            tester.joinCompletion();
            Assert.assertNotNull(tester.getLookup());
            Assert.assertEquals(314, tester.getLookup().getItems().size());

            // ob -> obfusc
            tester.typeWithPauses("fusc");
            tester.joinCompletion();
            Assert.assertNotNull(tester.getLookup());
            Assert.assertEquals(8, tester.getLookup().getItems().size());
        });
    }
}