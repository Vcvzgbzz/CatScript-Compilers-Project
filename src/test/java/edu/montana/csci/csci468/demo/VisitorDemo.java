package edu.montana.csci.csci468.demo;

import java.util.Arrays;
import java.util.List;

public class VisitorDemo {

    abstract static class Vehicle {
        abstract void accept(VehicleVisitor visitor);
    }

    static class Car  extends Vehicle {
        @Override
        void accept(VehicleVisitor visitor) {
            visitor.visit(this);
        }
    }

    static class Truck extends Vehicle {
        @Override
        void accept(VehicleVisitor visitor) {
            visitor.visit(this);
        }
    }

    interface VehicleVisitor {
        void visit(Car car);
        void visit(Truck truck);
    }

    static class VehiclePrinter implements VehicleVisitor {
        @Override
        public void visit(Car car) {
            System.out.println("Found a car");
        }

        @Override
        public void visit(Truck truck) {
            System.out.println("Found a truck");
        }
    }

    public static void main(String[] args) {
        List<Vehicle> vehicles = Arrays.asList(new Car(), new Truck(), new Car());
        VehiclePrinter printer = new VehiclePrinter();
        for (Vehicle vehicle : vehicles) {
            vehicle.accept(printer);
        }
    }


}
