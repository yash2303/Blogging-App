# Blogging App

## JSON Entities

### USER
```json
{
  "id": 12,
  "username": "johndoe",
  "email": "johndoe@gmail.com",
  "password": "****",
  "bio": "I am a writer"
}
```

### USER PROFILE
```json
{
  "id": 12,
  "username": "johndoe",
  "bio": "I am a writer"
}
```

### BLOG
```json
{
  "id": 45,
  "title": "How to blog",
  "authorId": 12,
  "content": "A quick brown fox jumps over the lazy dog.",
  "updatedAt": "2022-02-06 03:40:55",
  "createdAt": "2022-02-06 03:40:55"
}
```

### COMMENT
```json
{
  "id": 78,
  "blogId": 45,
  "commenterId": 12,
  "text": "This is an excellent comment",
  "createdAt": "2022-02-07 03:40:55",
  "updatedAt": "2022-02-07 03:40:55"
}
```
---
## API Endpoints

### `POST /users/register` 
Register a new user
### `POST /users/login`
Sign-in a user
### `GET /users/profile/{userId}`
Retrieve user profile for input _userId_
### `PATCH /users/{userId}`
Update user profile
### `DELETE /users/{userId}`
Delete user with input userId

---
### `POST /blogs`
Create a new blog
### `GET /blogs/feed` 📄
Retrieve user feed
### `GET /blogs/{blogId}`
Retrieve blog
### `PATCH /blogs/{blogId}`
Update blog
### `DELETE /blogs/{blogId}`
Delete blog

---
### `POST /blogs/{blogId}/comments`
Post a new comment on given blog
### `GET /blogs/{blogId}/comments` 📄
Retrieve comments for a given blog
### `PATCH /blogs/{blogId}/comments/{commentId}`
Update comment
### `DELETE /blogs/{blogId}/comments/{commentId}`
Delete comment

---