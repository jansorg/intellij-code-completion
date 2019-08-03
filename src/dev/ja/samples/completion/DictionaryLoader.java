package dev.ja.samples.completion;

import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.progress.ProgressIndicator;
import org.jetbrains.annotations.NotNull;


/**
 * Preloading activity to load the dictionary at startup.
 *
 * @author jansorg
 */
public class DictionaryLoader extends PreloadingActivity {
    @Override
    public void preload(@NotNull ProgressIndicator indicator) {
        Dictionary.load();
    }
}
