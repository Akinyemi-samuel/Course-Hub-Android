# Course Booking Mobile App

Welcome to the Course Booking Mobile App! This application allows users to browse, search for, and book courses. It provides a user-friendly interface and a smooth booking experience. Users can either browse as a guest or log in to access additional features such as booking courses and managing their profile.

## Features

### User Authentication

![User Authentication](images/authentication.png)

- Users can create an account or log in using their email and password or social media accounts.
- Password reset functionality is included for user convenience, utilizing Java Mail for sending emails.
- JSON Web Tokens (JWT) are used for secure authentication and authorization.

### Guest Mode

![Guest Mode](images/guest_mode.png)

- Users can browse courses without logging in.
- Limited functionality includes viewing courses and categories but excludes booking and profile management.

### Home Page

![Home Page](images/home_page.png)

- Displays a list of available courses with relevant details such as course name, instructor, duration, date, time, location, and price.
- Users can filter courses by categories and search for specific courses using the search functionality.
- Notification page for upcoming courses and changes.

### Course Details

![Course Details](images/course_details.png)

- Detailed view for each course showing additional information such as description and reviews.
- Users can add courses to their favorites for easy access.

### Booking Process

![Booking Process](images/booking_process.png)

- Users can book courses by clicking on the "Book Now" button.
- Stripe payment gateway is integrated for secure payments.
- Booking summary with total cost is displayed before confirmation.

### User Profile

![User Profile](images/user_profile.png)

- Users have a profile section where they can view their booked courses, upcoming courses, and past courses.
- Ability to cancel bookings if needed.

### Additional Features

- Error handling and validation for user inputs to enhance user experience and application robustness.
- Forgot password functionality allowing users to reset their passwords securely.

## Technologies Used

- Android (Java) for the mobile application development.
- Spring Boot for the backend API development.
- MySQL for the database management.
- JWT for authentication and authorization.
- Java Mail for sending emails.
- Stripe for payment processing.
- Docker for code packaging and deployment.

## Backend

The backend API for this application is developed using Spring Boot. You can find the backend repository [here](https://github.com/your-username/backend-repo).

## Installation

1. Clone the repository.
2. Set up the backend Spring Boot API and MySQL database.
3. Set up the Android development environment and run the application on a mobile device or emulator.

## Usage

1. Launch the application on your mobile device.
2. Choose to browse as a guest or log in using your email or social media account.
3. Explore available courses, filter by categories, and search for specific courses.
4. View course details, add courses to favorites, and book courses with the payment gateway.
5. Manage bookings and view profile information.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your enhancements or bug fixes.

## License

This project is licensed under the [MIT License](LICENSE).
