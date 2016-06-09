
package com.havens.siamese.dfa;

import java.util.*;

/**
 * 算法概述
 * <p/>
 * http://www-igm.univ-mlv.fr/~lecroq/string/node4.html
 *
 * Created by havens on 15-10-20.
 */
public class DFA {
    /**
     * 内部类DFAState，表示DFA状态节点
     */
    private class DFAState {
        public DFAState(DFAState parent) {
            this.m_Parent = parent;
            this.m_Failure = null;
        }

        // 记录了该状态节点下，字符-->另一个状态的对应关系
        public Map<Character, DFAState> m_Goto = new HashMap<Character, DFAState>();
        // 如果该状态下某具体字符找不到对应的下一状态，应该跳转到m_Failure状态继续查找
        public DFAState m_Failure;
        // 该状态节点的前一个节点
        public DFAState m_Parent;
        // 记录了到达该节点时，匹配到的关键词
        public List<String> m_Output = new ArrayList<String>();

        // 为当前状态节点添加字符c对应的下一状态
        DFAState AddGoto(char c) {
            if (!m_Goto.containsKey(c)) {
                // not in the goto table
                DFAState newState = new DFAState(this);
                m_Goto.put(c, newState);
                return newState;
            } else {
                return m_Goto.get(c);
            }
        }

        // 调用outputCallback处理当前状态节点对应的关键词列表
        void Output(DFAOutCallBack outputCallback) {
            for (Iterator iter = m_Output.iterator(); iter.hasNext(); ) {
                String element = (String) iter.next();
                outputCallback.CallBack(element);
            }
        }
    }

    public DFAState m_StartState;//起始状态

    public Map<String, Integer> m_Keywords = new HashMap<String, Integer>();//记录了当前关键词集合

    public DFA() {//初始化DFA，设置初始状态
        m_StartState = new DFAState(null);
        m_StartState.m_Failure = m_StartState;
    }

    // 清除所有DFA状态
    public void closeDFA() {
        CleanStates(m_StartState);
    }

    // 增加关键词，同时重建DFA
    public void AddKeyword(String keyword) {
        m_Keywords.put(keyword, 0);
        RebuildDFA();
    }

    // 删除关键词，同时重建DFA
    public void DeleteKeyword(String keyword) {
        m_Keywords.remove(keyword);
        RebuildDFA();
    }

    // 检索字符串text是否包含关键词，并调用outputCallback处理匹配的关键词
    public synchronized void Search(String text, DFAOutCallBack outputCallback) {
        DFAState curState = m_StartState;
        int i;
        for (i = 0; i < text.length(); ++i) {
            // 查看状态机中当前状态下该字符对应的下一状态，如果在当前状态下找不到满足该个字符的状态路线，
            // 则返回到当前状态的失败状态下继续寻找，直到初始状态
            while (curState.m_Goto.containsKey(text.charAt(i)) == false) {
                if (curState.m_Failure != m_StartState) {
                    if (curState == curState.m_Failure) { //陷入死循环了...
                        System.out.println("DFA Failure");
                        break;
                    }
                    curState = curState.m_Failure; // 返回到当前状态的失败状态
                } else {
                    curState = m_StartState;
                    break;
                }
            }
            // 如果当前状态下能找到该字符对应的下一状态，则跳到下一状态m，
            // 如果状态m包含了m_Output，表示匹配到了关键词，具体原因请继续往下看
            if (curState.m_Goto.containsKey(text.charAt(i))) {
                curState = curState.m_Goto.get(text.charAt(i));
                if (!curState.m_Output.isEmpty()) {
                    curState.Output(outputCallback);
                }
            }
        }
    }

    // 添加关键词到状态机的实际操作，建立状态节点，并设置最后结束节点的m_Output
    void DoAddWord(String keyword) {
        int i;
        DFAState curState = m_StartState;

        for (i = 0; i < keyword.length(); i++) {
            curState = curState.AddGoto(keyword.charAt(i));
        }

        curState.m_Output.add(keyword);
    }

    // 重建状态机
    void RebuildDFA() {
        CleanStates(m_StartState);

        m_StartState = new DFAState(null);
        m_StartState.m_Failure = m_StartState;
        // add all keywords
        for (String key : m_Keywords.keySet()) {
            DoAddWord(key);
        }
        // 为每个状态节点设置失败跳转的状态节点
        DoFailure();
    }

    // 清除state下的所有状态节点
    void CleanStates(DFAState state) {
        for (char key : state.m_Goto.keySet()) {
            CleanStates(state.m_Goto.get(key));
        }
        state = null;
    }

    // 为每个状态节点设置失败跳转的状态节点
    void DoFailure() {

        LinkedList<DFAState> q = new LinkedList<DFAState>();
        // 首先设置起始状态下的所有子状态,设置他们的m_Failure为起始状态，并将他们添加到q中
        for (char c : m_StartState.m_Goto.keySet()) {
            q.add(m_StartState.m_Goto.get(c));
            m_StartState.m_Goto.get(c).m_Failure = m_StartState;
        }

        while (!q.isEmpty()) {
            // 获得q的第一个element，并获取它的子节点，为每个子节点设置失败跳转的状态节点
            DFAState r = q.getFirst();
            DFAState state;
            q.remove();
            for (char c : r.m_Goto.keySet()) {
                q.add(r.m_Goto.get(c));
                // 从父节点的m_Failure(m1)开始，查找包含字符c对应子节点的节点，
                // 如果m1找不到，则到m1的m_Failure查找，依次类推
                state = r.m_Failure;
                while (state.m_Goto.containsKey(c) == false) {
                    state = state.m_Failure;
                    if (state == m_StartState) {
                        break;
                    }
                }
                // 如果找到了，设置该子节点的m_Failure为找到的目标节点(m2)，
                // 并把m2对应的关键词列表添加到该子节点中
                if (state.m_Goto.containsKey(c)) {
                    r.m_Goto.get(c).m_Failure = state.m_Goto.get(c);
                    for (String str : r.m_Goto.get(c).m_Failure.m_Output) {
                        r.m_Goto.get(c).m_Output.add(str);
                    }
                } else { //找不到，设置该子节点的m_Failure为初始节点
                    r.m_Goto.get(c).m_Failure = m_StartState;
                }
            }

        }
    }


    public static void main(String[] args) {
        DFA test = new DFA();
        test.AddKeyword("操");
        final String myWord = "操";
        test.Search("操", new DFAOutCallBack() {
            public void CallBack(String keyword) {
                System.out.println(myWord.replace(keyword, "*"));
                System.out.println(myWord);
            }
        });

        test.Search("操", new DFAOutCallBack() {
            public void CallBack(String keyword) {
                System.out.println(myWord.replace(keyword, "*"));
                System.out.println(myWord);
            }
        });
    }
}
