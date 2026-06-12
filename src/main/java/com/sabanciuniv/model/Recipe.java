package com.sabanciuniv.model;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Recipe {
	
	@Id
	private String id;
	
	private String name;
	private String description;
	private String instructions;
	private String image;
	private String imagePath;

	public Recipe() {
		
	}
	
	
	
	public Recipe(String name, String description, String instructions, String image, String imagePath) {
		super();
		this.name = name;
		this.description = description;
		this.instructions = instructions;
		this.image = image;
		this.imagePath = imagePath;
	}
	
	



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getInstructions() {
		return instructions;
	}



	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}



	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}



	@Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof Recipe)
        {
        	Recipe temp = (Recipe) obj;
        	
            if(this.id.equals(temp.id)) {
            	
            	return true;
            }
                
        }
        return false;
    }
	
	@Override
    public int hashCode() {
        // TODO Auto-generated method stub
        
        return (this.id.hashCode());        
    }
	
	

}
