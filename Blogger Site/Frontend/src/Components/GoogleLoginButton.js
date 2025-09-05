import React from "react";
import { GoogleLogin, GoogleOAuthProvider } from "@react-oauth/google";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const GoogleLoginButton = () => {
    const navigate = useNavigate();

    const handleSuccess = async (response) => {
        console.log("Google Login Success:", response);

        try {
            // Send Google token to backend
            const res = await axios.post("http://localhost:8080/auth/google", {
                token: response.credential,
            });

            console.log("Google Login Response:", res.data);

            // Store user data in localStorage
            localStorage.setItem("token", res.data.token);
            localStorage.setItem("user", JSON.stringify(res.data));

            // Redirect to Dashboard
            navigate("/dashboard");
        } catch (error) {
            console.error("Login Error:", error);
        }
    };

    const handleFailure = (error) => {
        console.error("Google Login Failed:", error);
    };

    return (
        <>
        <GoogleOAuthProvider clientId="490647164435-3hn4egh6fkdg1natanm509fru9r10pmn.apps.googleusercontent.com">
        <div className="flex justify-center mt-10">
            <GoogleLogin onSuccess={handleSuccess} onError={handleFailure} />

            
        </div>
        </GoogleOAuthProvider>
        </>
    );
};

export default GoogleLoginButton;
