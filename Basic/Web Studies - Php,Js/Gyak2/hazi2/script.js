let minus = document.querySelector("#minus")
let plus = document.querySelector("#plus")
let count = document.querySelector("#counter")
plus.addEventListener("click", () => {
    if (count.value == -10) {
        minus.disabled = false
    }
    count.value++;
    if (count.value == 10) {
        plus.disabled = true
    }
})
minus.addEventListener("click", () => {
    if (count.value == 10) {
        plus.disabled = false
    }
    count.value--;
    if (count.value == -10) {
        minus.disabled = true
    }
})