package com.domination.loadbalance;

import com.domination.common.URL;

import java.util.List;
import java.util.Random;

public class Loadbalance {
    //仅仅是随机挑一个进行
    public static URL random(List<URL> urls){
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }
}
