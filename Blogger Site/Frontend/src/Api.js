import axios from "axios";

// Base URLs
const AUTH_BASE_URL = "http://localhost:8080/login";
const API_BASE_URL = "http://localhost:8080";
const COMMENTS_BASE_URL = "http://localhost:8080/comments";

// Auth functions
export const registerUser = async (user) => {
  try {
    const response = await fetch(`${AUTH_BASE_URL}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    });

    const data = await response.json();
    
    if (!response.ok) {
      throw new Error(data.error || "Registration failed.");
    }
    
    return data;
  } catch (error) {
    return { error: error.message };
  }
};

export const loginUser = async (credentials) => {
  try {
    const response = await axios.post(`${AUTH_BASE_URL}/log`, credentials);

    console.log("Login Response:", response.data);

    if (response.data.token && response.data.user) {
      localStorage.setItem("token", response.data.token);  
      localStorage.setItem("username", response.data.user.username);
      localStorage.setItem("user", JSON.stringify(response.data.user));
    }

    return response.data;
  } catch (error) {
    console.error("Login failed:", error);
    throw error;
  }
};

export const verifyToken = async (token) => {
  const response = await fetch(`${AUTH_BASE_URL}/verify`, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.json();
};

export const verifyToken2 = async (token) => {
  const response = await fetch(`${AUTH_BASE_URL}/verifyy`, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.json();
};

// Blog functions
export const getAllPosts = () => axios.get(`${API_BASE_URL}/blogs/all`);

export const createPost = async (postData) => {
  const token = localStorage.getItem("token");
  return axios.post(`${API_BASE_URL}/blogs`, postData, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    }
  });
};

export const searchByKeyword = (keyword) => 
  axios.get(`${API_BASE_URL}/blogs/search?keyword=${keyword}`);

export const deletePost = async (id) => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Authentication required! Please log in.");
    return;
  }

  try {
    await axios.delete(`${API_BASE_URL}/blogs/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    alert("Blog deleted successfully!");
  } catch (error) {
    console.error("Error deleting blog:", error);
    alert("Delete failed! You may not have permission.");
  }
};

export const updatePost = async (id, post) => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Authentication required! Please log in.");
    return;
  }

  try {
    const response = await axios.put(`${API_BASE_URL}/blogs/${id}`, post, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error("Error updating blog:", error);
    alert("Update failed! You may not have permission.");
  }
};

export const getPaginatedPosts = async (page, size) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/blogs/paginated?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching paginated posts:", error);
    return { content: [], totalPages: 1 };
  }
};

export const getPostById = async (id) => {
  return axios.get(`${API_BASE_URL}/blogs/${id}`);
};

// Comment functions
export const getComments = async (blogPostId) => {
  return axios.get(`${COMMENTS_BASE_URL}/${blogPostId}`);
};

export const addComment = async (blogPostId, commentText) => {
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  if (!token) {
    console.error("❌ Authentication Error: Token is missing!");
    throw new Error("User is not authenticated");
  }

  const commentData = { 
    commenter: username,
    content: commentText,
  };

  return axios.post(`${COMMENTS_BASE_URL}/${blogPostId}`, commentData, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
};

export const deleteComment = async (commentId) => {
  return axios.delete(`${COMMENTS_BASE_URL}/${commentId}`);
};