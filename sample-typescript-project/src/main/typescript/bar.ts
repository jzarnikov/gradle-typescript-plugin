///<reference path="vendor/jquery/jquery.d.ts"/>

export class Bar {
    foo(): number {
        console.log("foooo!!!");
        return 1;
    }

    arrayContains(arr: any[], element: any): boolean {
        return $.inArray(element, arr) != -1;
    }


}