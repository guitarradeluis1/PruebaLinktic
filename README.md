# Prueba Linktic- Desarrollador Backend
 Luis Eduardo Bernal S.

## Uso Swagger
Para su ejecución la [URL](http://localhost:8080/swagger-ui/index.html) :
```
http://localhost:8080/swagger-ui/index.html
```

## Base de datos
Se realiza desarollo en SQLite el archivo se encuentra en:
```
src/main/resources/BD/data.sqlite3
```

## Api Productos
Crud para manejo de productos (API JSON)

| Acción              | Ejemplo HTTP                                     |
|---------------------|--------------------------------------------------|
| Estado API          | [Ejemplo](doc/productos/estado.http)             |
| Todos los productos | [Ejemplo](doc/productos/productos.http)          |
| Buscar por nombre   | [Ejemplo](doc/productos/productosBuscar.http)    |
| Buscar por Id       | [Ejemplo](doc/productos/productosId.http)        |
| Elimina por Id      | [Ejemplo](doc/productos/productosIdDeleted.http) |
| Nevo Producto       | [Ejemplo](doc/productos/productosNew.http)       |
| Editar Producto     | [Ejemplo](doc/productos/productosUpdate.http)    |
