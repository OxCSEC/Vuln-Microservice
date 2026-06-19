# vuln-microservice

`vuln-microservice`, DVWA benzeri zafiyetli senaryoları mikroservis mimarisi içinde göstermek için hazırlanmış eğitim amaçlı bir laboratuvar projesidir.

Bu repo şu anda iki ana servisten oluşur:

- `sqli-service`: SQL Injection odaklı zafiyetli uygulama servisi
- `gateway-service`: istekleri backend servislere yönlendiren API Gateway

## Önemli Uyarı

Bu proje bilinçli olarak zafiyetli kod içerir.

- production ortamında kullanılmamalıdır
- internet erişimine açık ortamlarda yayınlanmamalıdır
- yalnızca eğitim, demo ve yerel laboratuvar kullanımı için uygundur

## Mimari

Projede şu bileşenler bulunur:

- `gateway-service`
  - Spring Cloud Gateway tabanlı API gateway
  - dış dünyadan gelen istekleri backend servislere yönlendirir

- `sqli-service`
  - Spring Boot tabanlı SQL Injection eğitim servisi
  - güvenli ve kasıtlı olarak güvensiz endpoint örnekleri içerir

- `postgres`
  - `sqli-service` için PostgreSQL veritabanı
  - schema ve seed data Docker init scriptleri ile yüklenir

## Klasör Yapısı

```text
vuln-microservice/
├── docker/
│   └── postgres/
│       └── init/
├── gateway-service/
├── sqli-service/
└── docker-compose.yml
```

## Gereksinimler

Projeyi çalıştırmak için şunlar önerilir:

- Git
- Docker
- Docker Compose

İsteğe bağlı:

- Java 21
- Maven

Not: Java 21 host makinede kurulu değilse servisler Docker üzerinden çalıştırılabilir.

## Hızlı Başlangıç

### 1. Repoyu klonla

```bash
git clone <REPOSITORY_URL>
cd vuln-microservice
```

### 2. Tüm sistemi Docker ile ayağa kaldır

```bash
docker compose up -d --build
```

### 3. Container durumlarını kontrol et

```bash
docker compose ps
```

### 4. Logları izle

```bash
docker compose logs -f
```

## Servisler ve Portlar

Varsayılan port eşleştirmeleri:

| Bileşen | Container Port | Host Port | Açıklama |
|---|---:|---:|---|
| gateway-service | 8081 | 8082 | Dış erişim için gateway |
| sqli-service | 8080 | 8080 | Backend uygulama servisi |
| postgres | 5432 | 5433 | `sqli-service` veritabanı |

Not:

- `8081` host tarafında dolu olduğu için gateway host portu `8082` olarak ayarlanmıştır
- `5432` host tarafında dolu olduğu için PostgreSQL host portu `5433` olarak ayarlanmıştır

## Erişim Noktaları

### Gateway üzerinden

- `http://localhost:8082/sqli/api/users`
- `http://localhost:8082/sqli/api/users/vulnerable/search?username=admin`
- `http://localhost:8082/sqli/api/auth/vulnerable/login?username=admin&password=password`

### Doğrudan sqli-service üzerinden

- `http://localhost:8080/api/users`
- `http://localhost:8080/api/users/vulnerable/search?username=admin`
- `http://localhost:8080/api/users/vulnerable/prepared-search?username=admin`
- `http://localhost:8080/api/auth/vulnerable/login?username=admin&password=password`

## Veritabanı Bilgileri

`sqli-service` için varsayılan PostgreSQL ayarları:

- Host: `localhost`
- Port: `5433`
- Database: `sqli_service`
- Username: `dvwa`
- Password: `dvwa123`

Schema ve seed data şu dizinden yüklenir:

- [docker/postgres/init](/home/ozzy/Documents/oxcDVWA2/vuln-microservice/docker/postgres/init)

## Projede Bulunan Bilinçli Zafiyetler

Bu repoda eğitim amacıyla aşağıdaki SQL Injection örnekleri bulunur:

- doğrudan string birleştirme ile arama sorgusu
- doğrudan string birleştirme ile login bypass senaryosu
- yanlış kullanılan `PreparedStatement` ile SQL injection

Bu endpointler kod içinde yorum satırları ile işaretlenmiştir:

- `VULNERABLE ENDPOINT`
- `VULNERABLE CODE START`
- `VULNERABLE CODE END`

## Geliştirme İçin Yerelde Çalıştırma

İstersen sadece veritabanını Docker ile çalıştırıp servisleri host makinede de başlatabilirsin.

Önce veritabanını ayağa kaldır:

```bash
docker compose up -d postgres
```

Sonra ayrı terminallerde servisleri çalıştır:

```bash
cd sqli-service
./mvnw spring-boot:run
```

```bash
cd gateway-service
./mvnw spring-boot:run
```

Not:

- bunun için host makinede Java 21 kurulu olmalıdır

## Veritabanını Sıfırlama

Seed veya schema değişikliklerinden sonra temiz başlangıç yapmak istersen:

```bash
docker compose down -v
docker compose up -d --build
```

## Faydalı Komutlar

```bash
docker compose ps
docker compose logs -f sqli-service
docker compose logs -f gateway-service
docker compose down
docker compose down -v
```

## Servis Bazlı Dokümantasyon

Daha detaylı servis dökümanları:

- [sqli-service/README.md](/home/ozzy/Documents/oxcDVWA2/vuln-microservice/sqli-service/README.md)
- [gateway-service/README.md](/home/ozzy/Documents/oxcDVWA2/vuln-microservice/gateway-service/README.md)

## GitHub'a Pushlama Önerisi

Bu proje şu aşamada tek bir monorepo olarak tutulmaya uygundur.

Yani önerilen yaklaşım:

- tüm kök klasörü tek repository olarak pushlamak

Çünkü:

- servisler birlikte çalışıyor
- ortak `docker-compose.yml` kullanıyor
- eğitim/lab setup akışı tek yerden yönetiliyor

## Lisans / Kullanım Notu

Eğer bu repoyu herkese açık paylaşacaksan, README içinde zafiyetli eğitim projesi olduğu bilgisini görünür tutman iyi olur.
