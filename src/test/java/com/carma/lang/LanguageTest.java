package com.carma.lang;



import junit.framework.TestCase;

public class LanguageTest  extends TestCase {



    public final void testLanguage() {
        Language lang = new Language(null, 0);
        
        assertEquals(lang.lang, null);
        assertEquals(lang.prob, 0.0, 0.0001);
        assertEquals(lang.toString(), "");
        
        Language lang2 = new Language("en", 1.0);
        assertEquals(lang2.lang, "en");
        assertEquals(lang2.prob, 1.0, 0.0001);
        assertEquals(lang2.toString(), "en:1.0");
        
    }

}
