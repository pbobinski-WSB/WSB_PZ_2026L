// Wykorzystujemy funkcje zwracające Promise z poprzedniego przykładu.
// Słowo kluczowe 'async' pozwala nam użyć 'await' wewnątrz funkcji.
import { getUserPromise, getOrdersPromise, processPaymentPromise } from "./promise.js";

async function handleCheckout() {
    try {
        // 'await' pauzuje wykonanie tej konkretnej funkcji (nie całego programu!) 
        // do momentu aż Promise zostanie rozwiązany.
        
        const user = await getUserPromise(1);
        const orders = await getOrdersPromise(user);
        const result = await processPaymentPromise(orders);
        
        console.log("Koniec procesu (async/await):", result);
        
    } catch (error) {
        // Błędy obsługujemy standardowym blokiem try/catch, 
        // dokładnie tak samo jak w kodzie synchronicznym w Javie!
        console.error("Wystąpił błąd:", error);
    }
}

// --- WYKONANIE ---
handleCheckout();