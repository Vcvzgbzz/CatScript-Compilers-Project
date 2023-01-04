package edu.montana.csci.csci468.demo;

public class FunctionStuff {

    public int coverDemo() {
        if(true) {
            return 10;
        } else {
            return 2;
        }
    }

    public void callExample(){
        example();
    }

    public void example() {
        System.out.println("Example forward reference!");
    }


}
