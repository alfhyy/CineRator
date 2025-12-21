<img width="100" height="100" alt="CineRator Logo" src="https://github.com/user-attachments/assets/efafab89-5f70-4bcb-8f06-ab54853090c5" />

# CineRator📽️⭐




![Image](https://github.com/user-attachments/assets/47921165-122b-41a5-9bc0-ad1c212de6d9)

![GitHub repo size](https://img.shields.io/github/repo-size/alfhyy/CineRator)
![GitHub issues](https://img.shields.io/github/issues/alfhyy/CineRator)
![GitHub stars](https://img.shields.io/github/stars/alfhyy/CineRator?style=social)
![GitHub forks](https://img.shields.io/github/forks/alfhyy/CineRator?style=social)

App built with Java 21 and Swing, what does it do??? Rating, review ur fav movies.
rate 1-10 and share personal opinions, designed for simplicity, lightweight, and offline use. Using intuitive Swing interface , user can browse movies view average ratings, and read community reviews.
---




## ✨ Features

🔐 Login system  
Secure user authentication with username & password.

🎬 Add movie  
Users can add new movies to the database (title, genre, poster).

⭐ Rate movie (1–10 stars)  
Simple rating system with average score calculation.

📝 Opinion / description  
Users can write short reviews or descriptions for each movie.

📂 CSV/XLSX storage  
All movies, ratings, and reviews are saved in structured files.

🖥️ Java Swing UI  
Classic desktop interface with forms, tables, and dialogs.

---



## 🛠️ Technologies Used
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![Swing](https://img.shields.io/badge/UI-Swing-orange?logo=java)
![Apache POI](https://img.shields.io/badge/Apache_POI-Excel%2FXLSX-green?logo=apache)
![OpenCSV](https://img.shields.io/badge/OpenCSV-CSV-lightgrey?logo=files)
- **Java 21 (SE)**  
- **Java Swing** (UI toolkit)  
- **Apache POI** (for XLSX file handling)  
- **OpenCSV** (for CSV file handling)  

---

## 🚀 Installation

###  Clone the repository
     ```bash
     git clone https://github.com/alfhyy/CineRator.git
     cd movie-rating-app
## 📁Project Structure
     cinerator/
          ├── src/
          │   └── main/
          │       └── java/
          │           └── cinerator/
          │               ├── model/
          │               │   ├── Movie.java
          │               │   ├── Rating.java
          │               │   └── User.java
          │               ├── service/
          │               │   ├── MovieService.java
          │               │   ├── RatingService.java
          │               │   └── UserService.java
          │               ├── storage/
          │               │   ├── ExcelStorage.java
          │               │   └── StorageManager.java
          │               ├── UI/
          │               │   ├── AddMovieView.java
          │               │   ├── DashView.java
          │               │   ├── LoginView.java
          │               │   └── RatedView.java
          │               ├── AppController.java
          │               └── Main.java
          ├── resources/
          ├── target/
          ├── cinerator.xlsx
          ├── pom.xml
          └── .gitignore
## 💻UI Preview
<img width="391" height="248" alt="image" src="https://github.com/user-attachments/assets/e02068f2-4819-4ef2-8fe5-42fc2d58f4a7" /><img width="391" height="248" alt="image" src="https://github.com/user-attachments/assets/022f9ce3-2fe8-4439-b5d8-0480731b679e" />

### 🔐Login page 
Offer username and password authentication with clean and centered layout, so program know who's login. A bold red "Sign in" button emphasizing action and urgency.
Dont worry if u new here, u can straight to registration and get ur own user account, and rate all movies u want

 <img width="391" height="248" alt="image" src="https://github.com/user-attachments/assets/15748dcd-b503-4f77-bec0-97f846712aea" /><img width="318" height="248" alt="Screenshot 2025-12-21 203200" src="https://github.com/user-attachments/assets/15ce4f34-f753-4704-a80c-e2690775d88d" />

### 📊Dashboard page 
Presents two-pane layout, a sidebar navigation and main content area for movie and community inteeaction. Sidebar, includes the "CineRator" brand and intuitive menu options: Discover, Rated Movies, Add Movies which supports all element in movie rating app. Main panel show the "Community Movie List" with movie card displaying title, genres, and rating. Search bar offers user to find specific movie by related keyword, enhancing usability and engangement.
### ⭐Rate Panel
Show slider from 1-10 how good the movie was, also review box to share ur opinion.

<img width="441" height="298" alt="image" src="https://github.com/user-attachments/assets/1d650034-42e1-46d2-9c6a-6eb19f3082ab" />  <img width="267" height="297" alt="image" src="https://github.com/user-attachments/assets/58f9b2ee-ac98-40d7-a904-9a77c309d8e0" />

### ⭐Rated Movies Page
User can see movie they already rated, to review whats been rated. User also can click on the movie and add opinion/review about the movie

<img width="441" height="298" alt="image" src="https://github.com/user-attachments/assets/1575d74a-ac5f-4038-af00-07f2fa1a4a56" />

### 🎬Add Movie Page
Offer user to add their favourite movie, by entering title, genre, rating(1-10), and poster to add more movie details, also add preview image to ensure the correct  image are inserted, simply by clicking add button, movie will appear on the dashboard.

![Image](https://github.com/user-attachments/assets/3d2a3454-c604-47af-83e4-2ea88a366521)

