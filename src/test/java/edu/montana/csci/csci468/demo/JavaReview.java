package edu.montana.csci.csci468.demo;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaReview {

    private Integer compute(String rpnEquation) {
        return 1;
    }

    @Test
    public void exampleTest() {
        String rpnEquation = "21 22 + 1 -";
        Integer answer = compute(rpnEquation);
        assertEquals(42, answer);
    }

    public static void main(String[] args) throws IOException {
        LinkedList<Integer> stack = new LinkedList<>();
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.print("> ");
            String string = obj.readLine();
        } while(true);
    }

}
