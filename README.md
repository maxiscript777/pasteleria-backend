# Pasteleria 1000 Sabores - Backend

Autor: Maximiliano Alexander Amestica Piña (trabajo individual)

Backend con Spring Boot 3.5.7, Java 21, Maven y base de datos H2 (archivo en `data/`).

## Como correrlo

Necesitas Java 21 y Maven.

```
mvn spring-boot:run
```

- API: http://localhost:8080/api/v1
- Swagger: http://localhost:8080/swagger-ui/index.html
- Al partir por primera vez se crean 3 usuarios, 8 categorias y 16 productos.

## Usuarios de prueba

| Rol | Correo | Clave |
| --- | --- | --- |
| Administrador | admin@duoc.cl | admin123 |
| Vendedor | vendedor@duoc.cl | vendedor1 |
| Cliente | cliente@gmail.com | cliente1 |

## Estructura

- `model`: las 6 tablas (Usuario, Boleta, DetalleBoleta, Producto, Categoria, Contacto)
- `repository`: acceso a la base de datos
- `service`: logica de negocio (la compra esta en `BoletaService`)
- `controller`: endpoints REST bajo `/api/v1`
- `config`: seguridad JWT, CORS, Swagger y datos iniciales

## Quien puede hacer que

| Ruta | Publico | Cliente | Vendedor | Admin |
| --- | --- | --- | --- | --- |
| POST /auth/login, /auth/registro | si | si | si | si |
| GET /productos, /categorias | si | si | si | si |
| POST /contacto | si | si | si | si |
| POST /boletas (comprar), GET /boletas/mias | no | si | si | si |
| GET /boletas, GET /boletas/{id} | no | solo la suya | si | si |
| POST/PUT/DELETE productos y categorias | no | no | no | si |
| CRUD /usuarios, GET /contacto | no | no | no | si |

## Variables para produccion (EC2)

- `JWT_SECRET_BASE64`: secreto del token (minimo 32 bytes en base64)
- `CORS_ORIGEN`: direccion del frontend, por ejemplo `http://IP_PUBLICA`

## Pruebas

Importa `postman/Pasteleria_API.postman_collection.json` en Postman y ejecutala completa (Runner).
Con el backend corriendo, deben pasar los 30 requests.
