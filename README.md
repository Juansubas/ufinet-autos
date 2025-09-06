# Ufinet Autos 🚗

Fullstack application for managing user cars.  
Each user can register and manage one or multiple cars.  
The system includes authentication, user and car management, and follows good development practices.  

---

## 📖 Project Context
The goal is to build an application that allows users to register and manage their cars.  
The application must handle user authentication, car CRUD operations, and secure access to resources.

---

## 🚀 Functional Requirements

### Authentication
- User registration (stored in database).
- Login with JWT.
- Access to features only with a valid token.

### Car Management
- CRUD operations for cars associated with the authenticated user:
  - Create a car with: brand, model, year, license plate, color.
  - List user’s cars.
  - Edit car details.
  - Delete a car.

---

## ⚙️ Technical Requirements

### Backend (Spring Boot)
- REST API with well-structured endpoints.
- JWT authentication with Spring Security.
- SQL Server integration using JPA/Hibernate.

### Frontend (Angular)
- Login screen.
- Car management screen (list, add, edit, delete).
- JWT token storage.

### Database (SQL Server)
- Script for table creation:
  - `users` table.
  - `cars` table (with `user_id` as a foreign key).

---

## ✨ Extra Features
- Search by license plate or model.
- Filter by year or brand.
- Upload car photo (simulated field).
- Responsive design in Angular frontend.

---

## 📂 Project Structure

- **backend/** → Spring Boot project  
- **frontend/** → Angular project  
- **database/** → SQL Server scripts  
- **postman/** → Postman collections  
- **.gitignore** → Git ignore rules  
- **README.md** → Project documentation  



