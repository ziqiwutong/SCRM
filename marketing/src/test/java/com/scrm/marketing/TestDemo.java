package com.scrm.marketing;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author fzk
 * @date 2021-10-19 19:37
 */
public class TestDemo {
    @Test
    void test1(){
        // 步长为30
        int num=1-30;
        for(int i=0;i<10;++i){
            System.out.println(num=num+30);
        }
    }

    @Test
    void test2(){
        int m,n,u,r;
        m=100;
        n=50;
        u=m*n;
        do{
            r=m%n;
            m=n;
            n=r;
        }while (r!=0);
        System.out.println(m);
    }

    @Test
    void test3(){
        int a=100,b=64,i,t;
        for(t=1,i=2;i<=a&&i<=b;++i)
            while (a%i==0&&b%i==0){
                a/=i;
                b/=i;
                t*=i;
            }
        System.out.println(t);
    }

    @Test
    void test4(){
        List<Long> shareIds=new ArrayList<>();
        shareIds.add(1L);
        shareIds.add(1L);
        shareIds.add(1L);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Long shareId : shareIds) {
            sb.append(shareId);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb.toString());
    }
}
