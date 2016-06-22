package com.havens.siamese.entity.helper;

/**
 * Created by havens on 16-6-15.
 */
public class CardHelper {

    public static int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return new int[0];
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    public static int[] genCard(int n){
        if(n<=0) return new int[0];
        return randomCommon(1,52,3*n);
    }

    public static int multiple(int type){
        switch (type){
            case 1:return 1;
            case 2:return 2;
            case 3:return 3;
            case 4:return 5;
            case 5:return 6;
            case 6:return 9;
            default:return 1;
        }
    }

    public static boolean equalsCardNUm(int m,int n){
        return m/4==n/4;
    }

    public static int cardNUm(int m){
        if(m<=36)  return m/4+m%4;
        return 0;
    }

    public static int countPoints(int[] card){
        return (cardNUm(card[0])+cardNUm(card[1])+cardNUm(card[2]))%10;
    }

    public static int cardType(int[] card){
        if(card[0]>=9&&card[0]<=12&&card[1]>=9&&card[1]<=12&&card[2]>=9&&card[2]<=12) return 6;

        if(equalsCardNUm(card[0],card[1])&&equalsCardNUm(card[0],card[2])&&equalsCardNUm(card[1],card[2])) return 5;

        if(card[0]>=41&&card[1]>=41&&card[2]>=41) return 4;

        if(countPoints(card)==9) return 3;

        if(countPoints(card)==8) return 2;

        return 1;
    }

    public static int maxCard(int[] card){
        int max=card[0];
        for(int i=1;i<card.length;i++){
            if(max<card[i]) max=card[i];
        }
        return max;
    }

    public static boolean equalCardIndex(int[] cards1,int[] cards2){
        if(maxCard(cards1)>maxCard(cards2)) return true;
        else return false;
    }

    public static boolean equalCards(int[] cards1,int[] cards2){
        //cards1>cards2 true
        //比牌型
        int type1=cardType(cards1);
        int type2=cardType(cards2);
        if(type1>type2) return true;
        else if(type1<type2) return false;
        //比点数
        int point1=countPoints(cards1);
        int point2=countPoints(cards2);
        if(point1>point2) return true;
        else if(point1<point2) return false;
        //比牌大小
        if(equalCardIndex(cards1,cards2)) return true;
        else return false;
    }


    public static void main(String[] args){
        genCard(2);
    }
}
