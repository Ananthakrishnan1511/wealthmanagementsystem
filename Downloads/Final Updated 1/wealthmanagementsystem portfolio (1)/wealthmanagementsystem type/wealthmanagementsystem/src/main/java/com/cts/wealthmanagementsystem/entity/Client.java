package com.cts.wealthmanagementsystem.entity;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data; // Assuming you are using Lombok for @Data
 
@Entity
@Data // Lombok annotation for getters, setters, equals, hashCode, toString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer identity;
 
    @NotBlank(message = "Full Name cannot be empty")
    @Size(min = 2, max = 100, message = "Full Name must be between 2 and 100 characters")
    private String fullName;
 
    @NotBlank(message = "Email Address cannot be empty")
    @Email(message = "Invalid email address format")
    private String emailAddress;
 
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String userName;
 
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
             message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    private String passWord;
 
    @NotBlank(message = "Date of Birth cannot be empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of Birth must be in YYYY-MM-DD format")
    private String dateOfBirth; // Storing as String, consider LocalDate for better handling
 
    @NotNull(message = "Age cannot be empty")
    @Min(value = 18, message = "You must be at least 18 years old")
    private Integer age; // Changed to Integer for proper numeric validation
 
    private boolean isApproved = false; // Draft status
 
    @Override
    public String toString() {
        return "Client [identity=" + identity + ", fullName=" + fullName + ", emailAddress=" + emailAddress
                + ", userName=" + userName + ", passWord=" + passWord + ", dateOfBirth=" + dateOfBirth
                + ", age=" + age + ", isApproved=" + isApproved + "]";
    }
}
 