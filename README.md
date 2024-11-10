### Registration & Authentication
1. User Registration: As a new user, I want to register with my email and password.
2. User Login: As a registered user, I want to log in with my email and password.
3. Logout: As a logged-in user, I want the ability to log out from my profile.
4. Forgot Password: As a user, I want an option to reset my password if I forget it.
5. Email Validation: Ensure the user provides a valid email format during registration.
6. Password Length Validation: Require a minimum password length for security.
7. Unique Email Check: Prevent users from registering with an already registered email.
8. Display Registration Errors: Show specific error messages for registration failures (e.g., weak password, duplicate email).
9. Display Login Errors: Show specific error messages for login failures (e.g., incorrect password).
10. Remember Me Option: As a user, I want an option to stay logged in.

### Profile Management
11. View Profile: As a user, I want to view my profile details after logging in.
12. Auto-Generated Nickname: Generate a nickname (adjective + animal) upon successful registration.
13. Display Nickname in Profile: Show the generated nickname in the user's profile.
14. Change Nickname: Allow the user to change their nickname if desired.
15. Save Nickname to Database: Ensure the generated nickname is saved to Firebase under the user's UID.
16. Retrieve Nickname from Database: Load and display the saved nickname upon profile load.
17. Show Animal-Based Avatar: Display an avatar based on the animal part of the nickname.
18. Edit Profile Details: Allow the user to update personal information, such as nickname.
19. Display Nickname Change Confirmation: Notify the user when their nickname is successfully updated.
20. Display Profile Update Errors: Show error messages if updating profile information fails.

### Avatar Management
21. Display Avatar Based on Nickname: Match the avatar with the animal part of the generated nickname (e.g., "Cat" -> cat avatar).
22. Select Default Avatar: If no animal XML file matches, display a default avatar.
23. Update Avatar on Nickname Change: Change the avatar when the user updates their nickname to a different animal.
24. Save Avatar Selection: Store the avatar selection in Firebase along with the nickname.
25. Retrieve Avatar Selection: Load the stored avatar selection upon profile load.
26. Verify Animal XML Files: Confirm all animal avatar XML files are correctly implemented.
27. Test Avatar Circular Shape: Ensure avatars are displayed as circles, not squares.
28. Avatar Placeholder on Load: Display a placeholder avatar until the correct avatar loads.

### Database (Firebase) Management
29. Save User to Firebase: Save user details in Firebase Realtime Database upon registration.
30. Retrieve User Data from Firebase: Fetch user details from Firebase upon login.
31. Handle Database Errors: Display a message when there is an error retrieving or saving data in Firebase.
32. Store Additional User Info: Save additional profile information (e.g., bio, profile completion status).
33. Database Security Rules: Ensure Firebase security rules prevent unauthorized data access.
34. Optimize Database Reads: Reduce database read frequency for better performance.
35. Ensure Data Consistency: Verify data is consistent across all screens after updates.


36. Create Consistent Profile Layout: Ensure the profile screen is well-organized and visually consistent.
37. Add Loading Indicator on Login: Display a loading indicator during the login process.
38. Add Loading Indicator on Registration: Display a loading indicator during the registration process.
39. Show Successful Registration Message: Confirm to users when registration completes.
40. Show Successful Login Message: Confirm to users when login completes.
41. Show Avatar in Circular Frame: Display all avatars with circular framing for consistency.
42. Responsive Design: Ensure the UI adapts to different screen sizes and resolutions.
43. Error Message Styling: Style error messages consistently across the app.
44. Confirmation Dialogs for Changes: Show a confirmation dialog for significant changes, like nickname or logout.
45. Smooth Transitions Between Screens: Add animations between screen transitions.

### Navigation
46. Bottom Navigation Bar: Display a bottom navigation bar for easy access to Main and Profile screens.
47. Profile Screen Access: Allow users to access their profile from the navigation bar.
48. Main Screen Access: Enable users to switch to the Main screen from the navigation bar.
49. Highlight Current Tab: Indicate the active tab on the navigation bar.
50. Disable Back Navigation After Logout: Prevent users from accessing the app after logging out.

### Error Handling
51. Database Read/Write Error Handling: Display appropriate messages for database read/write errors.
52. Authentication Error Handling: Show user-friendly messages for authentication errors.
53. Avatar Loading Failure: Display a default avatar if the avatar XML file fails to load.
54. Nickname Generation Error Handling: Handle cases where nickname generation fails.

### Testing & Debugging
55. Nickname Uniqueness Test: Ensure nicknames are generated with reasonable uniqueness.
56. Test All Nickname Combinations: Confirm each adjective-animal combination works correctly.
57. Unit Test Nickname Generator: Write tests to validate nickname generation logic.
58. Test Profile Update Feature: Validate the update functionality in the profile.
59. Test Database Connectivity: Ensure stable and reliable Firebase connectivity.
60. Debug Log for Errors: Add debug logging for database, authentication, and avatar loading issues.

These cases cover a wide range of functionality, from registration to avatar selection and error handling, ensuring a comprehensive development and testing process.
