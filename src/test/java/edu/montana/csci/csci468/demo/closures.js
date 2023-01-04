
function createAdder() {
    var count = 0;
    return function() {
        return count++;
    }
}

let adder1 = createAdder();
console.log("\nadder1 ===========");
console.log(adder1());
console.log(adder1());
console.log(adder1());

console.log("\nadder2 ===========");
let adder2 = createAdder();
console.log(adder2());
console.log(adder2());
console.log(adder2());

console.log("\nadder1 ===========");
console.log(adder1());

