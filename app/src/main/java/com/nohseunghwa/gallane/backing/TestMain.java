package com.nohseunghwa.gallane.backing;

import com.nohseunghwa.gallane.temporary.Calculation;

import java.util.Scanner;

/**
 * Created by fontjuna on 2017-08-26.
 */

public class TestMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String input;

        while (true) {
            input = sc.nextLine();
            if (input.isEmpty()) {
                break;
            }
            System.out.println(input + " = " + Calculation.Calculate(input));


        }
    }
}