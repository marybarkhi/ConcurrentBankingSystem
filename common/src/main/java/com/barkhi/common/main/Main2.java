package com.barkhi.common.main;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Main2 {
    public static void main(String[] args) {
     /*   int cores = Runtime.getRuntime().availableProcessors();
        long memory = Runtime.getRuntime().totalMemory();
        System.out.println("number of cores" + cores);
        System.out.println("size of memory" + memory);*/
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 5; i++) {
            executorService.submit(()-> System.out.println(Thread.currentThread().getName()));
        }
        executorService.shutdown();
        System.out.println(reverse("marzieh will success at blu interview"));
    }
    public static String reverse(String input){
        StringBuilder reversed = new StringBuilder();
        for (int i = input.length() - 1 ; i >= 0 ; i--) {
            reversed.append(input.charAt(i));
        }
        return reversed.toString();
    }
}
