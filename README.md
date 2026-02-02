# Hotel Booking Service

Полнофункциональный backend-сервис бронирования отелей на Spring Framework с административной панелью CMS, системой рейтингов и статистикой.

## 📋 Содержание

- [Возможности](#возможности)
- [Технологический стек](#технологический-стек)
- [Требования](#требования)
- [Установка и запуск](#установка-и-запуск)
- [Архитектура](#архитектура)
- [API Документация](#api-документация)
- [Безопасность](#безопасность)
- [Статистика](#статистика)
- [Примеры использования](#примеры-использования)

## 🚀 Возможности

### Основной функционал
- ✅ **Управление отелями** — CRUD-операции для отелей
- ✅ **Управление комнатами** — CRUD-операции для номеров с привязкой к отелям
- ✅ **Система бронирования** — бронирование комнат с проверкой доступности
- ✅ **Управление пользователями** — регистрация и управление аккаунтами
- ✅ **Система рейтингов** — выставление оценок отелям (1-5 звезд)
- ✅ **Фильтрация и поиск** — расширенный поиск по отелям и комнатам
- ✅ **Пагинация** — постраничная выдача результатов

### Административные функции
- ✅ **Безопасность** — Spring Security с Basic Auth
- ✅ **Ролевая модель** — USER и ADMIN роли
- ✅ **Статистика** — сбор событий через Kafka
- ✅ **Экспорт данных** — выгрузка статистики в CSV

## 🛠 Технологический стек

### Backend
- **Java 17**
- **Spring Boot 3.2.4**
- **Spring Data JPA** — работа с базой данных
- **Spring Security** — аутентификация и авторизация
- **MapStruct 1.5.5** — маппинг DTO ↔ Entity
- **Lombok** — уменьшение boilerplate-кода

### Базы данных
- **PostgreSQL 15** — основная реляционная БД
- **MongoDB 7.0** — хранение статистики

### Messaging
- **Apache Kafka** — обработка событий
- **Zookeeper** — координация Kafka

### Инфраструктура
- **Docker & Docker Compose** — контейнеризация
- **Maven** — сборка проекта

## 📦 Требования

- **Java JDK 17+**
- **Maven 3.8+**
- **Docker & Docker Compose**
- **8GB RAM** (рекомендуется)

## 🚀 Установка и запуск

### 1. Клонирование проекта

```bash
# Распаковать архив
unzip hotel-booking-service.zip
cd hotel-booking-service
```

### 2. Сборка проекта

```bash
# Сборка Maven-проекта
mvn clean package -DskipTests

# После успешной сборки в директории target/ появится hotel-booking-service-1.0-SNAPSHOT.jar
```

### 3. Запуск через Docker Compose

```bash
# Запуск всех сервисов (PostgreSQL, MongoDB, Kafka, Zookeeper, приложение)
docker-compose up -d

# Проверка статуса контейнеров
docker-compose ps

# Просмотр логов приложения
docker-compose logs -f hotel-booking-app
```

**Приложение будет доступно по адресу:** `http://localhost:8080`

### 4. Локальный запуск (без Docker)

Если хотите запустить приложение локально:

```bash
# 1. Запустить только инфраструктуру
docker-compose up -d postgres-db mongo-db kafka zookeeper

# 2. Дождаться запуска всех сервисов (~30 сек)

# 3. Запустить Spring Boot приложение
mvn spring-boot:run
```

### 5. Остановка

```bash
# Остановить все контейнеры
docker-compose down

# Остановить и удалить данные
docker-compose down -v
```

## 🏗 Архитектура

### Структура проекта

```
hotel-booking-service/
├── src/main/java/com/hotelBooking/
│   ├── HotelBookingApplication.java      # Точка входа
│   ├── model/                             # Entities (Hotel, Room, User, Booking)
│   ├── dto/                               # Data Transfer Objects
│   ├── repository/                        # Spring Data JPA репозитории
│   ├── service/                           # Бизнес-логика + MapStruct мапперы
│   ├── controller/                        # REST контроллеры
│   ├── config/                            # Конфигурация (SecurityConfig)
│   ├── security/                          # UserDetails и UserDetailsService
│   └── statistics/                        # Слой статистики
│       ├── model/                         # Kafka события и MongoDB документы
│       ├── repository/                    # MongoDB репозитории
│       ├── service/                       # Kafka consumer и CSV экспорт
│       └── controller/                    # Статистика API
├── src/main/resources/
│   └── application.properties             # Конфигурация приложения
├── docker-compose.yml                     # Docker окружение
├── Dockerfile                             # Образ приложения
└── pom.xml                                # Maven зависимости
```

### Схема базы данных

```
┌─────────────┐         ┌─────────────┐
│   hotels    │         │    rooms    │
├─────────────┤         ├─────────────┤
│ id (PK)     │◄────────│ id (PK)     │
│ name        │         │ hotel_id(FK)│
│ headline    │         │ name        │
│ city        │         │ room_number │
│ address     │         │ price       │
│ distance    │         │ max_guests  │
│ rating      │         └─────────────┘
│ num_ratings │                │
└─────────────┘                │
                               │
        ┌──────────────────────┘
        │
        ▼
┌─────────────┐         ┌─────────────┐
│  bookings   │         │    users    │
├─────────────┤         ├─────────────┤
│ id (PK)     │         │ id (PK)     │
│ room_id(FK) │         │ username    │
│ user_id(FK) │◄────────│ password    │
│ check_in    │         │ email       │
│ check_out   │         │ role        │
└─────────────┘         └─────────────┘
```

### Поток данных

```
User Request
    ↓
Controller → DTO
    ↓
Service → MapStruct Mapper → Entity
    ↓
Repository → Database
    ↓
Service → MapStruct Mapper → DTO
    ↓
Controller → JSON Response
```

## 📚 API Документация

### Базовый URL

```
http://localhost:8080/api
```

### Аутентификация

Все endpoints (кроме регистрации) требуют **HTTP Basic Auth**:

```
Authorization: Basic <base64(username:password)>
```

---

### 👤 Users API

#### Регистрация пользователя (без авторизации)

```http
POST /api/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123",
  "email": "john@example.com",
  "role": "USER"
}
```

**Роли:** `USER` или `ADMIN`

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

#### Получить пользователя по ID

```http
GET /api/users/{id}
Authorization: Basic <credentials>
```

#### Обновить пользователя

```http
PUT /api/users/{id}
Authorization: Basic <credentials>
Content-Type: application/json

{
  "username": "john_updated",
  "email": "john.new@example.com",
  "password": "newPassword123",
  "role": "USER"
}
```

---

### 🏨 Hotels API

#### Получить отель по ID

```http
GET /api/hotels/{id}
Authorization: Basic <credentials>
```

**Response:**
```json
{
  "id": 1,
  "name": "Grand Hotel",
  "headline": "Luxury 5-star hotel in city center",
  "city": "Moscow",
  "address": "Red Square, 1",
  "distanceFromCenter": 0.5,
  "rating": 4.5,
  "numberOfRatings": 120
}
```

#### Создать отель (только ADMIN)

```http
POST /api/hotels
Authorization: Basic admin:password
Content-Type: application/json

{
  "name": "Grand Hotel",
  "headline": "Luxury 5-star hotel",
  "city": "Moscow",
  "address": "Red Square, 1",
  "distanceFromCenter": 0.5
}
```

#### Обновить отель (только ADMIN)

```http
PUT /api/hotels/{id}
Authorization: Basic admin:password
Content-Type: application/json

{
  "name": "Grand Hotel Updated",
  "headline": "New headline",
  "city": "Moscow",
  "address": "Red Square, 1",
  "distanceFromCenter": 0.5
}
```

#### Удалить отель (только ADMIN)

```http
DELETE /api/hotels/{id}
Authorization: Basic admin:password
```

#### Получить все отели

```http
GET /api/hotels
Authorization: Basic <credentials>
```

#### Обновить рейтинг отеля

```http
PUT /api/hotels/{id}/rating
Authorization: Basic <credentials>
Content-Type: application/json

{
  "newMark": 5
}
```

**newMark:** от 1 до 5

#### Поиск отелей с фильтрацией и пагинацией

```http
GET /api/hotels/search?city=Moscow&minRating=4.0&page=0&pageSize=10
Authorization: Basic <credentials>
```

**Параметры фильтрации:**
- `id` — ID отеля
- `name` — название (поиск по подстроке)
- `headline` — заголовок (поиск по подстроке)
- `city` — город (поиск по подстроке)
- `address` — адрес (поиск по подстроке)
- `minDistance`, `maxDistance` — расстояние от центра
- `minRating`, `maxRating` — рейтинг
- `minNumberOfRatings`, `maxNumberOfRatings` — количество оценок
- `page` — номер страницы (начиная с 0)
- `pageSize` — размер страницы

**Response:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "Grand Hotel",
      "headline": "Luxury hotel",
      "city": "Moscow",
      "address": "Red Square, 1",
      "distanceFromCenter": 0.5,
      "rating": 4.5,
      "numberOfRatings": 120
    }
  ],
  "totalCount": 1,
  "page": 0,
  "pageSize": 10
}
```

---

### 🛏 Rooms API

#### Получить комнату по ID

```http
GET /api/rooms/{id}
Authorization: Basic <credentials>
```

**Response:**
```json
{
  "id": 1,
  "name": "Deluxe Suite",
  "description": "Spacious room with city view",
  "roomNumber": 101,
  "price": 150.0,
  "maxGuests": 2,
  "hotelId": 1,
  "hotelName": "Grand Hotel"
}
```

#### Создать комнату (только ADMIN)

```http
POST /api/rooms
Authorization: Basic admin:password
Content-Type: application/json

{
  "name": "Deluxe Suite",
  "description": "Spacious room with city view",
  "roomNumber": 101,
  "price": 150.0,
  "maxGuests": 2,
  "hotelId": 1
}
```

#### Обновить комнату (только ADMIN)

```http
PUT /api/rooms/{id}
Authorization: Basic admin:password
Content-Type: application/json

{
  "name": "Deluxe Suite Updated",
  "description": "New description",
  "roomNumber": 101,
  "price": 175.0,
  "maxGuests": 3,
  "hotelId": 1
}
```

#### Удалить комнату (только ADMIN)

```http
DELETE /api/rooms/{id}
Authorization: Basic admin:password
```

#### Поиск комнат с фильтрацией

```http
GET /api/rooms/search?hotelId=1&minPrice=100&maxPrice=200&checkIn=2024-12-25&checkOut=2024-12-30&page=0&pageSize=10
Authorization: Basic <credentials>
```

**Параметры фильтрации:**
- `id` — ID комнаты
- `name` — название (поиск по подстроке)
- `minPrice`, `maxPrice` — диапазон цен
- `minGuests`, `maxGuests` — количество гостей
- `checkIn`, `checkOut` — даты (YYYY-MM-DD) — показывает только свободные номера
- `hotelId` — ID отеля
- `page`, `pageSize` — пагинация

**Важно:** Для фильтрации по датам нужно указать **оба** параметра (`checkIn` и `checkOut`)

---

### 📅 Bookings API

#### Создать бронирование

```http
POST /api/bookings
Authorization: Basic <credentials>
Content-Type: application/json

{
  "roomId": 1,
  "userId": 1,
  "checkIn": "2024-12-25",
  "checkOut": "2024-12-30"
}
```

**Response:**
```json
{
  "id": 1,
  "roomId": 1,
  "roomName": "Deluxe Suite",
  "userId": 1,
  "username": "john_doe",
  "checkIn": "2024-12-25",
  "checkOut": "2024-12-30"
}
```

**Ошибка, если комната занята:**
```json
{
  "statusCode": 400,
  "message": "Room is not available for the requested dates"
}
```

#### Получить все бронирования (только ADMIN)

```http
GET /api/bookings?page=0&pageSize=20
Authorization: Basic admin:password
```

---

### 📊 Statistics API

#### Скачать статистику в CSV (только ADMIN)

```http
GET /api/statistics/export/csv
Authorization: Basic admin:password
```

**Скачивает файл:** `statistics.csv`

**Формат CSV:**
```csv
ID,Event Type,User ID,Check In,Check Out,Created At
1,USER_REGISTRATION,1,,,2024-03-15T10:30:00
2,ROOM_BOOKING,1,2024-12-25,2024-12-30,2024-03-15T11:00:00
```

---

## 🔒 Безопасность

### Роли и доступ

| Endpoint | USER | ADMIN |
|----------|------|-------|
| `POST /api/users/register` | ✅ (без авторизации) | ✅ (без авторизации) |
| `GET /api/hotels/*` | ✅ | ✅ |
| `POST /api/hotels` | ❌ | ✅ |
| `PUT /api/hotels/{id}` | ❌ | ✅ |
| `DELETE /api/hotels/{id}` | ❌ | ✅ |
| `PUT /api/hotels/{id}/rating` | ✅ | ✅ |
| `GET /api/rooms/*` | ✅ | ✅ |
| `POST /api/rooms` | ❌ | ✅ |
| `PUT /api/rooms/{id}` | ❌ | ✅ |
| `DELETE /api/rooms/{id}` | ❌ | ✅ |
| `POST /api/bookings` | ✅ | ✅ |
| `GET /api/bookings` | ❌ | ✅ |
| `GET /api/statistics/export/csv` | ❌ | ✅ |

### Создание администратора

```http
POST /api/users/register
Content-Type: application/json

{
  "username": "admin",
  "password": "adminPassword",
  "email": "admin@hotel.com",
  "role": "ADMIN"
}
```

## 📈 Статистика

### События, отслеживаемые системой:

1. **USER_REGISTRATION** — регистрация нового пользователя
   - Сохраняется: `userId`

2. **ROOM_BOOKING** — бронирование комнаты
   - Сохраняется: `userId`, `checkIn`, `checkOut`

### Архитектура статистики

```
Registration/Booking
        ↓
    Kafka Producer
        ↓
    Kafka Topics
        ↓
    Kafka Consumer
        ↓
    MongoDB (JSON)
        ↓
    CSV Export Service
        ↓
    CSV File Download
```

## 💡 Примеры использования

### Сценарий 1: Регистрация и поиск отеля

```bash
# 1. Регистрация пользователя
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "password123",
    "email": "alice@example.com",
    "role": "USER"
  }'

# 2. Поиск отелей в Москве с рейтингом выше 4.0
curl -X GET "http://localhost:8080/api/hotels/search?city=Moscow&minRating=4.0" \
  -u alice:password123

# 3. Оценить отель
curl -X PUT http://localhost:8080/api/hotels/1/rating \
  -u alice:password123 \
  -H "Content-Type: application/json" \
  -d '{"newMark": 5}'
```

### Сценарий 2: Администратор создаёт отель и комнаты

```bash
# 1. Регистрация администратора
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "email": "admin@hotel.com",
    "role": "ADMIN"
  }'

# 2. Создание отеля
curl -X POST http://localhost:8080/api/hotels \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Paradise Resort",
    "headline": "Beachfront luxury resort",
    "city": "Sochi",
    "address": "Beach Street, 10",
    "distanceFromCenter": 5.0
  }'

# 3. Создание комнаты
curl -X POST http://localhost:8080/api/rooms \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ocean View Suite",
    "description": "Beautiful ocean view",
    "roomNumber": 201,
    "price": 250.0,
    "maxGuests": 2,
    "hotelId": 1
  }'
```

### Сценарий 3: Бронирование комнаты

```bash
# 1. Поиск свободных комнат на даты 25-30 декабря
curl -X GET "http://localhost:8080/api/rooms/search?checkIn=2024-12-25&checkOut=2024-12-30" \
  -u alice:password123

# 2. Бронирование комнаты
curl -X POST http://localhost:8080/api/bookings \
  -u alice:password123 \
  -H "Content-Type: application/json" \
  -d '{
    "roomId": 1,
    "userId": 1,
    "checkIn": "2024-12-25",
    "checkOut": "2024-12-30"
  }'
```

### Сценарий 4: Экспорт статистики

```bash
# Скачать CSV-файл со статистикой (только для администратора)
curl -X GET http://localhost:8080/api/statistics/export/csv \
  -u admin:admin123 \
  -o statistics.csv
```

## 🐛 Обработка ошибок

### Коды ответов

- **200 OK** — успешная операция
- **201 Created** — ресурс создан
- **204 No Content** — ресурс удалён
- **400 Bad Request** — некорректные данные запроса
- **404 Not Found** — ресурс не найден
- **500 Internal Server Error** — внутренняя ошибка сервера

### Примеры ошибок

**404 Not Found:**
```json
{
  "statusCode": 404,
  "message": "Hotel not found with id: 999"
}
```

**400 Bad Request:**
```json
{
  "statusCode": 400,
  "message": "Username already exists: john_doe"
}
```

**400 Bad Request (валидация):**
```json
{
  "statusCode": 400,
  "message": "Rating must be between 1 and 5"
}
```

## 🔧 Конфигурация

### application.properties

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_booking_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017
spring.data.mongodb.database=hotel_statistics_db

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
app.kafka.topic.user-registration=user-registration-topic
app.kafka.topic.room-booking=room-booking-topic
```

## 📝 Логирование

Просмотр логов:

```bash
# Логи приложения
docker-compose logs -f hotel-booking-app

# Логи PostgreSQL
docker-compose logs -f postgres-db

# Логи Kafka
docker-compose logs -f kafka
```

## 🧪 Тестирование

### Запуск тестов

```bash
mvn test
```

### Проверка здоровья сервисов

```bash
# PostgreSQL
docker exec -it hotel-postgres psql -U postgres -c "\l"

# MongoDB
docker exec -it hotel-mongo mongosh --eval "show dbs"

# Kafka topics
docker exec -it hotel-kafka kafka-topics --list --bootstrap-server localhost:9092
```

## 🤝 Контрибьюция

При разработке следуйте:
- Слоистой архитектуре (Controller → Service → Repository)
- Принципам SOLID
- Использованию DTO для всех API endpoints
- Обработке всех исключений через `@ControllerAdvice`

## 📄 Лицензия

Учебный проект для курса «Разработка на Spring Framework»

---

**Разработано с ❤️ для финального проекта курса Spring Framework**
