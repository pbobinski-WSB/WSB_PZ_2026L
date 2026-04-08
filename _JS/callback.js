// --- DEKLARACJE ---
function getUser(userId, callback) {
    console.log("Rozpoczynam pobieranie użytkownika...");
    setTimeout(() => {
        callback({ id: userId, name: "Jan Kowalski" });
    }, 1000);
}

function getOrders(user, callback) {
    console.log(`Szukam zamówień dla: ${user.name}...`);
    setTimeout(() => {
        callback(['Laptop', 'Myszka']);
    }, 1000);
}

function processPayment(orders, callback) {
    console.log(`Przetwarzam płatność za ${orders.length} przedmioty...`);
    setTimeout(() => {
        callback("Płatność zakończona sukcesem!");
    }, 1000);
}

// --- WYKONANIE (Callback Hell) ---
getUser(1, (user) => {
    getOrders(user, (orders) => {
        processPayment(orders, (result) => {
            console.log("Koniec procesu:", result);
        });
    });
});