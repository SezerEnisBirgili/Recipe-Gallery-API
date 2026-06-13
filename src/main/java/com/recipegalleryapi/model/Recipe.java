package com.recipegalleryapi.model;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Recipe {
	
	@Id
	private String id;

	@NotBlank(message = "Name is required")
	private String name;

	private String description;
	private String instructions;
	private String image;
	private String imagePath;


	public Recipe(String name, String description, String instructions, String image, String imagePath) {
		super();
		this.name = name;
		this.description = description;
		this.instructions = instructions;
		this.image = image;
		this.imagePath = imagePath;
	}
}
