package DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("id")
    private Long id; // Optional: Useful for frontend

    // No-args constructor
    public AuthorDTO() {}

    // Constructor with username only
    public AuthorDTO(String username) {
        this.username = username;
    }

    // Constructor with id and username
    public AuthorDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
