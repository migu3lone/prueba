import sqlite3

def crear_base_datos():
    # Conectar o crear la base de datos
    conexion = sqlite3.connect("perros.db")

    # Crear un cursor para ejecutar comandos SQL
    cursor = conexion.cursor()

    # Crear la tabla 'perros'
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS perros (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            raza TEXT NOT NULL,
            edad INTEGER NOT NULL,
            peso REAL NOT NULL,
            sexo TEXT NOT NULL
        )
    ''')

    # Insertar datos iniciales
    datos_iniciales = [
        ("Firulais", "Labrador", 3, 25.5, "M"),
        ("Max", "Pastor Alemán", 5, 30.2, "M"),
        ("Luna", "Beagle", 2, 18.0, "H"),
        ("Rocky", "Bulldog", 4, 22.3, "M"),
        ("Bella", "Chihuahua", 1, 3.5, "H"),
        ("Toby", "Golden Retriever", 6, 29.0, "M"),
        ("Nina", "Poodle", 3, 8.4, "H"),
        ("Simba", "Husky Siberiano", 4, 24.8, "M"),
        ("Coco", "Pomerania", 2, 5.3, "M"),
        ("Molly", "Dachshund", 3, 6.2, "H"),
        ("Rex", "Doberman", 5, 32.5, "M"),
        ("Lola", "Shih Tzu", 2, 7.1, "H"),
        ("Bobby", "Boxer", 6, 28.6, "M"),
        ("Maggie", "Collie", 4, 21.4, "H"),
        ("Chester", "San Bernardo", 5, 45.3, "M"),
        ("Daisy", "Cocker Spaniel", 3, 12.8, "H"),
        ("Duke", "Basset Hound", 4, 23.1, "M"),
        ("Sasha", "Border Collie", 2, 20.7, "H"),
        ("Zeus", "Akita", 6, 34.5, "M"),
        ("Nala", "Weimaraner", 4, 28.2, "H"),
        ("Oscar", "Galgo", 5, 27.4, "M"),
        ("Chispa", "Jack Russell", 3, 8.5, "H"),
        ("Rufo", "Fox Terrier", 4, 9.2, "M"),
        ("Mia", "Pinscher", 2, 6.0, "H"),
        ("Thor", "Gran Danés", 6, 50.7, "M"),
        ("Kira", "Pitbull", 5, 30.1, "H"),
        ("Maxi", "Dogo Argentino", 4, 37.2, "M"),
        ("Candy", "Caniche", 3, 9.8, "H"),
        ("Terry", "Yorkshire Terrier", 2, 3.1, "M")
    ]

    cursor.executemany('''
        INSERT INTO perros (nombre, raza, edad, peso, sexo)
        VALUES (?, ?, ?, ?, ?)
    ''', datos_iniciales)

    # Confirmar los cambios y cerrar la conexión
    conexion.commit()
    conexion.close()

    print("Base de datos 'perros.db' creada con datos iniciales.")

if __name__ == "__main__":
    crear_base_datos()
