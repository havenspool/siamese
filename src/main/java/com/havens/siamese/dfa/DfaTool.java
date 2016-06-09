package com.havens.siamese.dfa;


import com.havens.siamese.utils.FileHelper;

import java.util.Set;

/**
 * Created by havens on 15-10-20.
 */
public class DfaTool {
    public static Set<String> forbiddenWords;
    public static DFA removeKeyword = new DFA();

    public static void loadJsonData(){
        forbiddenWords = FileHelper.getContentLine("fbwords.txt");
        for (String word : forbiddenWords) {
            removeKeyword.AddKeyword(word);
        }
    }

}
