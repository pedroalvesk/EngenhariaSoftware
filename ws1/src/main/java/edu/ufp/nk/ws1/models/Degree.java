package edu.ufp.nk.ws1.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
public class Degree extends BaseModel {

	// Variables
	@Column(unique = true)
	private String name;

	@ManyToOne
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonBackReference
	private College college;

	// Constructor
	public Degree(String name){
		this.name = name;
	}

	// Gets & Sets
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
