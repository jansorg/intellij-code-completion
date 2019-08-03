package dev.ja.samples.completion;

import com.intellij.spellchecker.BundledDictionaryProvider;
import com.intellij.spellchecker.SpellCheckerManager;
import com.intellij.spellchecker.StreamLoader;
import com.intellij.spellchecker.compress.CompressedDictionary;
import com.intellij.spellchecker.engine.Transformation;
import com.intellij.util.containers.ContainerUtil;

import java.io.InputStream;
import java.util.List;

/**
 * @author jansorg
 */
class Dictionary {
    private static final List<CompressedDictionary> bundledDictionaries = ContainerUtil.createLockFreeCopyOnWriteList();

    static void load() {
        for (BundledDictionaryProvider provider : BundledDictionaryProvider.EP_NAME.getExtensions()) {
            for (String name : provider.getBundledDictionaries()) {
                InputStream stream = SpellCheckerManager.class.getResourceAsStream(name);
                if (stream != null) {
                    StreamLoader loader = new StreamLoader(stream, name);
                    CompressedDictionary dict = CompressedDictionary.create(loader, new Transformation());
                    bundledDictionaries.add(dict);
                }
            }
        }
    }

    static List<CompressedDictionary> get() {
        return bundledDictionaries;
    }
}
