import asyncio

# Słowo kluczowe 'async' tworzy funkcję asynchroniczną (zwracającą obiekt Coroutine)
async def get_user(user_id):
    print("Rozpoczynam pobieranie użytkownika...")
    await asyncio.sleep(1) # Zwalnia wątek dla innych zadań na 1 sekundę!
    return {"id": user_id, "name": "Jan Kowalski"}

async def get_orders(user):
    print(f"Szukam zamówień dla: {user['name']}...")
    await asyncio.sleep(1)
    return ['Laptop', 'Myszka']

async def process_payment(orders):
    print(f"Przetwarzam płatność za {len(orders)} przedmioty...")
    await asyncio.sleep(1)
    return "Płatność zakończona sukcesem!"

# Główna funkcja wykonawcza
async def main():
    try:
        # Kod wygląda synchronicznie, ale w momentach 'await' zwalnia zasoby
        user = await get_user(1)
        orders = await get_orders(user)
        result = await process_payment(orders)
        
        print("\n--- WYNIK ---")
        print("Koniec procesu (Python async/await):", result)
    except Exception as e:
        print("Wystąpił błąd:", e)

# Uruchomienie pętli zdarzeń (Event Loop) Pythona
asyncio.run(main())