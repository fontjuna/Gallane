package com.nohseunghwa.gallane.temporary;

import com.nohseunghwa.gallane.backing.Spliter;

import java.util.Scanner;

/**
 * Created by fontjuna on 2017-08-26.
 */

public class TestMain {
    public void main(String[] args) {
        int go = 2;
        switch (go){
            case 1: test1();
            case 2: test2();
        }
    }

    private void test2() {

    }
    private void test1() {
        // 반복 입력 받으면서 갈라내 입력창 테스트
        Scanner sc = new Scanner(System.in);
        String input;

        while (true) {
            input = sc.nextLine();
            if (input.isEmpty()) {
                break;
            }
            try {
                Spliter spliter = new Spliter(input, 1);
                System.out.println(input + " = \n" + spliter.getResult());
            } catch (Exception e) {
                System.out.println(input + " = " + "error!");
            }
        }
    }
}