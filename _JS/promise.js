// --- DEKLARACJE ---
export function getUserPromise(userId) {
    return new Promise((resolve, reject) => {
        console.log("Rozpoczynam pobieranie użytkownika...");
        setTimeout(() => resolve({ id: userId, name: "Jan Kowalski" }), 1000);
    });
}

export function getOrdersPromise(user) {
    return new Promise((resolve, reject) => {
        console.log(`Szukam zamówień dla: ${user.name}...`);
        setTimeout(() => resolve(['Laptop', 'Myszka']), 1000);
    });
}

export function processPaymentPromise(orders) {
    return new Promise((resolve, reject) => {
        console.log(`Przetwarzam płatność za ${orders.length} przedmioty...`);
        setTimeout(() => resolve("Płatność zakończona sukcesem!"), 1000);
        // Gdyby coś poszło nie tak, użylibyśmy: reject("Odrzucono kartę!");
    });
}

// --- WYKONANIE (Promise Chain) ---
getUserPromise(1)
    .then(user => getOrdersPromise(user))
    .then(orders => processPaymentPromise(orders))
    .then(result => console.log("Koniec procesu:", result))
    .catch(error => console.error("Wystąpił błąd na którymś etapie:", error));