# sqli-service

`sqli-service`, DVWA benzeri zafiyetli bir mikroservis laboratuvarının Java/Spring Boot tabanlı SQL injection servisidir.

## Teknolojiler

- Java 21
- Spring Boot 4.1.0
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Validation

## Servis Yapısı

Bu servis şu katmanlarla oluşturulmuştur:

- `controller`
- `service`
- `repository`
- `entity`

Örnek domain nesnesi olarak `AppUser` kullanılmaktadır.

## Varsayılan Uygulama Ayarları

Uygulama konfigürasyonu şu dosyada yer alır:

- [src/main/resources/application.yml](/home/ozzy/Documents/oxcDVWA2/vuln-microservice/sqli-service/src/main/resources/application.yml)

Varsayılan ayarlar:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/sqli_service
    username: dvwa
    password: dvwa123
```

Environment variable ile override edilebilen alanlar:

- `SERVER_PORT`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

## Veritabanı Ayarları

Docker Compose içinde tanımlı PostgreSQL bilgileri:

- Host: `localhost`
- Port: `5433`
- Database: `sqli_service`
- Username: `dvwa`
- Password: `dvwa123`

Not: PostgreSQL container içindeki gerçek port yine `5432`'dir. `5433`, sadece host makineden erişim için kullanılır.

Seed ve schema dosyaları:

- [../docker/postgres/init/01-schema.sql](/home/ozzy/Documents/oxcDVWA2/vuln-microservice/docker/postgres/init/01-schema.sql)
- [../docker/postgres/init/02-seed.sql](/home/ozzy/Documents/oxcDVWA2/vuln-microservice/docker/postgres/init/02-seed.sql)

## Çalıştırma

### 1. PostgreSQL container'ını ayağa kaldır

Proje root dizininde çalıştır:

```bash
docker compose up -d
```

İstersen durum kontrolü için:

```bash
docker compose ps
```

### 2. Servisi başlat

`sqli-service` dizinine gir:

```bash
cd sqli-service
```

Ardından uygulamayı çalıştır:

```bash
./mvnw spring-boot:run
```

### 3. Alternatif: Uygulamayı Docker ile çalıştır

Java 21 host makinede kurulu değilse uygulamayı container içinde çalıştırabilirsin:

```bash
docker compose up -d --build
```

Bu komut:

- PostgreSQL container'ını başlatır
- `sqli-service` uygulamasını Java 21 içeren image ile build eder
- uygulamayı `8080` portunda ayağa kaldırır

Durum kontrolü:

```bash
docker compose ps
```

Log izlemek için:

```bash
docker compose logs -f sqli-service
```

## Geliştirme Notları

- Bu projede `spring.jpa.hibernate.ddl-auto=validate` kullanılıyor.
- Yani tablo yapısı uygulama tarafından otomatik üretilmez.
- Tablo oluşturma ve seed data işlemleri Docker init SQL dosyaları üzerinden yapılır.

## Veritabanını Sıfırlama

Schema veya seed dosyalarını değiştirdikten sonra veritabanını temiz şekilde yeniden oluşturmak için:

```bash
docker compose down -v
docker compose up -d
```

## Mevcut Endpointler

- `GET /api/users`
- `GET /api/users/{id}`
- `GET /api/users/search?username=...`
- `GET /api/users/vulnerable/search?username=...`
- `GET /api/auth/vulnerable/login?username=...&password=...`
- `POST /api/users`
- `DELETE /api/users/{id}`

## Not

Bu servis şu anda temel CRUD iskeleti ile hazırlandı. Daha sonra bunun üstüne kasıtlı SQL injection senaryoları ve vulnerable endpointler eklenebilir.

## SQL Injection Lab Endpoint

Bu serviste eğitim amaçlı kasıtlı olarak zafiyetli bir endpoint de bulunur:

- `GET /api/users/vulnerable/search?username=...`
- `GET /api/auth/vulnerable/login?username=...&password=...`

Bu endpointler, kullanıcı girdisini native SQL sorgusuna doğrudan birleştirerek çalışır. Yani güvenli değildir ve yalnızca kontrollü lab ortamında kullanılmalıdır.
