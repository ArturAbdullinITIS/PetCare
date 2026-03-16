# MeetUp

MeetUp is a mobile application designed to help people easily organize and join meetings with friends.  
The app allows users to create events on a map, invite friends, chat inside meetings, and manage social connections in one place.

---

## Project Goal

The goal of the MeetUp project is to build a mobile application that simplifies the process of organizing real-life meetups with friends by combining maps, social features, and chat functionality.

---

## Technology Stack

### Android
- Jetpack Compose
- Room
- Firebase
- Hilt
- DataStore

---

# Features

## User Interface

The application contains the following screens:

- Registration
- Authorization (Login)
- Home Screen
- Profile
- Friends
- Meetings
- Meeting Details
- Meeting Chat Room
- Create Meeting
- Settings

---

# Navigation

The main navigation in the application is done through a bottom navigation bar with the following screens:

- Home
- Profile
- Friends
- Meetings

---

# Screen Functionality

## Registration Screen

- Register using email and password
- Sign up using a Google account
- Navigation to the Login screen

---

## Login Screen

- Login using email and password
- Login using a Google account
- Navigation to the Registration screen

---

## Home Screen

- Interactive map displaying meetings
- Tap on the map to create a meeting
- Tap on a meeting marker to view meeting details
- Button to center the map on the user's location
- Access to navigation bar

---

## Create Meeting Screen

Users can create a meeting with the following options:

- Add an image
- Add meeting title
- Select date and time
- Choose meeting location
- Add description
- Set meeting type:
  - Open Meeting
  - Private Meeting

### Meeting Types

Open Meeting
- Notifications are sent to all friends
- Meeting is visible to all friends on the map

Private Meeting
- Available only by invitation
- Creator selects friends to invite
- Meeting is visible only to invited users

---

## Profile Screen

- Add or change profile picture
- Change username
- User Tag for quick friend addition (with copy functionality)
- Display linked Email or Google account
- Access Settings

---

## Friends Screen

- List of friends
- Button to add a friend
- Tabs for Friend Requests
  - Sent requests
  - Incoming invitations
- Adding a friend via User Tag

---

## Meetings Screen

Contains three tabs:

- My Meetings – meetings created by the user
- Friends' Meetings – meetings created by friends
- Requests – meeting invitations

Each meeting is displayed as a card that opens the Meeting Details screen.

---

## Meeting Details Screen

Displays full information about the meeting:

- Image
- Title
- Description
- Date and time
- Location

Actions available:

- Button to open the Meeting Chat
- Button to change attendance status:
  - "I'm going"
  - "I'm not going"

Meetings with status "I'm going" are stored in the local database.

---

## Meeting Chat Screen

- Text chat with all meeting participants
- Ability to send text messages

---

## Settings Screen

- Toggle notifications
- Switch theme (Light / Dark)
- Change language (Russian / English)
- Log out
- Delete account

---

# Key Features

- Map-based meetup discovery
- Social interaction with friends
- Meeting invitations and attendance tracking
- Built-in meeting chat
- Cross-platform mobile support (Android & iOS)

---

# Future Improvements

- Push notifications
- Media sharing in chat
- Event reminders
- Advanced search and filters
- Public event discovery

---

# License

This project is developed for educational purposes.
