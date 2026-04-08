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
![Diagrama de base de datos](doc/bd.png

## Manejo de complas
Param el manejo de compras se realiza la siguiente trazado
1. Se crea una Orde de servicio
2. Se agrega productos a esta orden
3. Una vez cerrada la orden esta de forma automatica realiza actualización de cantidad en inventario

Nota: Los evento se registran de forma automatica al realizar estos cambio generando historico de registros

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
