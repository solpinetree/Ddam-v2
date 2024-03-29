package com.ddam.spring.domain.ask;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert 
public class AskFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long afid;   
	
	@NotNull  // NOT NULL
	@Column(length=100)
	private String filename;
	@NotNull  // NOT NULL
	@Column(length=100)
	private String originalname;
	
	private Long abid;
	
	@ManyToOne
	private AskBoard board;

}
