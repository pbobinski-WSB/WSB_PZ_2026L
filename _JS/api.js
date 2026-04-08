async function fetchRealData() {
    console.log("Wysyłam żądanie HTTP...");
    try {
        // Natywna funkcja fetch zwraca Promise
        const response = await fetch('https://jsonplaceholder.typicode.com/users/1');
        const userData = await response.json(); // .json() również zwraca Promise!
        
        console.log(`Pobrano z prawdziwego serwera: ${userData.name} (${userData.email})`);
    } catch (error) {
        console.error("Błąd sieci:", error);
    }
}

fetchRealData();