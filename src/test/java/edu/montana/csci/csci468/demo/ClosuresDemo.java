package edu.montana.csci.csci468.demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClosuresDemo {

    @Test
    void closureDemo1() throws Exception {
        Callable<Integer> counter = createAdder();
        System.out.println(counter.call());
        System.out.println(counter.call());
        System.out.println(counter.call());
    }

    private Callable<Integer> createAdder() {
        final int[] count = {0};
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return count[0]++;
            }
        };
    }

    @Test
    void closureDemo2() throws Exception {
        Callable<Integer> counter = createAdderWithLambda();
        System.out.println(counter.call());
        System.out.println(counter.call());
        System.out.println(counter.call());
    }

    private Callable<Integer> createAdderWithLambda() {
        final int[] count = {0};
        return () -> count[0]++;
    }

    private Callable<Integer> createAdderWithLambda2() {
        final int[] count = {0};
        return () -> {
            return count[0]++;
        };
    }

    @Test
    public void listMappingWithLambda() {
        List<String> strings = Arrays.asList("a", "ab", "abc");

//        List<Integer> lengths = strings.map(s -> s.length());

//        System.out.println(lengths);
    }

    @Test
    public void listMappingWithObjects() {
        List<String> strings = Arrays.asList("a", "ab", "abc");
        List<Integer> lengths = new ArrayList<>();
        for (String string : strings) {
            lengths.add(string.length());
        }
        System.out.println(lengths);
    }

    @Test
    public void filterTest() {
        List<String> strings = Arrays.asList("a", "ab", "abc");
        List<String> longStrings =
                filter(strings, s -> s.length() > 1);
        System.out.println(longStrings);

        String prefix = "a";
        List<String> stringsThatStartWith =
                filter(strings, s -> s.startsWith(prefix));
        System.out.println(stringsThatStartWith);
    }

    public<T> List<T> filter(List<T> values, Predicate<T> filter) {
        List<T> returnList = new ArrayList<>();
        for (T value : values) {
            if (filter.test(value)) {
                returnList.add(value);
            }
        }
        return returnList;
    }

}
