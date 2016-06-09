
package com.havens.siamese.dfa;

/**
 * 这是个简单的接口，表示发现匹配关键词后应做的操作
 * keyword:匹配的关键词
 */
public interface DFAOutCallBack {
    void CallBack(String keyword);
}
