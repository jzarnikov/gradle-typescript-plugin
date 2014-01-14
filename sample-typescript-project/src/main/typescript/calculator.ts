import adder = require("./operations/adder");

export class Calculator {

    add(a: number, b: number): number {
        return new adder.Adder(a, b).getResult();
    }
}