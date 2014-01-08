/// <reference path="vendor/jasmine/jasmine.d.ts"/>

import bar = require("../../../src/main/typescript/bar");

describe("", () => {
    it("", () => {
        var b = new bar.Bar();
        expect(b.foo()).toBe(1);
    });
    it ("array should contain one of its elements ", () => {
        var b = new bar.Bar();
        expect(b.arrayContains([1, 2, 3], 2)).toBeTruthy();
    });
});