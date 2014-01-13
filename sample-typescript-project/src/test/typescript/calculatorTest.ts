/// <reference path="vendor/jasmine/jasmine.d.ts"/>

import calculator = require("../../../src/main/typescript/calculator");

describe("Calculator", () => {
    it("should add positive numbers", () => {
        var calc = new calculator.Calculator();
        expect(calc.add(1, 1)).toBe(2);
    });
    it("should add negative numbers", () => {
        var calc = new calculator.Calculator();
        expect(calc.add(-1, -1)).toBe(-2);
    })
});