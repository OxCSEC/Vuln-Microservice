# gateway-service

`gateway-service`, `sqli-service` için API Gateway olarak çalışan Spring Cloud Gateway servisidir.

## Teknolojiler

- Java 21
- Spring Boot 4.1.0
- Spring Reactive Web
- Spring Cloud Gateway

## Varsayılan Çalışma Portu

- Container içi port: `8081`
- Host erişim portu: `8082`

## Route Tanımı

Gateway şu route'u sağlar:

- `GET /sqli/**`

Bu route, gelen isteği `sqli-service` servisine iletir ve `/sqli` prefix'ini kaldırır.

Örnek:

- Gateway çağrısı: `http://localhost:8082/sqli/api/users`
- Hedef servis çağrısı: `http://sqli-service:8080/api/users`
