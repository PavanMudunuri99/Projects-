import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { deletePost } from "../Api";

const UserDashboard1 = () => {
  const [user, setUser] = useState(null);
  const [blogs, setBlogs] = useState([]);
  const [editBlog, setEditBlog] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserBlogs = async (userId) => {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          alert("Not authenticated! Please login.");
          navigate("/");
          return;
        }

        const response = await axios.get(`http://localhost:8080/blogs/my-blogs?userId=${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        setBlogs(response.data);
      } catch (error) {
        console.error("Error fetching user blogs:", error);
        alert("Session expired. Please log in again.");
        navigate("/");
      }
    };

    const fetchUser = () => {
      try {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || !storedUser.id) {
          throw new Error("Invalid user data");
        }

        setUser(storedUser);
        fetchUserBlogs(storedUser.id);
      } catch (error) {
        console.error("Error retrieving user from storage:", error);
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        alert("Session error. Please log in again.");
        navigate("/");
      }
    };

    fetchUser();
  }, [navigate]);

  // 📝 Handle Update Blog
  const handleUpdate = async (blogId) => {
    try {
      if (!editBlog) return;

      const token = localStorage.getItem("token");
      const updatedBlog = {
        ...editBlog,
        categoryId: editBlog.categoryId || null, // Ensure categoryId is handled properly
      };

      const response = await axios.put(
        `http://localhost:8080/blogs/${blogId}`,
        updatedBlog,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        setBlogs(blogs.map((blog) => (blog.id === blogId ? response.data : blog)));
        setEditBlog(null);
        alert("Blog updated successfully!");
      } else {
        throw new Error("Failed to update blog");
      }
    } catch (error) {
      console.error("Error updating blog:", error);
      alert("Update failed! Try again.");
    }
  };

  // ❌ Handle Delete Blog
  const handleDelete = async (blogId) => {
    if (!window.confirm("Are you sure you want to delete this blog?")) return;

    try {
      const token = localStorage.getItem("token");
      await deletePost(blogId, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setBlogs(blogs.filter((blog) => blog.id !== blogId));
      alert("Blog deleted successfully!");
    } catch (error) {
      console.error("Error deleting blog:", error);
      alert("Delete failed! Try again.");
    }
  };

  return (
    <main className="flex flex-col items-center min-h-screen px-6 py-10 bg-gray-100 dark:bg-gray-900">
      <div className="w-full max-w-3xl p-6 bg-white shadow-lg dark:bg-gray-800 rounded-xl">
        {user ? (
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-800 dark:text-white">
              👋 Welcome, <span className="text-blue-600 dark:text-blue-400">{user.username}</span>
            </h2>
            <p className="text-sm text-gray-600 dark:text-gray-300">📧 {user.email}</p>
          </div>
        ) : (
          <p className="text-center text-gray-500 dark:text-gray-400 animate-pulse">Loading user...</p>
        )}

        <div className="mt-6">
          <h3 className="pb-2 text-lg font-semibold text-gray-700 border-b dark:text-gray-200">
            ✍️ Your Blog Posts
          </h3>

          {blogs.length > 0 ? (
            <div className="mt-4 space-y-4">
              {blogs.map((blog) => (
                <div key={blog.id} className="p-4 border rounded-lg shadow-md bg-gray-50 dark:bg-gray-900">
                  {editBlog?.id === blog.id ? (
                    <div>
                      <input
                        type="text"
                        value={editBlog.title || ""}
                        onChange={(e) => setEditBlog({ ...editBlog, title: e.target.value })}
                        className="w-full p-2 border rounded-lg dark:bg-gray-700 dark:text-white"
                      />
                      <textarea
                        value={editBlog.content || ""}
                        onChange={(e) => setEditBlog({ ...editBlog, content: e.target.value })}
                        className="w-full p-2 mt-2 border rounded-lg dark:bg-gray-700 dark:text-white"
                      />
                      <div className="flex justify-end mt-2 space-x-2">
                        <button
                          onClick={() => handleUpdate(blog.id)}
                          className="px-4 py-2 text-white transition bg-blue-500 rounded-lg hover:bg-blue-600"
                        >
                          Save
                        </button>
                        <button
                          onClick={() => setEditBlog(null)}
                          className="px-4 py-2 text-white transition bg-gray-500 rounded-lg hover:bg-gray-600"
                        >
                          Cancel
                        </button>
                      </div>
                    </div>
                  ) : (
                    <div>
                      <h4 className="text-xl font-bold text-gray-800 dark:text-white">{blog.title}</h4>
                      <p className="mt-2 text-gray-600 dark:text-gray-300">{blog.content}</p>
                      <div className="flex justify-end mt-3 space-x-2">
                        <button
                          onClick={() => setEditBlog(blog)}
                          className="px-4 py-2 text-white transition bg-yellow-500 rounded-lg hover:bg-yellow-600"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleDelete(blog.id)}
                          className="px-4 py-2 text-white transition bg-red-500 rounded-lg hover:bg-red-600"
                        >
                          Delete
                        </button>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <p className="mt-4 text-center text-gray-500 dark:text-gray-400">No blogs found.</p>
          )}
        </div>
      </div>
    </main>
  );
};

export default UserDashboard1;
