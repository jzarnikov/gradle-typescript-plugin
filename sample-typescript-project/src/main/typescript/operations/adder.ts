export class Adder {

    constructor(private a: number, private b: number) {
    }

    getResult(): number {
        return this.a + this.b;
    }
}