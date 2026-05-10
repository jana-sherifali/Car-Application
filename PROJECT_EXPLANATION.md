# Carista - Easy Car System Explanation

## Main Idea

Carista is a simple Spring Boot MVC web application for browsing cars, booking cars, and managing cars from an admin dashboard.

The project uses:

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Thymeleaf
- MySQL
- Spring Security
- BCrypt password encryption

No JavaScript, CSS framework, or frontend framework is used.

## Login Accounts

The application creates one default admin account when it starts:

- Email: `admin@car.com`
- Password: `admin123`

Normal users are created from the register page.

## File Explanation

`pom.xml`

Contains all project dependencies: web, Thymeleaf, JPA, MySQL, Lombok, validation, security, and tests.

`src/main/resources/application.properties`

Contains the MySQL connection settings. The database name is `car`, Hibernate creates or updates tables automatically, and SQL queries are shown in the console.

`src/main/java/com/car/CarApplication.java`

The main class that starts the Spring Boot project. It also creates the default admin account if it does not already exist.

`entity/User.java`

Represents the `users` table. It stores name, email, encrypted password, and role. The role is either `USER` or `ADMIN`.

`entity/Car.java`

Represents the `cars` table. It stores car name, model, color, price, image path or URL, and availability.

`entity/Booking.java`

Represents the `bookings` table. It stores start date, end date, the user who booked, and the booked car. It uses `@ManyToOne` for user and car.

`repository/UserRepository.java`

Handles database operations for users. It also has `findByEmail(String email)` for login and registration checks.

`repository/CarRepository.java`

Handles database operations for cars.

`repository/BookingRepository.java`

Handles database operations for bookings.

`service/UserService.java`

Contains user logic. It registers new users, encrypts passwords, gives new users the `USER` role, and finds users by email.

`service/CarService.java`

Contains car logic. It gets all cars, gets one car by id, saves cars, and deletes cars.

`service/BookingService.java`

Contains booking logic. It gets all bookings and saves new bookings.

`config/CustomUserDetailsService.java`

Connects Spring Security login to the users stored in MySQL.

`config/SecurityConfig.java`

Controls authentication and authorization. Public pages can be opened by everyone, booking pages require login, and admin pages require the `ADMIN` role.

`controller/HomeController.java`

Handles the home page route `/`.

`controller/AuthController.java`

Handles register and login pages. Login submission is handled by Spring Security.

`controller/CarController.java`

Handles `/cars` and `/cars/{id}`. It sends car data to Thymeleaf using `Model`.

`controller/BookingController.java`

Handles the booking form and saving bookings. It reads the logged-in user from Spring Security and connects that user to the booking.

`controller/AdminController.java`

Handles the admin dashboard. Admin users can add, edit, delete, and view cars and bookings.

## Template Explanation

`home.html`

Shows the website introduction and a link to explore cars.

`login.html`

Shows the login form. The email input is named `username` because Spring Security expects that name by default.

`register.html`

Shows the registration form and validation messages.

`cars.html`

Loops through all cars using `th:each` and displays image, name, model, price, availability, and a details link.

`car-details.html`

Shows full details for one car and a booking link if the car is available.

`booking.html`

Shows the booking form with start date and end date.

`admin.html`

Shows the add/edit car form, all cars, edit/delete links, and all bookings.

## Application Flow

1. A visitor opens `/` and clicks Explore Cars.
2. The visitor can view all cars at `/cars`.
3. The visitor opens one car page at `/cars/{id}`.
4. To book a car, the visitor must login or register.
5. Registration saves a new user with role `USER` and an encrypted BCrypt password.
6. Login is handled by Spring Security.
7. A logged-in user books a car from `/booking?carId=...`.
8. The booking is saved with the logged-in user and selected car.
9. The booked car becomes unavailable.
10. An admin logs in with `admin@car.com` and `admin123`.
11. The admin opens `/admin` to add cars, edit cars, delete cars, and view bookings.

## Database Tables

JPA creates these tables automatically:

- `users`
- `cars`
- `bookings`

The `bookings` table contains foreign keys to `users` and `cars`.
